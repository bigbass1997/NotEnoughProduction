package com.bigbass.nep.recipes;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;

import com.bigbass.nep.recipes.elements.AElement;
import com.bigbass.nep.recipes.elements.Pile;
import com.bigbass.nep.recipes.elements.usual.Fluid;
import com.bigbass.nep.recipes.elements.usual.Item;
import com.bigbass.nep.recipes.processing.Recipe;
import com.bigbass.nep.util.UJSON;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.bigbass.nep.recipes.RecipeDownloader.DownloadResponse.Code;
import com.github.axet.wget.WGet;
import com.twmacinta.util.MD5;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import javax.json.*;

public class RecipeDownloader {
	
	private final String MIRROR_URL = "http://libgdxjam.com/recex/";
	private final String CACHE_PATH = "cache/";
	
	public DownloadResponse downloadRecipeFile(String version){
		version = version.trim();
		
		final String jsonName = version + ".json";
		final String zipName = version + ".zip";
		
		// attempts to download zip version first, if download or checksum fails, simply move on
		DownloadResponse zipRes = downloadFilePair(zipName);
		if(zipRes == DownloadResponse.OK){
			final FileHandle handleZip = Gdx.files.local(CACHE_PATH + zipName);
			final FileHandle jsonDestination = Gdx.files.local(CACHE_PATH);
			
			ZipFile zipFile = new ZipFile(handleZip.file());
			try {
				zipFile.extractFile(jsonName, jsonDestination.file().getPath());

				try {
					final FileHandle jsonUncompressed = Gdx.files.local(CACHE_PATH + "/" + jsonName);
					JsonReader reader = Json.createReader(new FileReader(jsonUncompressed.file().getPath()));
					JsonObject root;

					root = reader.readObject();
					final String versionFolder = CACHE_PATH + version + "/";
					final String hrFolder = versionFolder + "sources/";
					for (JsonValue src : root.getJsonArray("sources")) {
						JsonObject source = src.asJsonObject();

						final String type = source.getJsonString("type").getString();
						final String typeFolder = hrFolder + type + "/";
						final FileHandle typeFolderHandler = Gdx.files.local(typeFolder);
						typeFolderHandler.mkdirs();

						if (type.equalsIgnoreCase("gregtech")) {
							for (JsonValue mch : source.getJsonArray("machines")) {
								JsonObject machine = mch.asJsonObject();
								String name = machine.getString("n").trim();
								JsonArrayBuilder list = Json.createArrayBuilder();
								try {
									for (JsonValue rv : machine.getJsonArray("recs")) {
										JsonObject json = rv.asJsonObject();
										Recipe recipe = new Recipe();
										recipe.duration = json.getInt("dur");
										Map<String, Pile> inputs = new HashMap<>();
										try {
											if (json.containsKey("iI")) {
												for (JsonValue i : json.getJsonArray("iI")) {
													populateRecipeIO(inputs, i, true);
												}
											}
											if (json.containsKey("fI")) {
												for (JsonValue i : json.getJsonArray("fI")) {
													populateRecipeIO(inputs, i, false);
												}
											}
											recipe.inputs.addAll(inputs.values());

											Map<String, Pile> outputs = new HashMap<>();
											if (json.containsKey("iO")) {
												for (JsonValue i : json.getJsonArray("iO")) {
													populateRecipeIO(outputs, i, true);
												}
											}
											if (json.containsKey("fO")) {
												for (JsonValue i : json.getJsonArray("fO")) {
													populateRecipeIO(outputs, i, false);
												}
											}
											recipe.outputs.addAll(outputs.values());
										} catch (Exception e) {
											System.out.println("error reading recipe:");
											System.out.println(name);
											System.out.println(json);
										}

										list.add(recipe.toJson());
									}

									FileWriter writer = new FileWriter(typeFolder + name + ".json");
									writer.write(UJSON.prettyPrint(list.build()));
									writer.close();
								} catch (IOException e) {
									System.out.println(e.toString());
									System.exit(1);
								}
							}
						} else if (!type.equals("shapedOreDict")) {
							JsonArrayBuilder list = Json.createArrayBuilder();
							try {
								for (JsonValue rv : source.getJsonArray("recipes")) {
									JsonObject json = rv.asJsonObject();
									Recipe recipe = new Recipe();
									recipe.duration = 0;
									Map<String, Pile> inputs = new HashMap<>();
									try {
										if (json.containsKey("iI")) {
											for (JsonValue i : json.getJsonArray("iI")) {
												populateRecipeIO(inputs, i, true);
											}
										}
										if (json.containsKey("fI")) {
											for (JsonValue i : json.getJsonArray("fI")) {
												populateRecipeIO(inputs, i, false);
											}
										}
										recipe.inputs.addAll(inputs.values());

										Map<String, Pile> outputs = new HashMap<>();
										if (json.containsKey("iO")) {
											for (JsonValue i : json.getJsonArray("iO")) {
												populateRecipeIO(outputs, i, true);
											}
										}
										if (json.containsKey("o")) {
											populateRecipeIO(outputs, json.getJsonObject("o"), true);
										}
										if (json.containsKey("fO")) {
											for (JsonValue i : json.getJsonArray("fO")) {
												populateRecipeIO(outputs, i, false);
											}
										}
										recipe.outputs.addAll(outputs.values());
									} catch (Exception e) {
										System.out.println("error reading recipe:");
										System.out.println(e);
										System.out.println(type);
										System.out.println(json);
									}

									list.add(recipe.toJson());
								}

								FileWriter writer = new FileWriter(typeFolder + "recipes.json");
								writer.write(UJSON.prettyPrint(list.build()));
								writer.close();
							} catch (IOException e) {
								System.out.println(e.toString());
								System.exit(1);
							}
						}
					}

					JsonArrayBuilder mendeteyBuilder = Json.createArrayBuilder();
					for (AElement element : AElement.mendeley.values()) {
						mendeteyBuilder.add(element.toJson());
					}
					try {
						FileWriter writer = new FileWriter(versionFolder + "mendeley.json");
						writer.write(UJSON.prettyPrint(mendeteyBuilder.build()));
						writer.close();
					} catch (IOException e) {
						System.out.println("error writing items list");
						e.printStackTrace();
					}

				} catch (FileNotFoundException e) {
					return new DownloadResponse(Code.MALFORMED, "Unpacking files to human readable form failed.");
				}

			} catch (ZipException e) {
				e.printStackTrace();
				jsonDestination.delete();
				return new DownloadResponse(Code.MALFORMED, "Extracting json from zip has failed.");
			}
		}

		// if zip download and decompression worked, this call will not download the json file, but will verify checksum
		DownloadResponse jsonRes = downloadFilePair(jsonName);
		return jsonRes;
	}

