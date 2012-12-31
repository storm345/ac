package com.amazar.plugin;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class acCommandExecutor implements CommandExecutor {
private Plugin plugin;
public acCommandExecutor(ac plugin) {
	this.plugin = plugin;
}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel,
			String[] args) {
		if(cmd.getName().equalsIgnoreCase("who")){
			//TODO list players online
			String msg = "Online players:" + ChatColor.DARK_RED;
			Player[] players = plugin.getServer().getOnlinePlayers();
			for(int i=0;i<players.length;i++){
				Player p = players[i];
				String name = p.getName();
				msg = msg + " " + name + ",";
			}
			sender.sendMessage(ChatColor.GOLD + msg);
			return true;
		}
		else if(cmd.getName().equalsIgnoreCase("trainme")){
			if(args.length < 1){
				//no args
				return false;
			}
			String skill = args[0];
			if(skill.equalsIgnoreCase("worldguard")){
				//TODO worldguard help
                sender.sendMessage(ChatColor.GRAY + "Type //wand to get the wooden axe cuboid selection tool. You select the corners of the cuboid area" +
                		" you wish to protect. You now do /region define [Region name] [Player],[Player],...    For more info do /help worldguard");//Line 1
				return true;
			}
			else if(skill.equalsIgnoreCase("warns")){
				sender.sendMessage(ChatColor.GRAY + "To warn somebody do /warn [Player] [Reason]. To view all recent server warns do " +
						"/warnslog or /warnslog clear to reset it. To view and individuals warns do /view-warns [Player]. Then to delete their warns do /delete-warns [Name]");
				return true;
			}
			else{
				sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.GRAY + "invalid skill! " +ChatColor.GOLD +"Valid skills are: worldguard, warns");
			}
			return true;
		}
		
		
		
	else if (cmd.getName().equalsIgnoreCase("accommands")){ // If the player typed /setlevel then do the following...
			  PluginDescriptionFile desc = plugin.getServer().getPluginManager().getPlugin("AmazarCraft").getDescription();
			  Map<String, Map<String, Object>> cmds = desc.getCommands();
			  Set<String> keys = cmds.keySet();
			  Object[] commandsavailable = keys.toArray();
			  int displayed = 0;
			  int page = 1;
			  if (args.length < 1){
				  page = 1;
			  }
			  else {
				  try {
					page = Integer.parseInt(args[0]);
				} catch (Exception e) {
					sender.sendMessage(ChatColor.RED + "Given page number is not a number!");
					return true;
				}
			  }
			  int startpoint = (page - 1) * 3;
			  double tot = keys.size() / 3;
			  double total = (double)Math.round(tot * 1) / 1;
			  total += 1;
			  if (page > total || page < 1){
				  sender.sendMessage(ChatColor.RED + "Invalid page number!");
				  return true;
			  }
			  int totalpages = (int) total;
			  sender.sendMessage(ChatColor.DARK_GREEN + "Page: [" + page + "/" + totalpages + "]");
			  for(int i = startpoint; displayed < 3; i++) {
				  String v;
				  try {
					v = commandsavailable[i].toString();
				} catch (Exception e) {
					return true;
				}
				  if(v == null){
					  return true;
				  }
				  Map<String, Object> vmap = cmds.get(v);
				    @SuppressWarnings("unused")
					Set<String> commandInfo = vmap.keySet();
				    String usage = vmap.get("usage").toString();
				    String description = vmap.get("description").toString();
				    String perm = vmap.get("permission").toString();
				    if(sender.hasPermission(perm)){
				    	@SuppressWarnings("unchecked")
						List<String> aliases = (List<String>) vmap.get("aliases");
					    usage = usage.replaceAll("<command>", v);
			        	sender.sendMessage(ChatColor.GOLD + usage + ChatColor.RED + " Description: " + ChatColor.DARK_PURPLE + description);
			        	sender.sendMessage(ChatColor.RED + " Aliases: " + ChatColor.DARK_PURPLE + aliases);
			        	sender.sendMessage(ChatColor.RED + " Permission: " + ChatColor.DARK_PURPLE + perm);	
				    }
				    else{
				    	//nothing
				    }
		        	displayed++;
					}
			  int next = page + 1;
			  if (next < total + 1){
			  sender.sendMessage(ChatColor.DARK_GREEN+ "Do /accommands " + next + " for the next page");
			  }
		 
		     
		      
			  
		
	
	
//TODO
	
		 