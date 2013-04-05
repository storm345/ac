package com.amazar.utils;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MinigameUpdateEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private Minigame game = null;
	public MinigameUpdateEvent(Minigame minigame){
		this.game = minigame;
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
