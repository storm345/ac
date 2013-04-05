package com.amazar.utils;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MinigameStartEvent  extends Event {
	private static final HandlerList handlers = new HandlerList();
	private Minigame game = null;
	public MinigameStartEvent(Minigame game){
		this.game = game;
	}
	public Minigame getGame(){
		return this.game;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	public HandlerList getHandlerList(){
		return handlers;
	}
	
	

}
