package me.maploop.mapdungeons.session;

import lombok.Getter;
import lombok.Setter;
import me.maploop.mapdungeons.data.ServerData;
import me.maploop.mapdungeons.util.SUtil;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

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
    private List<DungeonSpawner> mobSpawns;
    private boolean enabled;
    private long delayBetweenSpawners;
    private int killsObjective;
    private ItemStack[] kit;

    public Dungeon(String name) {
        this.name = name;
        this.spawnPoint = ServerData.getSpawnFor(name);
        this.mobSpawns = ServerData.getMobSpawns(name);
        this.enabled = ServerData.isEnabled(name);
        this.delayBetweenSpawners = ServerData.getDelayBetweenSpawn(name);
        this.killsObjective = ServerData.getKillsObjective(name);
        this.kit = ServerData.getKit(name);
        CACHED_DUNGEONS.put(name, this);
    }

    public static Dungeon get(String name) {
        if (CACHED_DUNGEONS.containsKey(name))
            return CACHED_DUNGEONS.get(name);
        return new Dungeon(name);
    }

    public void setSpawner(DungeonSpawner spawner) {
        for (int i = 0; i < mobSpawns.size(); i++) {
            if (mobSpawns.get(i).getId().equals(spawner.getId())) {
                mobSpawns.set(i, spawner);
                break;
            }
        }
    }

    public void deleteSpawner(DungeonSpawner spawner) {
        mobSpawns.remove(spawner);
    }

    public void addSpawnPoint(DungeonSpawner loc) {
        mobSpawns.add(loc);
    }

    public void save() {
        SUtil.runAsync(() -> {
            ServerData.setSpawnFor(name, spawnPoint);
            ServerData.setMobSpawns(name, mobSpawns);
            ServerData.setEnabled(name, enabled);
            ServerData.setDelayBetweenSpawn(name, delayBetweenSpawners);
            ServerData.setKillsObjective(name, killsObjective);
            ServerData.setKit(name, kit);
            ServerData.save();
        });
    }
}
