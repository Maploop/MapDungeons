package me.maploop.mapdungeons.session;

import lombok.Getter;
import lombok.Setter;
import me.maploop.mapdungeons.data.ServerData;
import me.maploop.mapdungeons.util.SUtil;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Dungeon
{
    public static final Map<String, Dungeon> CACHED_DUNGEONS = new HashMap<>();

    private String name;
    private Location spawnPoint;
    private List<Location> mobSpawns;
    private boolean enabled;

    public Dungeon(String name) {
        this.name = name;
        this.spawnPoint = ServerData.getSpawnFor(name);
        this.mobSpawns = ServerData.getMobSpawns(name);
        this.enabled = ServerData.isEnabled(name);
        CACHED_DUNGEONS.put(name, this);
    }

    public static Dungeon get(String name) {
        if (CACHED_DUNGEONS.containsKey(name))
            return CACHED_DUNGEONS.get(name);
        return new Dungeon(name);
    }

    public void addSpawnPoint(Location loc) {
        mobSpawns.add(loc);
    }

    public void save() {
        SUtil.runAsync(() -> {
            ServerData.setSpawnFor(name, spawnPoint);
            ServerData.setMobSpawns(name, mobSpawns);
            ServerData.setEnabled(name, enabled);
            ServerData.save();
        });
    }
}
