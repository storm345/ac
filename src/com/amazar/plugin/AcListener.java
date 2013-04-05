package com.amazar.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.TravelAgent;
import org.bukkit.block.Block;
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
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.util.Vector;

import com.amazar.utils.Arena;
import com.amazar.utils.ArenaTntori;
import com.amazar.utils.ArenaType;
import com.amazar.utils.ItemStackFromId;
import com.amazar.utils.Minigame;
import com.amazar.utils.MinigameFinishEvent;
import com.amazar.utils.MinigameStartEvent;
import com.amazar.utils.MinigameUpdateEvent;
import com.amazar.utils.Profile;
import com.amazar.utils.StringColors;

public class AcListener implements Listener {
	
	private ac plugin = null;
	
	public AcListener(ac instance){
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
		List<String> inplayers = game.getInPlayers();
		String in = "";
		for(String inp:inplayers){
			in = in+" "+inp; 
		}
		for(String playername:players){
				Player player = plugin.getServer().getPlayer(playername);
				player.setCustomName(ChatColor.stripColor(player.getCustomName()));
				player.setCustomNameVisible(false);
				player.teleport(player.getLocation().getWorld().getSpawnLocation());
				if(player.isOnline()){
					if(!inplayers.contains(playername)){
						Profile pProfile = new Profile(playername);
						pProfile.addRewardPoint(3);
						player.sendMessage(ChatColor.RED+"+3 reward points!");
					}
					else{
						Profile pProfile = new Profile(playername);
						pProfile.addRewardPoint(10);
						player.sendMessage(ChatColor.RED+"+10 reward points!");
					}
					player.sendMessage(ChatColor.GOLD+game.getWinner() + " won the game!");
					player.sendMessage(ChatColor.GREEN+"Winner(s): "+in);
				}
		}
		plugin.gameScheduler.reCalculateQues();
		return;
	}
	@EventHandler void tntoriProtect(EntityExplodeEvent event){
		List<Block> toDamage = new ArrayList<Block>();
		toDamage.addAll(event.blockList());
		for(Block bl:toDamage){
			if(plugin.mgMethods.isArena(bl.getLocation()) == null){
				return;
			}
			Arena arena = plugin.minigamesArenas.getArena(plugin.mgMethods.isArena(bl.getLocation()));
			if(arena.getType() == ArenaType.TNTORI){
				ArenaTntori arenaGame = (ArenaTntori) arena;
				if(arenaGame.isProtected()){
					event.blockList().remove(bl);
				}
			}
		}
		return;
	}
	@EventHandler void mgDie(PlayerDeathEvent event){
		if(plugin.mgMethods.inAGame(((Player)event.getEntity()).getName()) == null){
			return;
		}
		Player player = event.getEntity();
		Minigame game = plugin.mgMethods.inAGame(player.getName());
		if(game.getGameType() == ArenaType.TNTORI){
			ArenaTntori gameArena = (ArenaTntori) game.getArena();
			player.setBedSpawnLocation(gameArena.getCenter(), false);
			return;
		}
		
		
		return;
	}
	@EventHandler (priority = EventPriority.HIGHEST)
	void miniGameStart(MinigameStartEvent event){
		Minigame game = event.getGame();
		List<String> blue = game.getBlue();
		List<String> red = game.getRed();
		List<String> players = game.getPlayers();
		for(String name:blue){
			Player p = plugin.getServer().getPlayer(name);
			p.setCustomName(ChatColor.BLUE+p.getName());
		}
		for(String name:red){
			Player p = plugin.getServer().getPlayer(name);
			p.setCustomName(ChatColor.RED+p.getName());
		}
		//TODO give items and stuffs
		if(game.getGameType() == ArenaType.TNTORI){
			ArenaTntori gameArena = (ArenaTntori) game.getArena();
			String[] items = gameArena.getItems();
			for(String raw:items){
				ItemStack item = ItemStackFromId.get(raw);
				for(String pname:players){
					plugin.getServer().getPlayer(pname).getInventory().addItem(item);
				}
			}
			for(String pname:players){
				plugin.getServer().getPlayer(pname).getInventory().addItem(new ItemStack(Material.TNT, 64));
				plugin.getServer().getPlayer(pname).getInventory().addItem(new ItemStack(Material.TNT, 64));
			}
		}
		plugin.gameScheduler.reCalculateQues();
		return;
	}
	@EventHandler
	void miniGameHandler(MinigameUpdateEvent event){
		Minigame game = event.getGame();
		if(!game.getRunning()){
			plugin.gameScheduler.stopGame(game.getArena(), game.getArenaName());
			plugin.gameScheduler.reCalculateQues();
			return;
		}
		Arena arena = game.getArena();
		//String arenaName = game.getArenaName();
		ArenaType type = game.getGameType();
		if(type == ArenaType.TNTORI){
			ArenaTntori gameArena = (ArenaTntori) arena;
			List<String> inplayers = game.getInPlayers();
			List<String> players = game.getPlayers();
			List<String> blue = game.getBlue();
			List<String> red = game.getRed();
			ChatColor team_color = ChatColor.WHITE;
			for(String pname:players){
				Player p = plugin.getServer().getPlayer(pname);
				if(!arena.isLocInArena(p.getLocation())){
					int totalLives = gameArena.getLives();
					int lives = totalLives;
					if(game.lives.containsKey(p.getName())){
						lives = game.lives.get(p.getName());
					}
					lives -= 1;
					game.lives.put(pname, lives);
					if(lives < 0){
					//they lose
					if(blue.contains(pname)){
						team_color = ChatColor.BLUE;
						blue.remove(pname);
					}
					if(red.contains(pname)){
						team_color = ChatColor.RED;
						red.remove(pname);
					}
					game.playerOut(pname);
					for(String player:players){
						Player pl = plugin.getServer().getPlayer(player);
						pl.sendMessage(team_color + pname+ " was knocked off and is out!");
					}
					}
					else{
						p.teleport(arena.getCenter());
						for(String player:players){
							Player pl = plugin.getServer().getPlayer(player);
							pl.sendMessage(team_color + pname+ " was knocked off and now has "+lives+"lives left!");
						}
					}
				}
			}
			game.setInPlayers(inplayers);
			plugin.gameScheduler.updateGame(game);
            if(red.size() < 1 && blue.size() < 1){
            	team_color = ChatColor.GOLD;
				for(String player:players){
					Player pl = plugin.getServer().getPlayer(player);
					pl.sendMessage(team_color + "Game end!");
				}
				game.setWinner(team_color+"Nobody");
				game.end();
			}
			if(blue.size() < 1){
				team_color = ChatColor.RED;
				for(String player:players){
					Player pl = plugin.getServer().getPlayer(player);
					pl.sendMessage(team_color + "Game end!");
				}
				game.setWinner(team_color+"Red");
				game.end();
			}
			if(red.size() < 1){
				team_color = ChatColor.BLUE;
				for(String player:players){
					Player pl = plugin.getServer().getPlayer(player);
					pl.sendMessage(team_color + "Game end!");
				}
				game.setWinner(team_color+"Blue");
				game.end();
			}
		}
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
