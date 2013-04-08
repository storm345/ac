package com.amazar.utils;

import java.io.Serializable;

import org.bukkit.Location;

import com.amazar.plugin.ac;

public class Lobbies implements Serializable {
	private static final long serialVersionUID = 8650311534439769069L;
	private SerializableLocation ctf;
	private SerializableLocation push;
	private SerializableLocation pvp;
	private SerializableLocation survival;
	private SerializableLocation teams;
	private SerializableLocation tntori;
	private SerializableLocation ucars;
	public Lobbies(){
		
	}
	public void setLobby(ArenaType type, Location loc){
		SerializableLocation newLoc = new SerializableLocation(loc);
		if(type == ArenaType.CTF){
			this.ctf = newLoc;
		}
		else if(type == ArenaType.PUSH){
			this.push = newLoc;
		}
		else if(type == ArenaType.PVP){
			this.pvp = newLoc;
		}
		else if(type == ArenaType.SURVIVAL){
			this.survival = newLoc;
		}
		else if(type == ArenaType.TEAMS){
			this.teams = newLoc;
		}
		else if(type == ArenaType.TNTORI){
			this.tntori = newLoc;
		}
		else if(type == ArenaType.UCARS){
			this.ucars = newLoc;
		}
		return;
	}
	public Location getLobby(ArenaType type){
		SerializableLocation loc = null;
		if(type == ArenaType.CTF){
			loc = this.ctf;
		}
		else if(type == ArenaType.PUSH){
			loc = this.push;
		}
		else if(type == ArenaType.PVP){
			loc = this.pvp;
		}
		else if(type == ArenaType.SURVIVAL){
			loc = this.survival;
		}
		else if(type == ArenaType.TEAMS){
			loc = this.teams;
		}
		else if(type == ArenaType.TNTORI){
		    loc =this.tntori;
		}
		else if(type == ArenaType.UCARS){
			loc =this.ucars;
		}
		if(loc == null){
			return null;
		}
		return loc.getLocation(ac.plugin.getServer());
	}
	

}
