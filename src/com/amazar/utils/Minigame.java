package com.amazar.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.scheduler.BukkitTask;

import com.amazar.plugin.ac;

public class Minigame {
	private ArenaType gameType = ArenaType.INAVLID;
	private List<String> players = new ArrayList<String>();
	private List<String> inplayers = new ArrayList<String>();
	private List<String> blue = new ArrayList<String>();
	private List<String> red = new ArrayList<String>();
	private String gameId = "";
	private Arena arena = null;
	private String arenaName = "";
	private String winner = "Unknown";
	private Boolean running = false;
	private BukkitTask task = null;
	public HashMap<String, Integer> lives = new HashMap<String, Integer>();
	public Minigame(Arena arena, String arenaName){
		this.gameId = UniqueString.generate();
		this.arena = arena;
		this.gameType = arena.getType();
		this.arenaName = arenaName;
	}
	public void joinBlue(String name){
		this.blue.add(name);
		return;
	}
	public void joinRed(String name){
		this.red.add(name);
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
		if(this.players.contains(playername)){
			this.players.remove(playername);
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
				MinigameStartEvent event = new MinigameStartEvent(game);
				ac.plugin.getServer().getPluginManager().callEvent(event);
			}}, 30l, 30l);
    	ac.plugin.getServer().getPluginManager().callEvent(new MinigameStartEvent(this));
    }
    public void end(){
    	this.running = false;
    	if(task != null){
    		task.cancel();
    	}
    	ac.plugin.getServer().getPluginManager().callEvent(new MinigameFinishEvent(this));
    }
}
