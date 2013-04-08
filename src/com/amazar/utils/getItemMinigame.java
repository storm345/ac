package com.amazar.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.amazar.plugin.ac;

public class getItemMinigame {
public static ItemStack getItem(String minigame, String lore, Material type, short data){
ItemStack result = new ItemStack(type);
if(data != 0){
	result.setDurability(data);
}
ItemMeta meta = result.getItemMeta();
meta.setDisplayName(minigame);
List<String> lores = new ArrayList<String>();
lores.add(lore);
meta.setLore(lores);
result.setItemMeta(meta);
return result;
}
}
