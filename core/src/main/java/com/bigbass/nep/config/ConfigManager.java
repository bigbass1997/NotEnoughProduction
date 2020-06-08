package com.bigbass.nep.config;

import javax.json.Json;
import java.util.Hashtable;

public class ConfigManager {
	
	public static final String CONFIG_DIR = "./config/";
	
	private Hashtable<String, Config> configs;
	
	private ConfigManager(){
		configs = new Hashtable<String, Config>();
		
		// Register core configs
		registerConfig("general", "general.json");
		
		// Check if core configs exist, if not, create them with default settings
		Config general = getConfig("general");
		if(general.retrieveData() == null){
			general.data = Json.createObjectBuilder()
					.add("atlas_version", "v2.0.9.0QF2-x0.0.3")
					.build();
			general.saveData();
		}
	}
	
	private static class LazyHolder {
		private static final ConfigManager INSTANCE = new ConfigManager();
	}
	
	public static ConfigManager getInstance(){
		return LazyHolder.INSTANCE;
	}
	
	public boolean registerConfig(String id, String filename){
		if(id == null || filename == null || configs.containsKey(id)){ // Prevents accidentally rewriting already loaded configs
			return false;
		}
		
		configs.put(id, new Config(filename));
		return true;
	}
	
	public Config getConfig(String id){
		if(id == null){
			return null;
		}
		
		return configs.get(id);
	}
	
	public void saveAll(){
		for(Config config : configs.values()){
			config.saveData();
		}
	}
}
