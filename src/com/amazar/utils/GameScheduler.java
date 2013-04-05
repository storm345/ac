package com.amazar.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

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
				if(!arenaInUse(arenaName) && arena.getHowManyPlayers() > 1){
					Minigame game = new Minigame(arena, arenaName);
					List<String> que = arena.getPlayers();
					for(String name:que){
						if(plugin.getServer().getPlayer(name).isOnline() && plugin.getServer().getPlayer(name) != null){
							game.join(name);
							arena.removePlayer(name);
							startGame(arena, arenaName, game);
						}
					}
					//TODO start game
					return true;
				}
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
			if(!arenaInUse(aname) && arena.getHowManyPlayers() > 1){
				Minigame game = new Minigame(arena, aname);
				for(String name:arenaque){
				game.join(name);
				arena.removePlayer(name);
				
				}
				startGame(arena, aname, game);
			}
		}
		return;
	}
	public void startGame(Arena arena, String arenaName, Minigame game){
		this.games.put(game.getGameId(), game);
		final List<String> players = game.getPlayers();
		List<String> blue = new ArrayList<String>();
		List<String> red = new ArrayList<String>();
		if(game.getGameType() == ArenaType.CTF || game.getGameType() == ArenaType.PUSH || game.getGameType() == ArenaType.TEAMS || game.getGameType() == ArenaType.TNTORI){
			for(String player:players){
				if(blue.size() > red.size()){
					red.add(player);
				}
				else if(red.size() > blue.size()){
					blue.add(player);
				}
				else{
					int rand = 1 + (int)(Math.random() * ((2 - 1) + 1));
					if(rand > 1){
						blue.add(player);
					}
					else{
						red.add(player);
					}
				}
			}
		}
		game.setBlue(blue);
		game.setRed(red);
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
				plugin.getServer().getPlayer(name).teleport(gameArena.getPlayerSpawnpoint());
			}
		}
		else{
			for(String name:players){
				plugin.getServer().getPlayer(name).teleport(arena.getCenter());
			}
		}
		final Map<String, Location> locations = new HashMap<String, Location>();
		for(String name:players){
			plugin.getServer().getPlayer(name).setWalkSpeed(0);
			locations.put(name, plugin.getServer().getPlayer(name).getLocation());
			plugin.getServer().getPlayer(name).sendMessage(ChatColor.GOLD+"Game preparing...");
		}
		final MinigameStartEvent start = new MinigameStartEvent(game);
		
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
				p.teleport(locations.get(name));
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				}
				plugin.getServer().getPluginManager().callEvent(start);
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
}
