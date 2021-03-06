package com.atlasplugins.atlastemplate.core;

import java.io.File;
import java.nio.charset.StandardCharsets;

import com.atlasplugins.atlastemplate.apis.Configs;
import com.atlasplugins.atlastemplate.handlers.DependencyHandler;
import com.atlasplugins.atlastemplate.storage.AtlasDatabaseHandler;
import com.dont.licensesystem.plugin.AtlasPlugin;
import com.google.common.io.Resources;

import lombok.Getter;

@Getter
public abstract class AtlasProductionCore extends AtlasPlugin {

	private static @Getter AtlasProductionCore plugin;
	private AtlasDatabaseHandler databaseHandler;
	private DependencyHandler dependencyHandler;
	private boolean useDatabase, useConfig, useMultipleConfigs;

	public AtlasProductionCore(boolean useConfig, boolean useMultipleConfigs) {
		this.useConfig = useConfig;
		this.useMultipleConfigs = useMultipleConfigs;
	}

	@Override
	public void onLoad() {
		this.load();
	}

	@Override
	public void onEnable() {
		if(useConfig) setupConfig();
		if(useMultipleConfigs) Configs.setup();
		if(useDatabase) setupDatabase();
		this.dependencyHandler = new DependencyHandler(this);

		this.enable();
	}

	@Override
	public void onDisable() {
		this.disable();
	}

	private void setupDatabase() {
		this.databaseHandler = new AtlasDatabaseHandler(getConfig());
	}

	public void setupDatabaseConnection() {
		this.useDatabase = true;
		if(!useConfig) {
			setupConfig();
			this.useConfig = true;
		}
		if(!getConfig().isSet("Database")) {
			getConfig().set("Database.Tipo", "SQLLITE");
			getConfig().set("Database.IP", "localhost:3306");
			getConfig().set("Database.DB", "test");
			getConfig().set("Database.User", "root");
			getConfig().set("Database.Pass", "");
			getConfig().set("Database.Debug", true);
			saveConfig();
		}
	}

	public void setupConfig() {
		saveDefaultConfig();
		try {
			File file = new File(getDataFolder() + File.separator, "config.yml");
			String allText = Resources.toString(file.toURI().toURL(), StandardCharsets.UTF_8);
			getConfig().loadFromString(allText);
		} catch (Exception e) {
			e.printStackTrace();
		}
		(plugin = this).saveDefaultConfig();
	}

	public abstract void load();
	public abstract void enable();
	public abstract void disable();

}
