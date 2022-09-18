package me.maploop.mapdungeons.util;

import me.maploop.mapdungeons.MapDungeons;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Config extends YamlConfiguration {
    private final File file;

    public Config(File parent, String name) {
        this.file = new File(parent, name);

        if (!file.exists()) {
            options().copyDefaults(true);
            MapDungeons.getPlugin().saveResource(name, false);
        }
        load();
    }

    public Config(String name) {
        this(MapDungeons.getPlugin().getDataFolder(), name);
    }

    public void load() {
        try {
            super.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            super.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