	private void populateRecipeIO(Map<String, Pile> collection, JsonValue i, boolean isItem) {
		if (i == JsonValue.NULL) {
			return;
		}
		JsonObject json = i.asJsonObject();
		AElement element;
		if (isItem) {
			Map<String, String> nbt = new HashMap<>();
			if (json.containsKey("cfg")) {
				nbt.put("cfg", Integer.toString(json.getInt("cfg")));
			}
			element = new Item(json.getString("uN"), json.getString("lN"), nbt);
		} else {
			element = new Fluid(json.getString("uN"), json.getString("lN"));
		}
		if (AElement.mendeley.containsKey(element.eid())) {
			element = AElement.mendeley.get(element.eid());
		} else {
			AElement.mendeley.put(element.eid(), element);
		}
		if (!collection.containsKey(element.eid())) {
			collection.put(element.eid(), new Pile(json.getInt("a"), element));
		} else {
			collection.get(element.eid()).amount += json.getInt("a");
		}

		
	}

	/**
	 * <p>Downloads the given filename with matching md5 file, and verifies checksum. Written specifically
	 * for this application, thus assumes some information such as directory and remote paths, and the
	 * naming system for md5 files. <b>This method also assumes that if the given filename exists remotely, there
	 * MUST be an md5 to accompany it!</b></p>
	 * 
	 * <p>Method first checks if file exists locally. Then checks if md5 file also exists. If the md5 exists,
	 * a checksum comparison will be performed. If checksum fails, both files are deleted. If either file
	 * doesn't already exist, they will be downloaded and their checksum compared again.</p>
	 * 
	 * <p>If this checksum fails or if the md5 file doesn't exist remotely, a {@link DownloadResponse} will
	 * be returned explaining the error.</p>
	 * 
	 * <p><b>If a download or checksum fails, the related files will be deleted in order to prevent accidental use
	 * of likely corrupted or inaccurate files.</b></p>
	 * 
	 * @param filename filename with extention if applicable
	 * @return
	 */
	private DownloadResponse downloadFilePair(String filename){
		filename = filename.trim();
		final FileHandle filehandle = Gdx.files.local(CACHE_PATH + filename);
		
		final String md5name = filename + ".md5";
		final FileHandle md5handle = Gdx.files.local(CACHE_PATH + md5name);
		
		// check if file exists
		if(filehandle.exists()){
			// check if md5 exists
			if(md5handle.exists()){
				if(compareChecksum(filehandle, md5handle)){
					return DownloadResponse.OK;
				} else {
					// checksum must have failed, either of the files could be corrupt, so deleted both
					filehandle.delete();
					md5handle.delete();
				}
			} else {
				// md5 doesn't exist locally, check remote server for it
				DownloadResponse res = downloadFile(md5name);
				
				if(res == DownloadResponse.OK){
					if(compareChecksum(filehandle, md5handle)){
						return DownloadResponse.OK;
					} else {
						// checksum must have failed, either of the files could be corrupt, so delete both
						filehandle.delete();
						md5handle.delete();
					}
				} else {
					return new DownloadResponse(Code.MISSING, "Either the local and remote md5 file doesn't exist or the download for it failed.");
				}
			}
		}
		
		// if this point is reached, filehandle and md5handle either never existed, or are now deleted
		
		DownloadResponse fileDownloadRes = downloadFile(filename);
		DownloadResponse md5DownloadRes = downloadFile(md5name);
		
		if(fileDownloadRes != DownloadResponse.OK){
			return new DownloadResponse(Code.DOWNLOAD_FAILED, filename + " failed to download.");
		}
		if(md5DownloadRes != DownloadResponse.OK){
			return new DownloadResponse(Code.DOWNLOAD_FAILED, md5name + " failed to download.");
		}
		
		if(compareChecksum(filehandle, md5handle)){
			return DownloadResponse.OK;
		} else {
			// checksum must have failed, either of the files could be corrupt, so delete both
			filehandle.delete();
			md5handle.delete();
			
			return new DownloadResponse(Code.CHECKSUM, "Checksum has failed for " + filename + " and " + md5name);
		}
	}
	
