package com.amazar.utils;

import org.bukkit.Location;

public class ArenaTeams extends Arena {
private Location blueSpawn = null;
private Location redSpawn = null;
	public ArenaTeams(Location center, int radius, ArenaShape shape,
			ArenaType type, Location blueSpawn, Location redSpawn) {
		super(center, radius, shape, type);
		this.blueSpawn = blueSpawn;
		this.redSpawn = redSpawn;
	}
	public void setBlueSpawn(Location loc){
		this.blueSpawn = loc;
		return;
	}
	public void setRedSpawn(Location loc){
		this.redSpawn = loc;
		return;
	}
	public Location getRedSpawnpoint(){
		return this.redSpawn;
	}
	public Location getBlueSpawnpoint(){
		return this.blueSpawn;
	}
}
