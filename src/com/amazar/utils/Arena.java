package com.amazar.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class Arena {
	private Location center = null;
	private int radius = 0;
	private ArenaShape shape = ArenaShape.SQUARE;
	private ArenaType type = null;
public Arena(Location center, int radius, ArenaShape shape, ArenaType type){
	this.center = center;
	this.radius = radius;
	this.shape = shape;
	this.type = type;
}
public void setCenter(Location loc){
	this.center = loc;
	return;
}
public void setRadius(int radius){
	this.radius = radius;
	return;
}
public void setShape(ArenaShape shape){
	this.shape = shape;
	return;
}
public void setType(ArenaType type){
	this.type = type;
	return;
}
public Location getCenter(){
	return this.center;
}
public Integer getRadius(){
	return this.radius;
}
public ArenaShape getShape(){
	return this.shape;
}
public ArenaType getType(){
	return this.type;
}
public Boolean isLocInArena(Location check){
	if(this.shape == ArenaShape.CIRCLE){
	double dist = check.distance(this.center);
	if(dist <= this.radius){
		return true;
	}
	return false;
	}
	else if(this.shape == ArenaShape.SQUARE){
		double minX = this.center.getX();
		double maxX = this.center.getX()+this.radius;
		double minZ = this.center.getZ();
		double maxZ = this.center.getZ()+this.radius;
		double x = check.getX();
		double z = check.getZ();
		if(x >= minX && x <=maxX && z >= minZ && z <= maxZ){
			return true;
		}
		return false;
	}
	return false;
}
public void markArena(Material marker){
	if(this.shape == ArenaShape.CIRCLE){
		int radius = this.radius;
		int radiusSquared = radius * radius;
		 
		for(int x = -radius; x <= radius; x++) {
		    for(int z = -radius; z <= radius; z++) {
		        if( (x*x) + (z*z) <= radiusSquared) {
		            double y = this.center.getY();
		            double locX = this.center.getX() + x;
		            double locZ = this.center.getZ() + z;
		            World world = this.center.getWorld();
		            Location block = new Location(world,locX,y,locZ);
		            block.getBlock().setType(marker);
		        }
		    }
		}
		this.center.getBlock().setType(Material.GLOWSTONE);
	}
	else if(this.shape == ArenaShape.SQUARE){
		int radius = this.radius;
		for(int x=-radius; x <= radius; x++){
			for(int z=-radius; z <= radius; z++){
				double y = this.center.getY();
	            double locX = this.center.getX() + x;
	            double locZ = this.center.getZ() + z;
	            World world = this.center.getWorld();
	            Location block = new Location(world,locX,y,locZ);
	            block.getBlock().setType(marker);
			}
		}
		this.center.getBlock().setType(Material.GLOWSTONE);
	}
	return;
}
}
