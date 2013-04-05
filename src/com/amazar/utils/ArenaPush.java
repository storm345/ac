package com.amazar.utils;

import org.bukkit.Location;

public class ArenaPush extends ArenaPvp {
	private int countDown = 0;
	private Boolean doCountDown = false;
	public ArenaPush(Location center, int radius, ArenaShape shape,
			ArenaType type, Location playerRedSpawn, Location playerBlueSpawn,
			int lives, String[] items, int countDown, Boolean doCountdown) {
		super(center, radius, shape, type, playerRedSpawn, playerBlueSpawn, lives,
				items);
		this.doCountDown = doCountdown;
		this.countDown = countDown;
		
	}
	public void setDoCountdown(Boolean doCountdown){
		this.doCountDown = doCountdown;
		return;
	}
	public void setCountdown(int startPoint){
		this.countDown = startPoint;
		return;
	}
	public Boolean getDoCountdown(){
		return this.doCountDown;
	}
	public Integer getCountdownStartPoint(){
		return this.countDown;
	}
}
