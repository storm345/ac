package com.amazar.utils;

import org.bukkit.Location;

import com.amazar.plugin.ac;

public class ArenaTeams extends Arena {
private SerializableLocation blueSpawn;
private SerializableLocation redSpawn;
private String[] items = {"0"};
	public ArenaTeams(Location center, int radius, ArenaShape shape,
			ArenaType type, Location blueSpawn, Location redSpawn, int playerLimit, String[] items) {
		super(center, radius, shape, type, playerLimit);
		this.blueSpawn = ac.plugin.invalidLoc;
		this.redSpawn = ac.plugin.invalidLoc;
		if(blueSpawn != null){
		this.blueSpawn = new SerializableLocation(blueSpawn);
		}
		if(redSpawn != null){
		this.redSpawn = new SerializableLocation(redSpawn);
		}
		if(items != null){
			this.items = items;
		}
	}
	public Boolean isValid(){
		if(this.blueSpawn.equals(ac.plugin.invalidLoc )|| this.redSpawn.equals(ac.plugin.invalidLoc)){
			return false;
		}
		return true;
	}
	public void setBlueSpawn(Location loc){
		this.blueSpawn = new SerializableLocation(loc);
		return;
	}
	public void setRedSpawn(Location loc){
		this.redSpawn = new SerializableLocation(loc);
		return;
	}
	public Location getRedSpawnpoint(){
		return this.redSpawn.getLocation(ac.plugin.getServer());
	}
	public Location getBlueSpawnpoint(){
		return this.blueSpawn.getLocation(ac.plugin.getServer());
	}
	public void setItems(String[] ids){
		this.items = ids;
		return;
	}
	public String[] getItems(){
		return this.items;
	}
}
