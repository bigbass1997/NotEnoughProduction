package com.bigbass.nep.config;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParsingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Config {
	
	private static JsonWriterFactory writerFactory;
	
	public final String filename;
	public JsonObject data;
	
	public Config(String filename){
		this.filename = filename;
		
		Map<String, Object> properties = new HashMap<>(1);
		properties.put(JsonGenerator.PRETTY_PRINTING, true);
		writerFactory = Json.createWriterFactory(properties);
	}
	
	/**
	 * <p>Attempts to read and parse a JSON file into a JsonObject to be used later.</p>
	 * 
	 * <p>This will return null if file is not found or if the file is corrupt/invalid.
	 * If the data is not null, this Config instance's data member will be updated to reflect this method's output.</p>
	 * 
	 * @return JSON data from file as JsonObject
	 */
	public JsonObject retrieveData(){
		JsonObject data = null;
		
		try {
			JsonReader reader = Json.createReader(new FileInputStream(getFile()));
			data = reader.readObject();
			reader.close();
		} catch (FileNotFoundException e) {
			// Do nothing
		} catch (JsonParsingException e) {
			e.printStackTrace();
		} catch (JsonException e) {
			//e.printStackTrace();
		}
		
		if(data != null){
			this.data = data;
		}
		
		return data;
	}
	
	/**
	 * <p>Saves this instance's data member to the file; therefore saving any changes that were made to the data member since last retrieval.</p>
	 * 
	 * <p>If the data member is null, or any exceptions are thrown, the data will NOT be saved!</p>
	 */
	public void saveData(){
		if(data == null){
			return;
		}
		
		try {
			JsonWriter writer = writerFactory.createWriter(new FileOutputStream(getFile()));
			
			writer.writeObject(data);
			
			writer.close();
			
			System.out.println(filename + " has been saved.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println(filename + " failed to save! This config's data will remain in memory, however any changes since last retrieval have NOT been saved to disk.");
		}
	}
	
	/**
	 * Creates a File object for this config's file path. This does NOT retrieve actual data.
	 * 
	 * @return File object per config's path
	 */
	private File getFile(){
		File configDir = new File(ConfigManager.CONFIG_DIR);
		if(!configDir.exists()){
			configDir.mkdir();
		}
		
		File file = new File(ConfigManager.CONFIG_DIR + filename);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return file;
	}
}