	/**
	 * <p>Downloads the given filename from remote server. This method does NOT run any checksums!</p>
	 * 
	 * <p>If the file already exists locally (to any degree), it will be deleted and rewritten.</p>
	 * 
	 * @param filename filename with extention if applicable
	 * @return
	 */
	private DownloadResponse downloadFile(String filename){
		try {
			System.out.println("Attempting to download: " + filename);
			URL url = new URL(MIRROR_URL + filename);
			File target = Gdx.files.local(CACHE_PATH + filename).file();
			
			WGet w = new WGet(url, target);
			
			w.download(); // blocking! attempts to download file from the url to the target File.
		} catch (MalformedURLException e) {
			return new DownloadResponse(Code.MALFORMED, e.getMessage());
		} catch (RuntimeException e) {
			e.printStackTrace();
			return new DownloadResponse(Code.RUNTIME, e.getMessage());
		}
		
		return DownloadResponse.OK;
	}
	
	private boolean compareChecksum(FileHandle file, FileHandle md5){
		if(!file.exists() || !md5.exists()){
			return false;
		}
		
		try {
			String[] parts = FileUtils.readFileToString(md5.file(), Charset.defaultCharset()).split(" ");
			if(parts.length > 0 && parts[0].length() == 32){
				final byte[] hashFromMD5 = Hex.decodeHex(parts[0]);
				final byte[] hashFromFile = MD5.getHash(file.file());
				
				return MD5.hashesEqual(hashFromMD5, hashFromFile);
			}
		} catch (IOException | DecoderException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static class DownloadResponse {
		
		public static final DownloadResponse OK = new DownloadResponse(Code.OK, "");
		
		public enum Code {
			OK, MALFORMED, RUNTIME, MISSING, DOWNLOAD_FAILED, CHECKSUM;
		}
		
		private final Code code;
		private final String description;
		
		public DownloadResponse(Code code, String description){
			this.code = code;
			this.description = description;
		}
		
		public Code getCode(){
			return code;
		}
		
		public String getDescription(){
			return description;
		}
		
		@Override
		public String toString(){
			return code + ": " + description;
		}
	}
}
