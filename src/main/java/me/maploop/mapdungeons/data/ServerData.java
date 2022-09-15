package me.maploop.mapdungeons.data;

import me.maploop.mapdungeons.MapDungeons;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ServerData
{
    private static final MapDungeons plugin = MapDungeons.getPlugin();

    public static void setLobby(Location loc) {
        plugin.getServerData().set("lobby", loc);
        plugin.getServerData().save();
    }

    public static Location getLobby() {
        return (Location) plugin.getServerData().get("lobby");
    }

    public static Location getSpawnFor(String dungeon) {
        return (Location) plugin.getServerData().get("dungeons." + dungeon + ".spawn");
    }

    public static List<Location> getMobSpawns(String name) {
        if (plugin.getServerData().get("dungeons." + name + ".spawnrs") == null)
            return new ArrayList<>();
        return plugin.getServerData().getList("dungeons." + name + ".spawners").stream().map((o -> (Location) o)).collect(Collectors.toList());
    }

    public static void setMobSpawns(String dun, List<Location> points) {
        plugin.getServerData().set("dungeons." + dun + ".spawners", points);
        plugin.getServerData().save();
    }

    public static void setSpawnFor(String dungeon, Location loc) {
        plugin.getServerData().set("dungeons." + dungeon + ".spawn", loc);
        plugin.getServerData().save();
    }

    public static void createDungeon(String d) {
        plugin.getServerData().set("dungeons." + d + ".name", d);
        plugin.getServerData().save();
    }

    public static Set<String> getDungeons() {
        return plugin.getServerData().getConfigurationSection("dungeons").getKeys(false);
    }
}
