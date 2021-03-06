package com.atlasplugins.atlastemplate.core;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.bukkit.plugin.java.JavaPlugin;

import com.atlasplugins.atlastemplate.apis.Configs;
import com.atlasplugins.atlastemplate.handlers.DependencyHandler;
import com.atlasplugins.atlastemplate.storage.AtlasDatabaseHandler;
import com.google.common.io.Resources;

import lombok.Getter;

@Getter
public abstract class AtlasDevelopmentCore extends JavaPlugin {

	private static @Getter JavaPlugin plugin;
	private AtlasDatabaseHandler databaseHandler;
	private DependencyHandler dependencyHandler;
	private boolean useDatabase, useConfig, useMultipleConfigs;

	public AtlasDevelopmentCore(boolean useConfig, boolean useMultipleConfigs) {
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
