package com.amazar.utils;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.Location;

import com.amazar.plugin.ac;

public class MinigameMethods {
	private ac plugin = null;
	public MinigameMethods(){
		this.plugin = ac.plugin;
	}
    public Minigame inAGame(String playername){
    	HashMap<String, Minigame> games = plugin.gameScheduler.getGames();
    	Set<String> keys = games.keySet();
    	Boolean inAGame = false;
        Minigame mgame = null;
    	for(String key:keys){
    		Minigame game = games.get(key);
    		if(game.getPlayers().contains(playername)){
    			inAGame = true;
    			mgame = game;
    		}
    	}
    	if(inAGame){
    	return mgame;
    	}
    	return null;
    }
    public String isArena(Location loc){
    	Set<String> arenaNames = plugin.minigamesArenas.getArenas();
    	Boolean is = false;
    	String name = null;
    	for(String arenaName:arenaNames){
    	Arena arena = plugin.minigamesArenas.getArena(arenaName);
    	if(arena.isLocInArena(loc)){
    		is = true;
    		name = arenaName;
    	}
    	}
    	if(is){
    	return name;
    	}
    	return null;
    }
}
