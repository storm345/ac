package com.amazar.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.fusesource.jansi.Ansi.Color;

import com.amazar.plugin.ac;

public class GameScheduler {
	private HashMap<String, Minigame> games = new HashMap<String, Minigame>();
	private ac plugin;
	public GameScheduler(){
		this.plugin = ac.plugin;
	}
	public Boolean joinGame(String playername, Arena arena, String arenaName){
		if(arena.isValid() && arena.getHowManyPlayers() < arena.getPlayerLimit() && plugin.getServer().getPlayer(playername).isOnline()){
			if(plugin.getServer().getPlayer(playername).isOnline()){
				arena.addPlayer(playername);
				List<String> arenaque = arena.getPlayers();
				if(!arenaque.contains(playername)){
					arenaque.add(playername);
				}
				for(String name:arenaque){
					if(!(plugin.getServer().getPlayer(name).isOnline() && plugin.getServer().getPlayer(name) != null)){
						arenaque.remove(name);
						for(String ppname:arenaque){
							if(plugin.getServer().getPlayer(ppname).isOnline() && plugin.getServer().getPlayer(ppname) != null){
								plugin.getServer().getPlayer(ppname).sendMessage(ChatColor.RED+"["+arenaName+":] "+ChatColor.GOLD+playername+" left the game que!");
							}
						}
					}
					else{
						plugin.getServer().getPlayer(name).sendMessage(ChatColor.RED+"["+arenaName+":] "+ChatColor.GOLD+playername+" joined the game que!");
					}
				}
				plugin.minigamesArenas.setArena(arenaName, arena);
				this.reCalculateQues();
				plugin.getServer().getPlayer(playername).sendMessage(ChatColor.GREEN+"In arena que!");
				return true;
			}
		}
		if(plugin.getServer().getPlayer(playername).isOnline()){
			plugin.getServer().getPlayer(playername).sendMessage(ChatColor.RED+"Minigame que full!");
		}
		return false;
	}
	public void reCalculateQues(){
		Set<String> arenaNames = plugin.minigamesArenas.getArenas();
		for(String aname:arenaNames){
			Arena arena = plugin.minigamesArenas.getArena(aname);
			List<String> arenaque = arena.getPlayers();
			for(String name:arenaque){
				if(!(plugin.getServer().getPlayer(name).isOnline() && plugin.getServer().getPlayer(name) != null)){
					arenaque.remove(name);
				}
			}
			if(arena.getTransitioning() == null){
				arena.setTransitioning(false);
			}
			if(!arenaInUse(aname) && arena.getHowManyPlayers() > 1 && !arena.getTransitioning()){
				arena.setTransitioning(true);
				plugin.minigamesArenas.setArena(aname, arena);
				final String arenaName = aname;
			    plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable(){

					@Override
					public void run() {
						String aname = arenaName;
						Arena arena = ac.plugin.minigamesArenas.getArena(aname);
						Minigame game = new Minigame(arena, aname);
						List<String> aquep = new ArrayList<String>();
						aquep.addAll(arena.getPlayers());
						for(String name:aquep){
						game.join(name);
						arena.removePlayer(name);
						}
						arena.setTransitioning(false);
						plugin.minigamesArenas.setArena(aname, arena);
						startGame(arena, aname, game);
					}}, 100l);
				
			}
		}
		return;
	}
	public void startGame(Arena arena, String arenaName, final Minigame game){
		this.games.put(game.getGameId(), game);
		Scoreboard teams = game.getTeams();
		Set<Team> CurrentTeams = teams.getTeams();
		Boolean hasRed = false;
		Boolean hasBlue = false;
		for(Team team:CurrentTeams){
			if(team.getName() == ("blue"+game.getGameId())){
				hasBlue = true;
			}
			else if(team.getName() == ("red"+game.getGameId())){
				hasRed = true;
			}
		}
		if(!hasBlue){
			teams.registerNewTeam("blue"+game.getGameId());
		}
		if(!hasRed){
			teams.registerNewTeam("red"+game.getGameId());
		}
		Team blueTeam = teams.getTeam("blue"+game.getGameId());
		Team redTeam = teams.getTeam("red"+game.getGameId());
		blueTeam.setPrefix(ChatColor.BLUE+"");
		redTeam.setPrefix(ChatColor.RED+"");
		final List<String> players = game.getPlayers();
		List<String> blue = new ArrayList<String>();
		List<String> red = new ArrayList<String>();
		Map<String, ItemStack[]> oldInv = new HashMap<String,ItemStack[]>();
		if(game.getGameType() == ArenaType.CTF || game.getGameType() == ArenaType.PUSH || game.getGameType() == ArenaType.TEAMS || game.getGameType() == ArenaType.TNTORI || game.getGameType() == ArenaType.PVP){
			for(String player:players){
				Player pl = plugin.getServer().getPlayer(player);
				oldInv.put(player,pl.getInventory().getContents());
				pl.getInventory().clear();
				pl.setGameMode(GameMode.SURVIVAL);
				if(blue.size() > red.size()){
					red.add(player);
					redTeam.addPlayer(plugin.getServer().getOfflinePlayer(player));
				}
				else if(red.size() > blue.size()){
					blue.add(player);
					blueTeam.addPlayer(plugin.getServer().getOfflinePlayer(player));
				}
				else{
					int rand = 1 + (int)(Math.random() * ((2 - 1) + 1));
					if(rand > 1){
						blue.add(player);
						blueTeam.addPlayer(plugin.getServer().getOfflinePlayer(player));
					}
					else{
						red.add(player);
						redTeam.addPlayer(plugin.getServer().getOfflinePlayer(player));
					}
				}
			}
		}
		game.setBlue(blue);
		game.setRed(red);
		game.setTeamScoreboard(teams);
		game.setOldInventories(oldInv);
		//allocated to teams
		final ArenaType type = game.getGameType();
		if(type == ArenaType.CTF){
			ArenaCtf ctf = (ArenaCtf) game.getArena();
			for(String name:blue){
				plugin.getServer().getPlayer(name).teleport(ctf.getBlueSpawnpoint());
			}
			for(String name:red){
				plugin.getServer().getPlayer(name).teleport(ctf.getRedSpawnpoint());
			}
		}
		else if(type == ArenaType.PUSH){
			ArenaPush gameArena = (ArenaPush) game.getArena();
			for(String name:blue){
				plugin.getServer().getPlayer(name).teleport(gameArena.getPlayerBlueSpawn());
			}
			for(String name:red){
				plugin.getServer().getPlayer(name).teleport(gameArena.getPlayerRedSpawn());
			}
		}
		else if(type == ArenaType.TEAMS){
			ArenaTeams gameArena = (ArenaTeams) game.getArena();
			for(String name:blue){
				plugin.getServer().getPlayer(name).teleport(gameArena.getBlueSpawnpoint());
			}
			for(String name:red){
				plugin.getServer().getPlayer(name).teleport(gameArena.getRedSpawnpoint());
			}
		}
		else if(type == ArenaType.TNTORI){
			ArenaTntori gameArena = (ArenaTntori) game.getArena();
			if(!gameArena.isProtected()){
				gameArena.markArena(Material.SANDSTONE);
			}
			for(String name:blue){
				plugin.getServer().getPlayer(name).teleport(gameArena.getPlayerBlueSpawn());
			}
			for(String name:red){
				plugin.getServer().getPlayer(name).teleport(gameArena.getPlayerRedSpawn());
			}
		}
		else if(type == ArenaType.PVP){
			ArenaPvp gameArena = (ArenaPvp) game.getArena();
			for(String name:blue){
				plugin.getServer().getPlayer(name).teleport(gameArena.getPlayerBlueSpawn());
			}
			for(String name:red){
				plugin.getServer().getPlayer(name).teleport(gameArena.getPlayerRedSpawn());
			}
		}
		else if(type == ArenaType.SURVIVAL){
			ArenaSurvival gameArena = (ArenaSurvival) game.getArena();
			for(String name:players){
				plugin.getServer().getPlayer(name).setGameMode(GameMode.SURVIVAL);
				plugin.getServer().getPlayer(name).getInventory().clear();
				plugin.getServer().getPlayer(name).teleport(gameArena.getPlayerSpawnpoint());
			}
		}
		else if(type == ArenaType.UCARS){
			UCarsArena gameArena = (UCarsArena) game.getArena();
			for(int i=0;i<players.size();i++){
				Player p = plugin.getServer().getPlayer(players.get(i));
				if(gameArena.getStartGrid().size() >= i){
					Location loc = gameArena.getStartGrid().get(i);
					Minecart car = (Minecart) loc.getWorld().spawnEntity(loc, EntityType.MINECART);
					p.teleport(gameArena.getStartGrid().get(i));
					p.eject();
					car.eject();
					car.setPassenger(p);
					//p.teleport(gameArena.getStartGrid().get(i));
				}
				else{
					p.teleport(game.getArena().getCenter());
				}
				
			}
		}
		else{
			for(String name:players){
				plugin.getServer().getPlayer(name).teleport(arena.getCenter());
			}
		}
		for(String name:players){
			plugin.getServer().getPlayer(name).setGameMode(GameMode.SURVIVAL);
			plugin.getServer().getPlayer(name).getInventory().clear();
		}
		final Map<String, Location> locations = new HashMap<String, Location>();
		for(String name:players){
			locations.put(name, plugin.getServer().getPlayer(name).getLocation());
			plugin.getServer().getPlayer(name).sendMessage(ChatColor.GOLD+"Game preparing...");
		}
		List<String> gameIn = new ArrayList<String>();
		gameIn.addAll(game.getPlayers());
		game.setInPlayers(gameIn);
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable(){

			@Override
			public void run() {
				for(String name:players){
					Player p=plugin.getServer().getPlayer(name);
					p.sendMessage(ChatColor.GOLD+type.toString().toLowerCase()+" beginning in...");
				}
				for(int i=10;i>0;i--){
				for(String name:players){
				Player p=plugin.getServer().getPlayer(name);
				p.sendMessage(ChatColor.GOLD+""+i);
				if(game.getGameType() != ArenaType.UCARS && game.getGameType() != ArenaType.INAVLID){
				p.teleport(locations.get(name));
				}
				if(game.getGameType() == ArenaType.UCARS){
					p.setWalkSpeed(0);
				}
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				}
				for(String name:players){
					Player p=plugin.getServer().getPlayer(name);
					p.sendMessage(ChatColor.GOLD+"Go!");
					}
				game.start();
				return;
			}});
		
		return;
	}
	public void updateGame(Minigame game){
		this.games.put(game.getGameId(), game);
		return;
	}
	public void stopGame(Arena arena, String arenaName){
		if(!arenaInUse(arenaName)){
			return;
		}
		removeArena(arenaName);
		reCalculateQues();
		return;
	}
	public void leaveQue(String playername, Arena arena, String arenaName){
		if(getQue(arena).contains(playername)){
			arena.removePlayer(playername);
		}
		for(String ppname:getQue(arena)){
			if(plugin.getServer().getPlayer(ppname).isOnline() && plugin.getServer().getPlayer(ppname) != null){
				plugin.getServer().getPlayer(ppname).sendMessage(ChatColor.RED+"["+arenaName+":] "+ChatColor.GOLD+playername+" left the game que!");
			}
		}
		reCalculateQues();
		return;
	}
	public List<String> getQue(Arena arena){
		return arena.getPlayers();
	}
    public Boolean arenaInUse(String arenaName){
    	Set<String> keys = this.games.keySet();
    	for(String key:keys){
    		Minigame game = this.games.get(key);
    		if(game.getArenaName().equalsIgnoreCase(arenaName)){
    			return true;
    		}
    	}
    	return false;
    }
    public Boolean removeArena(String arenaName){
    	Set<String> keys = this.games.keySet();
    	for(String key:keys){
    		Minigame game = this.games.get(key);
    		if(game.getArenaName().equalsIgnoreCase(arenaName)){
    			this.games.remove(game.getGameId());
    		}
    	}
    	return false;
    }
    public HashMap<String, Minigame> getGames(){
    	return this.games;
    }
}
