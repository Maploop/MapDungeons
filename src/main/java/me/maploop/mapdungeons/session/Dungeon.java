package me.maploop.mapdungeons.session;

import lombok.Getter;
import lombok.Setter;
import me.maploop.mapdungeons.data.ServerData;
import org.bukkit.Location;

import java.util.List;

@Getter
@Setter
public class Dungeon
{
    private String name;
    private Location spawnPoint;
    private List<Location> mobSpawns;

    public Dungeon(String name) {
        this.name = name;
        this.spawnPoint = ServerData.getSpawnFor(name);
        this.mobSpawns = ServerData.getMobSpawns(name);
    }

    public void addSpawnPoint(Location loc) {
        mobSpawns.add(loc);
    }

    public void save() {
        ServerData.setSpawnFor(name, spawnPoint);
        ServerData.setMobSpawns(name, mobSpawns);
    }
}
