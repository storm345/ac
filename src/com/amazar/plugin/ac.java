package com.amazar.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

import com.amazar.utils.Arena;
import com.amazar.utils.Arenas;
import com.amazar.utils.GameScheduler;
import com.amazar.utils.ListStore;
import com.amazar.utils.Lobbies;
import com.amazar.utils.LobbyManager;
import com.amazar.utils.MinigameMethods;
import com.amazar.utils.SerializableLocation;

public class ac extends JavaPlugin {
	public SerializableLocation invalidLoc;
	public HashMap<String, Arena> arenas = new HashMap<String, Arena>();
	//Main class
	private void copy(InputStream in, File file) {
	    try {
	        OutputStream out = new FileOutputStream(file);
	        byte[] buf = new byte[1024];
	        int len;
	        while((len=in.read(buf))>0){
	            out.write(buf,0,len);
	        }
	        out.close();
	        in.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	@SuppressWarnings("unchecked")
	public static HashMap<String, String> loadHashMapString(String path)
	{
		try
		{
			System.out.println("Loading information!");
	        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
	        Object result = ois.readObject();
	        ois.close();
			return (HashMap<String, String>) result;
		}
		catch(Exception e)
		{
			System.out.println("Information failed to load error:");
			e.printStackTrace();
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	public static HashMap<String, Arena> loadHashMapArena(String path)
	{
		try
		{
			System.out.println("Loading information!");
	        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
	        Object result = ois.readObject();
	        ois.close();
			return (HashMap<String, Arena>) result;
		}
		catch(Exception e)
		{
			System.out.println("Information failed to load error:");
			e.printStackTrace();
			return null;
		}
	}
	public static void saveHashMap(HashMap<String, String> map, String path)
	{
		try
		{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
			oos.writeObject(map);
			oos.flush();
			oos.close();
			//Handle I/O exceptions
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void saveHashMapArena(HashMap<String, Arena> map, String path)
	{
		try
		{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
			oos.writeObject(map);
			oos.flush();
			oos.close();
			//Handle I/O exceptions
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static Plugin bukkit;
	public static ac plugin;
	public static ListStore news;
	public static ListStore packages;
	public static ListStore spends;
	public static ListStore vote;
	public static ListStore clans;
	public static ListStore warns;
	public static ListStore warnsplayer;
	public static HashMap<String, String> clanMembers = new HashMap<String, String>();
	public static HashMap<String, String> clanInvites = new HashMap<String, String>();
	public static FileConfiguration config;
	public static PluginDescriptionFile pluginYaml;
	public static Economy econ = null;
    public static Permission perms = null;
    public Arenas minigamesArenas = null;
    public GameScheduler gameScheduler = null;
    public MinigameMethods mgMethods = null;
    public Lobbies mgLobbies = null;
    public LobbyManager mgLobbyManager = null;
    public Boolean isUcarsInstalled = false;
    public com.useful.ucars.ucars ucars = null;
public void onEnable(){
	//Now on github!
	plugin = this;
	bukkit = this;
	getServer().getPluginManager().registerEvents(new AcListener(this), this); //TODO this shud be working??
	pluginYaml = plugin.getDescription();
	//START HERE
	invalidLoc = new SerializableLocation(new Location(getServer().getWorlds().get(0), 1, 1, 1));
	PluginDescriptionFile pldesc = plugin.getDescription();
    Map<String, Map<String, Object>> commands = pldesc.getCommands();
    Set<String> keys = commands.keySet();
    for(String k : keys){
    	try {
			getCommand(k).setExecutor(new acCommandExecutor(this));
		} catch (Exception e) {
			getLogger().log(Level.SEVERE, "Error registering command " + k.toString());
			e.printStackTrace();
		}
    }
    //END HERE
    String pluginFolder = this.getDataFolder().getAbsolutePath();
	(new File(pluginFolder)).mkdirs();
    File newsFile = new File(this.getDataFolder().getAbsolutePath() + File.separator + "news.txt");
    newsFile.getParentFile().mkdirs();
    //newsFile.mkdirs();
    try {
		newsFile.createNewFile();
	} catch (IOException e) {
	}
    news = new ListStore(newsFile);
	news.load();
	File packageFile = new File(this.getDataFolder().getAbsolutePath() + File.separator + "packages.txt");
    packageFile.getParentFile().mkdirs();
    //newsFile.mkdirs();
    if(packageFile.exists() == false || packageFile.length() < 1){
    	try {
    		packageFile.createNewFile();
    	} catch (IOException e) {
    	}
    	copy(getResource("packages.txt"), packageFile);
    }
    try {
		packageFile.createNewFile();
	} catch (IOException e) {
	}
    packages = new ListStore(packageFile);
	packages.load();
	File spendsFile = new File(this.getDataFolder().getAbsolutePath() + File.separator + "spends.txt");
    spendsFile.getParentFile().mkdirs();
    //newsFile.mkdirs();
    if(spendsFile.exists() == false || spendsFile.length() < 1){
    	try {
    		packageFile.createNewFile();
    	} catch (IOException e) {
    	}
    	copy(getResource("spends.txt"), spendsFile);
    }
    try {
		spendsFile.createNewFile();
	} catch (IOException e) {
	}
    spends = new ListStore(spendsFile);
	spends.load();
	minigamesArenas = new Arenas(this);
	config = getConfig();
	try{
		config.load(this.getDataFolder().getAbsolutePath() + File.separator + "config.yml");
		if(!config.contains("general.loginmsg")){
			config.set("general.loginmsg", "&6%name% &2joined the game");
		}
		if(!config.contains("general.quitmsg")){
			config.set("general.quitmsg", "&6%name% &4left the game");
		}
		if(!config.contains("general.maintenance.enable")){
			config.set("general.maintenance.enable", false);
		}
		if(!config.contains("general.maintenance.msg")){
			config.set("general.maintenance.msg", "&6Maintenance in progress!");
		}
		if(!config.contains("general.maintenance.permission")){
			config.set("general.maintenance.permission", "ac.worker");
		}
	}
	catch (Exception e){
		//error
	}
	saveConfig();
	this.gameScheduler = new GameScheduler();
	this.mgMethods = new MinigameMethods();
	File voteFile = new File(this.getDataFolder().getAbsolutePath() + File.separator + "voteInfo.txt");
    voteFile.getParentFile().mkdirs();
    //newsFile.mkdirs();
    try {
		voteFile.createNewFile();
	} catch (IOException e) {
	}
    vote = new ListStore(voteFile);
	vote.load();
	File clansFile = new File(this.getDataFolder().getAbsolutePath() + File.separator + "clans.txt");
    clansFile.getParentFile().mkdirs();
    //newsFile.mkdirs();
    try {
		clansFile.createNewFile();
	} catch (IOException e) {
	}
    clans = new ListStore(clansFile);
	clans.load();
	File clansMembersFile = new File(this.getDataFolder().getAbsolutePath() + File.separator + "clansMembers.bin");
    clansMembersFile.getParentFile().mkdirs();
    //newsFile.mkdirs();
    try {
		clansMembersFile.createNewFile();
	} catch (IOException e) {
	}
    String path355 = this.getDataFolder().getAbsolutePath() + File.separator + "clansMembers.bin";
	File file355 = new File(path355);
	if(file355.exists() && file355.length() > 1){ // check if file exists before loading to avoid errors!
		clanMembers = loadHashMapString(path355);
	}
	else{
		getLogger().info("Created a new clansMembers.bin!");
		clanMembers = new HashMap<String, String>();
		saveHashMap(clanMembers, this.getDataFolder().getAbsolutePath() + File.separator + "clansMembers.bin");
	}
	String pathArenas = this.getDataFolder().getAbsolutePath() + File.separator + "arenas.bin";
	File fileArenas = new File(pathArenas);
	if(fileArenas.exists() && fileArenas.length() > 1){ // check if file exists before loading to avoid errors!
		arenas = loadHashMapArena(pathArenas);
	}
	else{
		getLogger().info("Created a new arenas.bin!");
		arenas = new HashMap<String, Arena>();
		saveHashMapArena(arenas, this.getDataFolder().getAbsolutePath() + File.separator + "arenas.bin");
	}
	File clansInvitesFile = new File(this.getDataFolder().getAbsolutePath() + File.separator + "cinvites.bin");
    clansInvitesFile.getParentFile().mkdirs();
    //newsFile.mkdirs();
    try {
		clansInvitesFile.createNewFile();
	} catch (IOException e) {
	}
    String path3555 = this.getDataFolder().getAbsolutePath() + File.separator + "cinvites.bin";
	File file3555 = new File(path3555);
	if(file3555.exists() && file3555.length() > 1){ // check if file exists before loading to avoid errors!
		clanInvites = loadHashMapString(path3555);
	}
	else{
		getLogger().info("Created a new cinvites.bin!");
		clanInvites = new HashMap<String, String>();
		saveHashMap(clanInvites, this.getDataFolder().getAbsolutePath() + File.separator + "cinvites.bin");
	}
	pluginFolder = this.getDataFolder().getAbsolutePath();
	(new File(pluginFolder)).mkdirs();
	(new File(pluginFolder + File.separator + "warns")).mkdirs();
	File warnsLogFile = new File(pluginFolder + File.separator + "warns.log");
	if(!(warnsLogFile.exists()) || warnsLogFile.length() < 1){
		//doesnt exist
		try {
			warnsLogFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	warns = new ListStore(warnsLogFile);
	warns.load();
	this.mgLobbies = new Lobbies();
	File lobbyFile = new File(getDataFolder().getAbsolutePath()+File.separator+"mgLobbies.lobbylist");
	if(!lobbyFile.exists() || lobbyFile.length() < 1){
		try {
			lobbyFile.createNewFile();
		} catch (Exception e) {
			getLogger().info("Failed to create lobbyList file!");
		}
	}
	this.mgLobbyManager = new LobbyManager(lobbyFile);
	this.mgLobbyManager.load();
	Set<Team> teams = plugin.getServer().getScoreboardManager().getMainScoreboard().getTeams();
	for(Team team:teams){
		if(team.getName().startsWith("red") || team.getName().startsWith("blue")){
			team.unregister();
		}
	}
	if (!setupEconomy() ) {
        getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
        getServer().getPluginManager().disablePlugin(this);
        return;
    }
    setupPermissions();
    if(getServer().getPluginManager().getPlugin("uCars") == null){
    	getLogger().log(Level.SEVERE, "UCars NOT DETECTED! UCARS MINIGAME UNABLE TO FUNCTION! AAAAAAAH IM FEELING ERRRORS RIGHT NOW.");
    	this.isUcarsInstalled = false;
    }
    else{
    	this.isUcarsInstalled = true;
    	this.ucars = (com.useful.ucars.ucars) getServer().getPluginManager().getPlugin("uCars");
    	getLogger().info("Successfully hooked into ucars!");
    }
	getLogger().info("AmazarCraft plugin is enabled :)");	//Tell teh console it is enabled
}
private boolean setupEconomy() {
    if (getServer().getPluginManager().getPlugin("Vault") == null) {
    	getLogger().info("No vault");
        return false;
    }
    RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
    if (rsp == null) {
        return false;
    }
    econ = rsp.getProvider();
    return econ != null;
}

private boolean setupPermissions() {
    RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
    perms = rsp.getProvider();
    return perms != null;
}
public void onDisable(){
	this.mgLobbyManager.save();
	Set<Team> teams = plugin.getServer().getScoreboardManager().getMainScoreboard().getTeams();
	for(Team team:teams){
		if(team.getName().startsWith("red") || team.getName().startsWith("blue")){
			team.unregister();
		}
	}
	getLogger().info("AmazarCraft plugin is disabled");
}
}
