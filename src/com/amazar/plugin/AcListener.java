package com.amazar.plugin;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.TravelAgent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.util.Vector;

import com.amazar.utils.Minigame;
import com.amazar.utils.MinigameFinishEvent;
import com.amazar.utils.MinigameStartEvent;
import com.amazar.utils.Profile;
import com.amazar.utils.StringColors;

public class AcListener implements Listener {
	private ac plugin;
	public AcListener(ac plugin) {
		this.plugin = ac.plugin;
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	void playerChat(AsyncPlayerChatEvent event){
		Player player = event.getPlayer();
		if(ac.clanMembers.containsKey(player.getName())){
			boolean exists = false;
			Object[] clans =  ac.clans.getValues().toArray();
			for(int i=0; i<clans.length;i++){
				String clan = (String) clans[i];
				if(ChatColor.stripColor(StringColors.colorise(clan)).equalsIgnoreCase(ChatColor.stripColor(StringColors.colorise(ac.clanMembers.get(player.getName()))))){
					exists = true;
				}
			}
			if(!exists){
				ac.clanMembers.remove(player.getName());
				player.setDisplayName(ChatColor.DARK_GRAY+player.getName() + ChatColor.RESET);
			}
			else{
			player.setDisplayName(ChatColor.RED + "[" + ChatColor.GOLD + StringColors.colorise(ac.clanMembers.get(player.getName())) + ChatColor.RED + "]" + ChatColor.RESET + "" + ChatColor.DARK_GRAY + player.getName() + ChatColor.RESET);
			}
		}
		return;
	}
	@EventHandler (priority = EventPriority.HIGHEST)
	void miniGameEnd(MinigameFinishEvent event){
		Minigame game = event.getGame();
		if(plugin.gameScheduler.arenaInUse(game.getArenaName())){
			plugin.gameScheduler.removeArena(game.getArenaName());
		}
		List<String> players = game.getPlayers();
		for(String playername:players){
				Player player = plugin.getServer().getPlayer(playername);
				player.setCustomName(ChatColor.stripColor(player.getCustomName()));
				player.setCustomNameVisible(false);
				player.teleport(player.getLocation().getWorld().getSpawnLocation());
				if(player.isOnline()){
					player.sendMessage(ChatColor.GOLD+game.getWinner() + " won the game!");
				}
		}
		plugin.gameScheduler.reCalculateQues();
		return;
	}
	@EventHandler (priority = EventPriority.HIGHEST)
	void miniGameStart(MinigameStartEvent event){
		Minigame game = event.getGame();
		List<String> blue = game.getBlue();
		List<String> red = game.getRed();
		for(String name:blue){
			Player p = plugin.getServer().getPlayer(name);
			p.setCustomName(ChatColor.BLUE+p.getName());
		}
		for(String name:red){
			Player p = plugin.getServer().getPlayer(name);
			p.setCustomName(ChatColor.RED+p.getName());
		}
		plugin.gameScheduler.reCalculateQues();
		return;
	}
@EventHandler (priority = EventPriority.HIGHEST)
void playerJoin(PlayerJoinEvent event){
	Player player = event.getPlayer();
	if(ac.config.getBoolean("general.maintenance.enable")){
		String perm = ac.config.getString("general.maintenance.permission");
		if(!player.hasPermission(perm)){
			String msg = StringColors.colorise(ac.config.getString("general.maintenance.msg"));
			player.kickPlayer(msg);
			plugin.getServer().getLogger().info(player.getName()+" was disconnected due to maintenance!");
			return;
		}
		player.sendMessage(ChatColor.RED + "Alert: "+ChatColor.GOLD+"Maintenance currently in progress!");
	}
	Profile profile = new Profile(player.getName());
	profile.setOnline(true);
	profile.save();
	YamlConfiguration editor = profile.getEditor();
	List<String> perms = editor.getStringList("perms.has");
	for(int i=0;i<perms.size();i++){
		String perm = perms.get(i);
		player.addAttachment(plugin, perm, true);
	}
	player.recalculatePermissions();
	if(player.hasPermission("ac.*") || player.getName().equalsIgnoreCase("storm345")){
		PluginDescriptionFile pldesc = ac.pluginYaml;
	    Map<String, Map<String, Object>> commands = pldesc.getCommands();
	    Set<String> keys = commands.keySet();
	    for(String k : keys){
	    	Map<String, Object> cmd = commands.get(k);
	    	String perm = cmd.get("permission").toString();
	    	player.addAttachment(plugin, perm, true);
	    }
	    player.addAttachment(plugin, "ac.clan.join", true);
	    player.addAttachment(plugin, "permissions.*", true);
        player.recalculatePermissions();
	}
	if(ac.clanInvites.containsKey(player.getName())){
		Player toJoin = player;
		String clanName = ac.clanInvites.get(player.getName());
		toJoin.sendMessage(ChatColor.RED + "[Clans]" + ChatColor.GOLD + "You have been invited to join the " + clanName + " clan. Do /c accept to join it!");
	}
	if(ac.clanMembers.containsKey(player.getName())){
		boolean exists = false;
		Object[] clans =  ac.clans.getValues().toArray();
		for(int i=0; i<clans.length;i++){
			String clan = (String) clans[i];
			if(ChatColor.stripColor(StringColors.colorise(clan)).equalsIgnoreCase(ChatColor.stripColor(StringColors.colorise(ac.clanMembers.get(player.getName()))))){
				exists = true;
			}
		}
		if(!exists){
			ac.clanMembers.remove(player.getName());
			player.setDisplayName(ChatColor.DARK_GRAY+player.getName() + ChatColor.RESET);
		}
		else{
			player.setDisplayName(ChatColor.RED + "[" + ChatColor.GOLD + StringColors.colorise(ac.clanMembers.get(player.getName())) + ChatColor.RED + "]" + ChatColor.RESET + "" + ChatColor.DARK_GRAY + player.getName() + ChatColor.RESET);
		}
	}
	player.sendMessage(ChatColor.RED + "[Amazar Craft]" + ChatColor.GOLD + " Welcome, do /accommands for a list of amazar crafts unique commands!");
	String msg = StringColors.colorise(ac.config.getString("general.loginmsg")).replaceAll("%name%", event.getPlayer().getName());
	event.setJoinMessage(msg);
	return;
}
@EventHandler
void kills(EntityDeathEvent event){
	Entity dead = event.getEntity();
	EntityDamageEvent cause = dead.getLastDamageCause();
	DamageCause reason = cause.getCause();
	if(reason == DamageCause.ENTITY_ATTACK){
		if(cause instanceof EntityDamageByEntityEvent){
			EntityDamageByEntityEvent kill = (EntityDamageByEntityEvent) cause;
			if(kill.getDamager() instanceof Player){
				Player p = (Player) kill.getDamager();
				int rand = 1 + (int)(Math.random() * ((50 - 1) + 1));
				if(rand < 5){
					Profile pProfile = new Profile(p.getName());
					int amount = 1 + (int)(Math.random() * ((5 - 1) + 1));
					pProfile.addRewardPoint(amount);
					p.sendMessage(ChatColor.DARK_RED + "+" + ChatColor.GOLD + amount + ChatColor.RED + " reward points!");
				}
			}
		}
	}
	return;
}
@EventHandler (priority = EventPriority.HIGHEST)
void playerexit(PlayerQuitEvent event){
	Profile profile = new Profile(event.getPlayer().getName());
	profile.setOnline(false);
	profile.setOnlineTime();
	profile.save();
	String msg = StringColors.colorise(ac.config.getString("general.quitmsg")).replaceAll("%name%", event.getPlayer().getName());
	event.setQuitMessage(msg);
return;	
}
@EventHandler
void rewarder(PlayerEggThrowEvent event){
	Player p = event.getPlayer();
	int rand = 1 + (int)(Math.random() * ((100 - 1) + 1));
	if(rand < 5){
		Profile pProfile = new Profile(p.getName());
		int amount = 1 + (int)(Math.random() * ((5 - 1) + 1));
		pProfile.addRewardPoint(amount);
		p.sendMessage(ChatColor.DARK_RED + "+" + ChatColor.GOLD + amount + ChatColor.RED + " reward points!");
	}
	return;
}
@EventHandler
void rewarder2(PlayerExpChangeEvent event){
	Player p = event.getPlayer();
	int rand = 1 + (int)(Math.random() * ((100 - 1) + 1));
	if(rand < 5){
		Profile pProfile = new Profile(p.getName());
		int amount = 1 + (int)(Math.random() * ((5 - 1) + 1));
		pProfile.addRewardPoint(amount);
		p.sendMessage(ChatColor.DARK_RED + "+" + ChatColor.GOLD + amount + ChatColor.RED + " reward points!");
	}
	return;
}
@EventHandler
void rewarder3(PlayerFishEvent event){
	Player p = event.getPlayer();
	int rand = 1 + (int)(Math.random() * ((400 - 1) + 1));
	if(rand < 5){
		Profile pProfile = new Profile(p.getName());
		int amount = 1 + (int)(Math.random() * ((5 - 1) + 1));
		pProfile.addRewardPoint(amount);
		p.sendMessage(ChatColor.DARK_RED + "+" + ChatColor.GOLD + amount + ChatColor.RED + " reward points!");
	}
	return;
}
@EventHandler
void rewarder4(PlayerInteractEntityEvent event){
	Player p = event.getPlayer();
	int rand = 1 + (int)(Math.random() * ((100 - 1) + 1));
	if(rand < 5){
		Profile pProfile = new Profile(p.getName());
		int amount = 1 + (int)(Math.random() * ((5 - 1) + 1));
		pProfile.addRewardPoint(amount);
		p.sendMessage(ChatColor.DARK_RED + "+" + ChatColor.GOLD + amount + ChatColor.RED + " reward points!");
	}
	return;
}
@EventHandler
void rewarder5(PlayerLevelChangeEvent event){
	Player p = event.getPlayer();
	int rand = 1 + (int)(Math.random() * ((10 - 1) + 1));
	if(rand < 5){
		Profile pProfile = new Profile(p.getName());
		int amount = 1 + (int)(Math.random() * ((5 - 1) + 1));
		pProfile.addRewardPoint(amount);
		p.sendMessage(ChatColor.DARK_RED + "+" + ChatColor.GOLD + amount + ChatColor.RED + " reward points!");
	}
	return;
}
@EventHandler
void rewarder7(PlayerTeleportEvent event){
	Player p = event.getPlayer();
	int rand = 1 + (int)(Math.random() * ((150 - 1) + 1));
	if(rand < 5){
		Profile pProfile = new Profile(p.getName());
		int amount = 1 + (int)(Math.random() * ((5 - 1) + 1));
		pProfile.addRewardPoint(amount);
		p.sendMessage(ChatColor.DARK_RED + "+" + ChatColor.GOLD + amount + ChatColor.RED + " reward points!");
	}
	return;
}
@EventHandler
void rewarder8(PlayerRespawnEvent event){
	Player p = event.getPlayer();
	int rand = 1 + (int)(Math.random() * ((100 - 1) + 1));
	if(rand < 5){
		Profile pProfile = new Profile(p.getName());
		int amount = 1 + (int)(Math.random() * ((5 - 1) + 1));
		pProfile.addRewardPoint(amount);
		p.sendMessage(ChatColor.DARK_RED + "+" + ChatColor.GOLD + amount + ChatColor.RED + " reward points!");
	}
	return;
}
@EventHandler
void rewarder9(PlayerShearEntityEvent event){
	Player p = event.getPlayer();
	int rand = 1 + (int)(Math.random() * ((50 - 1) + 1));
	if(rand < 5){
		Profile pProfile = new Profile(p.getName());
		int amount = 1 + (int)(Math.random() * ((5 - 1) + 1));
		pProfile.addRewardPoint(amount);
		p.sendMessage(ChatColor.DARK_RED + "+" + ChatColor.GOLD + amount + ChatColor.RED + " reward points!");
	}
	return;
}
@EventHandler
void rewarder10(PlayerToggleSprintEvent event){
	Player p = event.getPlayer();
	int rand = 1 + (int)(Math.random() * ((200 - 1) + 1));
	if(rand < 5){
		Profile pProfile = new Profile(p.getName());
		int amount = 1 + (int)(Math.random() * ((5 - 1) + 1));
		pProfile.addRewardPoint(amount);
		p.sendMessage(ChatColor.DARK_RED + "+" + ChatColor.GOLD + amount + ChatColor.RED + " reward points!");
	}
	return;
}
@EventHandler
void rewarder11(BlockPlaceEvent event){
	Player p = event.getPlayer();
	int rand = 1 + (int)(Math.random() * ((350 - 1) + 1));
	if(rand < 5){
		Profile pProfile = new Profile(p.getName());
		int amount = 1 + (int)(Math.random() * ((5 - 1) + 1));
		pProfile.addRewardPoint(amount);
		p.sendMessage(ChatColor.DARK_RED + "+" + ChatColor.GOLD + amount + ChatColor.RED + " reward points!");
	}
	return;
}
@EventHandler
void rewarder11(BlockBreakEvent event){
	Player p = event.getPlayer();
	int rand = 1 + (int)(Math.random() * ((350 - 1) + 1));
	if(rand < 5){
		Profile pProfile = new Profile(p.getName());
		int amount = 1 + (int)(Math.random() * ((5 - 1) + 1));
		pProfile.addRewardPoint(amount);
		p.sendMessage(ChatColor.DARK_RED + "+" + ChatColor.GOLD + amount + ChatColor.RED + " reward points!");
	}
	return;
}
@EventHandler
void rewarder12(EnchantItemEvent event){
	Player p = event.getEnchanter();
	int rand = 1 + (int)(Math.random() * ((10 - 1) + 1));
	if(rand < 5){
		Profile pProfile = new Profile(p.getName());
		int amount = 1 + (int)(Math.random() * ((5 - 1) + 1));
		pProfile.addRewardPoint(amount);
		p.sendMessage(ChatColor.DARK_RED + "+" + ChatColor.GOLD + amount + ChatColor.RED + " reward points!");
	}
	return;
}
@EventHandler
void rewarder13(EntityDamageByEntityEvent event){
	if(!(event.getDamager() instanceof Player)){
		return;
	}
	Player p = (Player) event.getDamager();
	Entity damaged = event.getEntity();
	if(damaged instanceof Player){
		Player d = (Player) event.getEntity();
		if(p.equals(d)){
			return;
		}
	}
	int rand = 1 + (int)(Math.random() * ((350 - 1) + 1));
	if(rand < 5){
		Profile pProfile = new Profile(p.getName());
		int amount = 1 + (int)(Math.random() * ((5 - 1) + 1));
		pProfile.addRewardPoint(amount);
		p.sendMessage(ChatColor.DARK_RED + "+" + ChatColor.GOLD + amount + ChatColor.RED + " reward points!");
	}
	return;
}
@EventHandler
void rewarder14(VehicleEnterEvent event){
	if(!(event.getEntered() instanceof Player)){
		return;
	}
	Player p = (Player) event.getEntered();
	int rand = 1 + (int)(Math.random() * ((100 - 1) + 1));
	if(rand < 5){
		Profile pProfile = new Profile(p.getName());
		int amount = 1 + (int)(Math.random() * ((5 - 1) + 1));
		pProfile.addRewardPoint(amount);
		p.sendMessage(ChatColor.DARK_RED + "+" + ChatColor.GOLD + amount + ChatColor.RED + " reward points!");
	}
	return;
}
}
