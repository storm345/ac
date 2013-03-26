package com.amazar.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import com.amazar.plugin.ac;

public class Profile {
	private static ac plugin = ac.plugin;
	private String name = null;
	private File profileFile = null;
	private YamlConfiguration editor = new YamlConfiguration();
	public Profile(String name){
		this.name = name;
		OfflinePlayer[] players = Bukkit.getOfflinePlayers();
		for(int i=0;i<players.length;i++){
			if(players[i].getName().equalsIgnoreCase(name)){
				name = players[i].getName();
			}
		}
		String filename = name+".yml";
		File file = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "profiles" + File.separator + filename);
		if(file.length()<1 || !file.exists()){
			file.getParentFile().mkdirs();
			try {
				file.createNewFile();
			} catch (IOException e) {
			}
		}
		profileFile = file;
		try {
			editor.load(profileFile);
		} catch (Exception e) {
		}
	}
public static File getProfile(String name){
	OfflinePlayer[] players = Bukkit.getOfflinePlayers();
	for(int i=0;i<players.length;i++){
		if(players[i].getName().equalsIgnoreCase(name)){
			name = players[i].getName();
		}
	}
	String filename = name+".yml";
	File file = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "profiles" + File.separator + filename);
	if(file.length()<1 || !file.exists()){
		try {
			file.createNewFile();
		} catch (IOException e) {
		}
	}
	return file;
}
public File getProfile(){
	return this.profileFile;
}
public YamlConfiguration getEditor(){
	return this.editor;
}
public void addWarn(){
	int prev = 0;
	if(editor.contains("profile.warns")){
		prev = editor.getInt("profile.warns");
	}
	prev+=1;
	editor.set("profile.warns", prev);
	this.save();
	this.load();
	return;
}
public void load(){
	try {
		editor.load(profileFile);
	} catch (Exception e) {
	}
}
public int getWarns(){
	this.load();
	int warns = 0;
	if(editor.contains("profile.warns")){
		warns = editor.getInt("profile.warns");
	}
	return warns;
}
public void clearWarns(){
	editor.set("profile.warns", 0);
	this.save();
	this.load();
}
public void save(){
	try {
		editor.save(profileFile);
	} catch (IOException e) {
		try {
			profileFile.createNewFile();
		} catch (IOException e2) {
		}
		try {
			editor.save(profileFile);
		} catch (IOException e1) {
		}
	}
}
public void setOnline(Boolean online){
	editor.set("profile.online", online);
	this.save();
	this.load();
}
public Boolean getOnline(){
	Boolean online = false;
	if(editor.contains("profile.online")){
		online = editor.getBoolean("profile.online");
	}
	return online;
}
public void setOnlineTime(){
	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
	   Date date = new Date();
	   String time = dateFormat.format(date);
	   editor.set("profile.last", time);
	   this.save();
	   this.load();
	return;
}
public String getOnlineTime(){
	String time = "Unknown";
	if(editor.contains("profile.last")){
		time = editor.getString("profile.last");
	}
	return time;
}
public String getClan(){
	String clanName = "Not in a clan";
	if(ac.clanMembers.containsKey(this.name)){
		clanName = StringColors.colorise(ac.clanMembers.get(this.name));
	}
	return clanName;
}
public int getRewardPoints(){
	int points = 0;
	if(editor.contains("profile.rewardPoints")){
		points = editor.getInt("profile.rewardPoints");
	}
	return points;
}
public void addRewardPoint(int amount){
	int current = 0;
	if(editor.contains("profile.rewardPoints")){
		current = editor.getInt("profile.rewardPoints");
	}
	int newOne = current;
	newOne += amount;
	if(current < 1 && newOne <0){
		newOne = 0;
	}
	if(newOne<0){
		newOne = 0;
	}
	editor.set("profile.rewardPoints", newOne);
	this.save();
	this.load();
	return;
}
public void addKill(){
	int current = 0;
	if(editor.contains("profile.kills")){
		current = editor.getInt("profile.kills");
	}
	current += 1;
	editor.set("profile.kills", current);
	this.save();
	this.load();
}
public int getKills(){
	int current = 0;
	if(editor.contains("profile.kills")){
		current = editor.getInt("profile.kills");
	}
	return current;
}
public void unlockPerm(String perm){
	List<String> toSet = new ArrayList<String>();
	toSet.add(perm);
	if(editor.contains("perms.has")){
		List<String> old = editor.getStringList("perms.has");
		for(int i=0;i<old.size();i++){
			String v = old.get(i);
			toSet.add(v);
		}
	}
	editor.set("perms.has", toSet);
	this.save();
	this.load();
	return;
}
public void lockPerm(String perm){
	List<String> toSet = Arrays.asList();
	if(editor.contains("perms.has")){
		List<String> old = editor.getStringList("perms.has");
		for(int i=0;i<old.size();i++){
			String v = old.get(i);
			toSet.add(v);
		}
	}
	if(toSet.contains(perm)){
		toSet.remove(perm);
	}
	editor.set("perms.has", toSet);
	this.save();
	this.load();
	return;
}
}
