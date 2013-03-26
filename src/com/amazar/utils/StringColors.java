package com.amazar.utils;

import org.bukkit.ChatColor;

public class StringColors {
	public static String colorise(String prefix){
		prefix = prefix.replace("&0", "" + ChatColor.BLACK);
		prefix = prefix.replace("&1", "" + ChatColor.DARK_BLUE);
		prefix = prefix.replace("&2", "" + ChatColor.DARK_GREEN);
		prefix = prefix.replace("&3", "" + ChatColor.DARK_AQUA);
		prefix = prefix.replace("&4", "" + ChatColor.DARK_RED);
		prefix = prefix.replace("&5", "" + ChatColor.DARK_PURPLE);
		prefix = prefix.replace("&6", "" + ChatColor.GOLD);
		prefix = prefix.replace("&7", "" + ChatColor.GRAY);
		prefix = prefix.replace("&8", "" + ChatColor.DARK_GRAY);
		prefix = prefix.replace("&9", "" + ChatColor.BLUE);
		prefix = prefix.replace("&a", "" + ChatColor.GREEN);
		prefix = prefix.replace("&b", "" + ChatColor.AQUA);
		prefix = prefix.replace("&c", "" + ChatColor.RED);
		prefix = prefix.replace("&d", "" + ChatColor.LIGHT_PURPLE);
		prefix = prefix.replace("&e", "" + ChatColor.YELLOW);
		prefix = prefix.replace("&f", "" + ChatColor.WHITE);
		prefix = prefix.replace("&r", "" + ChatColor.RESET);
		prefix = prefix.replace("&l", "" + ChatColor.BOLD);
		prefix = prefix.replace("&i", "" + ChatColor.ITALIC);
		prefix = prefix.replace("&m", "" + ChatColor.MAGIC);
		return prefix;
	}
}
