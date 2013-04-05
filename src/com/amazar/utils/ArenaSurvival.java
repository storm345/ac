package com.amazar.utils;

import org.bukkit.Location;

public class ArenaSurvival extends Arena {

	private int countDown = 0;
	private Boolean doCountDown = false;
	private Location playerSpawn = null;
	private Location enemySpawn = null;
	private String[] items = null;
	public ArenaSurvival(Location center, int radius, ArenaShape shape,
			ArenaType type, int countDown, Boolean doCountdown, Location playerSpawn, Location enemySpawn, String[] items) {
		super(center, radius, shape, type);
		this.doCountDown = doCountdown;
		this.countDown = countDown;
		this.playerSpawn = playerSpawn;
		this.enemySpawn = enemySpawn;
		this.items = items;
	}
	public void setDoCountdown(Boolean doCountdown){
		this.doCountDown = doCountdown;
		return;
	}
	public void setCountdown(int startPoint){
		this.countDown = startPoint;
		return;
	}
	public void setPlayerSpawn(Location loc){
		this.playerSpawn = loc;
		return;
	}
	public void setEnemySpawn(Location loc){
		this.enemySpawn = loc;
		return;
	}
	public void setItems(String[] ids){
		return;
	}
	public Boolean getDoCountdown(){
		return this.doCountDown;
	}
	public Integer getCountdownStartPoint(){
		return this.countDown;
	}
	public Location getPlayerSpawnpoint(){
		return this.playerSpawn;
	}
	public Location getEnemySpawnpoint(){
		return this.enemySpawn;
	}
	public String[] getItems(){
		return this.items;
	}
}
