package com.amazar.utils;

import org.bukkit.Location;

import com.amazar.plugin.ac;

public class ArenaCtf extends ArenaTeams {
SerializableLocation blueFlag;
SerializableLocation redFlag;
	public ArenaCtf(Location center, int radius, ArenaShape shape,
			ArenaType type, Location blueSpawn, Location redSpawn, Location blueFlag, Location redFlag, int playerLimit, String[] items) {
		super(center, radius, shape, type, blueSpawn, redSpawn, playerLimit, items);
		this.blueFlag = ac.plugin.invalidLoc;
		this.redFlag = ac.plugin.invalidLoc;
		if(blueFlag != null){
		this.blueFlag = new SerializableLocation(blueFlag);
		}
		if(redFlag != null){
		this.redFlag = new SerializableLocation(redFlag);
		}
	}
	public Boolean isValid(){
		if(this.blueFlag.equals(ac.plugin.invalidLoc )|| this.redFlag.equals(ac.plugin.invalidLoc)){
			return false;
		}
		return true;
	}
public void setBlueFlag(Location loc){
	this.blueFlag = new SerializableLocation(loc);
}
public void setRedFlag(Location loc){
	this.redFlag = new SerializableLocation(loc);
}
public Location getBlueFlag(){
	return this.blueFlag.getLocation(ac.plugin.getServer());
}
public Location getRedFlag(){
	return this.redFlag.getLocation(ac.plugin.getServer());
}
}
