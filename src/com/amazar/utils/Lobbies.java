package com.amazar.utils;

import java.io.Serializable;

import org.bukkit.Location;

public class Lobbies implements Serializable {
	private static final long serialVersionUID = 8650311534439769069L;
	private SerializableLocation ctf;
	private SerializableLocation push;
	private SerializableLocation pvp;
	private SerializableLocation survival;
	private SerializableLocation teams;
	private SerializableLocation tntori;
	public Lobbies(){
		
	}
	public void setLobby(ArenaType type, Location loc){
		SerializableLocation newLoc = new SerializableLocation(loc);
		if(type == ArenaType.CTF){
			this.ctf = newLoc;
		}
		return;
	}
	public Location getLobby(ArenaType type){
		
	}
	

}
