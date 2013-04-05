package com.amazar.plugin;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class ac extends JavaPlugin {
	public Plugin bukkit;
public void onEnable(){
	bukkit = this;
	getCommand("who").setExecutor(new acCommandExecutor(this));
	getCommand("accommands").setExecutor(new acCommandExecutor(this));
	getCommand("trainme").setExecutor(new acCommandExecutor(this));
	getCommand("news").setExecutor(new acCommandExecutor(this));
getLogger().info("AmazarCraft plugin is enabled");	//Tell teh console it is enabled
}

public void onDisable(){
	getLogger().info("AmazarCraft plugin is disabled");
}
}
