package com.amazar.utils;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MinigameFinishEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private Minigame game = null;
	public MinigameFinishEvent(Minigame game){
		this.game = game;
	}
	public Minigame getGame(){
		return this.game;
	}
	public HandlerList getHandlers() {
		return handlers;
	}
	public static HandlerList getHandlerList(){
		return handlers;
	}

}
