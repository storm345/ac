package com.amazar.utils;

import org.bukkit.Location;

import com.amazar.plugin.ac;

public class ArenaPvp extends Arena {
	private SerializableLocation playerRedSpawn;
	private SerializableLocation playerBlueSpawn;
	private int lives = 1;
	private String[] items = {"0"};

	public ArenaPvp(Location center, int radius, ArenaShape shape,
			ArenaType type, Location playerRedSpawn, Location playerBlueSpawn, int lives, String[] items, int playerLimit) {
		super(center, radius, shape, type, playerLimit);
		this.playerRedSpawn = ac.plugin.invalidLoc;
		this.playerBlueSpawn = ac.plugin.invalidLoc;
		if(playerRedSpawn != null){
		this.playerRedSpawn = new SerializableLocation(playerRedSpawn);
		}
		if(playerBlueSpawn != null){
		this.playerBlueSpawn = new SerializableLocation(playerBlueSpawn);
		}
		if(lives < 1){
			lives = 1;
		}
		this.lives = lives;
		if(items != null){
		this.items = items;
		}
	}
	public Boolean isValid(){
		if(this.playerRedSpawn.equals(ac.plugin.invalidLoc) || this.playerBlueSpawn.equals(ac.plugin.invalidLoc)){
			return false;
		}
		return true;
	}
	public void setPlayerRedSpawn(Location loc){
		this.playerRedSpawn = new SerializableLocation(loc);
		return;
	}
	public void setPlayerBlueSpawn(Location loc){
		this.playerBlueSpawn = new SerializableLocation(loc);
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
		return this.playerRedSpawn.getLocation(ac.plugin.getServer());
	}
	public Location getPlayerBlueSpawn(){
		return this.playerBlueSpawn.getLocation(ac.plugin.getServer());
	}
	public int getLives(){
		return this.lives;
	}
	public String[] getItems(){
		return this.items;
	}

}
