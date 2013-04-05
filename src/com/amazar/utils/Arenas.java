package com.amazar.utils;

import java.io.File;
import java.util.Set;

import com.amazar.plugin.ac;

public class Arenas {
	ac plugin = null;
public Arenas(ac plugin){
	this.plugin = plugin;
}
public void setArena(String name, Arena toAdd){
	plugin.arenas.put(name, toAdd);
	this.saveArenas();
	return;
}
public void removeArena(String name){
	plugin.arenas.remove(name);
	this.saveArenas();
	return;
}
public Arena getArena(String name){
	return plugin.arenas.get(name);
}
public Boolean arenaExists(String name){
		return plugin.arenas.containsKey(name);
}
public void saveArenas(){
	ac.saveHashMapArena(plugin.arenas, plugin.getDataFolder().getAbsolutePath() + File.separator + "arenas.bin");
}
public void reloadArenas(){
	plugin.arenas = ac.loadHashMapArena(plugin.getDataFolder().getAbsolutePath() + File.separator + "arenas.bin");
}
public Set<String> getArenas(){
	return plugin.arenas.keySet();
}
}
