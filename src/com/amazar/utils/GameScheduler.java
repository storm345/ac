package com.amazar.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;

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
