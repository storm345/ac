package com.amazar.utils;

import org.bukkit.Location;

public class ArenaPvp extends Arena {
	private Location playerRedSpawn = null;
	private Location playerBlueSpawn = null;
	private int lives = 1;
	private String[] items = null;

	public ArenaPvp(Location center, int radius, ArenaShape shape,
			ArenaType type, Location playerRedSpawn, Location playerBlueSpawn, int lives, String[] items) {
		super(center, radius, shape, type);
		this.playerRedSpawn = playerRedSpawn;
		this.playerBlueSpawn = playerBlueSpawn;
		this.lives = lives;
		this.items = items;
	}
	public void setPlayerRedSpawn(Location loc){
		this.playerRedSpawn = loc;
		return;
	}
	public void setPlayerBlueSpawn(Location loc){
		this.playerBlueSpawn = loc;
		return;
	}
	public void setLives(int lives){
		this.lives = lives;
	}
	public void setItems(String[] ids){
		this.items = ids;
		return;
	}
	public Location getPlayerRedSpawn(){
		return this.playerRedSpawn;
	}
	public Location getPlayerBlueSpawn(){
		return this.playerBlueSpawn;
	}
	public int getLives(){
		return this.lives;
	}
	public String[] getItems(){
		return this.items;
	}

}
