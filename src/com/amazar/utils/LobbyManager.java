package com.amazar.utils;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.amazar.plugin.ac;

public class LobbyManager {
	File storageFile;
public LobbyManager(File storage){	
	this.storageFile = storage;
	if (this.storageFile.exists() == false){
		try{
			this.storageFile.mkdir();
			this.storageFile.createNewFile();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
public void save(){
	try {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(this.storageFile));
		oos.writeObject(ac.plugin.mgLobbies);
		oos.flush();
		oos.close();
	}catch (Exception e) {
		e.printStackTrace();
	}
}
public void load(){
	try {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(this.storageFile));
		Object result = ois.readObject();
		ois.close();
		ac.plugin.mgLobbies = (Lobbies) result;
	} catch (Exception e) {
		e.printStackTrace();
	}
}
}
