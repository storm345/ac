package com.amazar.utils;

import org.bukkit.Location;

public class ArenaTntori extends ArenaPvp {
    private Boolean protect = true;
	private int countDown = 0;
	private Boolean doCountDown = false;
	public ArenaTntori(Location center, int radius, ArenaShape shape,
			ArenaType type, Location playerRedSpawn, Location playerBlueSpawn,
			int lives, String[] items, Boolean protect, int countDown, Boolean doCountdown) {
		super(center, radius, shape, type, playerRedSpawn, playerBlueSpawn, lives,
				items);
		this.protect = protect;
		this.doCountDown = doCountdown;
		this.countDown = countDown;
	}
public void setProtect(Boolean protect){
	this.protect = protect;
	return;
}
public void setDoCountdown(Boolean doCountdown){
	this.doCountDown = doCountdown;
	return;
}
public void setCountdown(int startPoint){
	this.countDown = startPoint;
	return;
}
public Boolean isProtected(){
	return this.protect;
}
public Boolean getDoCountdown(){
	return this.doCountDown;
}
public Integer getCountdownStartPoint(){
	return this.countDown;
}



}
