package com.amazar.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;

import com.amazar.plugin.ac;

public class Minigame {
	private ArenaType gameType = ArenaType.INAVLID;
	private List<String> players = new ArrayList<String>();
	private List<String> inplayers = new ArrayList<String>();
	private List<String> blue = new ArrayList<String>();
	private List<String> red = new ArrayList<String>();
	private Map<String, ItemStack[]> oldInventories = new HashMap<String,ItemStack[]>();
	private String gameId = "";
	private Arena arena = null;
	private String arenaName = "";
	private String winner = "Unknown";
	private Boolean running = false;
	private BukkitTask task = null;
	private Scoreboard teams = null;
	public HashMap<String, Integer> lives = new HashMap<String, Integer>();
	public Minigame(Arena arena, String arenaName){
		this.gameId = UniqueString.generate();
		this.arena = arena;
		this.gameType = arena.getType();
		this.arenaName = arenaName;
		this.teams = ac.plugin.getServer().getScoreboardManager().getMainScoreboard();
	}
	public void joinBlue(String name){
		this.blue.add(name);
		return;
	}
	public void setOldInventories(Map<String, ItemStack[]> inventories){
		this.oldInventories = inventories;
		return;
	}
	public Map<String, ItemStack[]> getOldInventories(){
		return this.oldInventories;
	}
	public void joinRed(String name){
		this.red.add(name);
		return;
	}
	public Scoreboard getTeams(){
		return this.teams;
	}
	public void setTeamScoreboard(Scoreboard board){
		this.teams = board;
		return;
	}
	public void setBlue(List<String> blue){
		this.blue = blue;
		return;
	}
	public void setRed(List<String> red){
		this.red = red;
		return;
	}
    public List<String> getBlue(){
    	return this.blue;
    }
    public List<String> getRed(){
    	return this.red;
    }
    public List<String> getInPlayers(){
    	return this.inplayers;
    }
    public void setInPlayers(List<String> in){
    	this.inplayers = in;
    	return;
    }
    public void playerOut(String name){
    	if(this.inplayers.contains(name)){
    	this.inplayers.remove(name);
    	}
    }
	public Boolean join(String playername){
		if(players.size() < this.arena.getPlayerLimit()){
			players.add(playername);
			return true;
		}
		return false;
	}
	public void leave(String playername){
		
		this.getPlayers().remove(playername);
		if(this.getRed().contains(playername)){
			this.getRed().remove(playername);
		}
		if(this.getBlue().contains(playername)){
			this.getBlue().remove(playername);
		}
		if(this.teams.getTeam("blue"+this.gameId).getPlayers().contains(ac.plugin.getServer().getOfflinePlayer(playername))){
			this.teams.getTeam("blue"+this.gameId).removePlayer(ac.plugin.getServer().getOfflinePlayer(playername));
		}
		if(this.teams.getTeam("red"+this.gameId).getPlayers().contains(ac.plugin.getServer().getOfflinePlayer(playername))){
			this.teams.getTeam("red"+this.gameId).removePlayer(ac.plugin.getServer().getOfflinePlayer(playername));
		}
		this.playerOut(playername);
		Player player = ac.plugin.getServer().getPlayer(playername);
		if(player != null){
			player.getInventory().clear();
		}
		if(this.getOldInventories().containsKey(playername)){
			if(player != null){
		player.getInventory().setContents(this.getOldInventories().get(playername));
			}
		this.getOldInventories().remove(playername);
		}
		if(player != null){
			player.setGameMode(GameMode.SURVIVAL);
			player.teleport(player.getWorld().getSpawnLocation());
			player.sendMessage(ChatColor.GOLD+"Successfully left the minigame!");
			}
		for(String playerName:this.getPlayers()){
			if(ac.plugin.getServer().getPlayer(playerName) != null && ac.plugin.getServer().getPlayer(playerName).isOnline()){
				Player p=ac.plugin.getServer().getPlayer(playerName);
				p.sendMessage(ChatColor.GOLD+playername+" left the minigame!");
			}
		}
		return;
	}
	public Boolean isEmpty(){
		if(this.players.size() < 1){
			return true;
		}
		return false;
	}
	public String getGameId(){
		return this.gameId;
	}
	public String getArenaName(){
		return this.arenaName;
	}
	public Arena getArena(){
		return this.arena;
	}
    public ArenaType getGameType(){
    	return this.gameType;
    }
    public List<String> getPlayers(){
    	return this.players;
    }
    public void setWinner(String winner){
    	this.winner = winner;
    	return;
    }
    public String getWinner(){
    	return this.winner;
    }
    public Boolean getRunning(){
    	return this.running;
    }
    public void start(){
    	this.running = true;
    	final Minigame game = this;
    	this.task = ac.plugin.getServer().getScheduler().runTaskTimer(ac.plugin, new Runnable(){

			@Override
			public void run() {
				MinigameUpdateEvent event = new MinigameUpdateEvent(game);
				ac.plugin.getServer().getPluginManager().callEvent(event);
				return;
			}}, 30l, 30l);
    	ac.plugin.getServer().getPluginManager().callEvent(new MinigameStartEvent(this));
    }
    public void end(){
    	this.running = false;
    	if(task != null){
    		task.cancel();
    	}
    	Set<OfflinePlayer> bluePlayers = this.teams.getTeam("blue").getPlayers();
    	for(OfflinePlayer pl:bluePlayers){
    		this.teams.getTeam("blue").removePlayer(pl);
    	}
    	Set<OfflinePlayer> redPlayers = this.teams.getTeam("red").getPlayers();
    	for(OfflinePlayer pl:redPlayers){
    		this.teams.getTeam("red").removePlayer(pl);
    	}
    	this.teams.getTeam("blue"+this.gameId).unregister();
    	this.teams.getTeam("red"+this.gameId).unregister();
    	ac.plugin.getServer().getPluginManager().callEvent(new MinigameFinishEvent(this));
    }
}
