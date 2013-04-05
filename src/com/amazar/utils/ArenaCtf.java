package com.amazar.utils;

import org.bukkit.Location;

public class ArenaCtf extends ArenaTeams {
Location blueFlag = null;
Location redFlag  = null;
	public ArenaCtf(Location center, int radius, ArenaShape shape,
			ArenaType type, Location blueSpawn, Location redSpawn, Location blueFlag, Location redFlag) {
		super(center, radius, shape, type, blueSpawn, redSpawn);
		this.blueFlag = blueFlag;
		this.redFlag = redFlag;
	}
public void setBlueFlag(Location loc){
	this.blueFlag = loc;
}
public void setRedFlag(Location loc){
	this.redFlag = loc;
}
public Location getBlueFlag(){
	return this.blueFlag;
}
public Location getRedFlag(){
	return this.redFlag;
}
}
