package me.maploop.mapdungeons.data;

import me.maploop.mapdungeons.MapDungeons;
import me.maploop.mapdungeons.session.Dungeon;
import me.maploop.mapdungeons.session.DungeonSpawner;
import me.maploop.mapdungeons.util.BukkitSerialization;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ServerData
{
    private static final MapDungeons plugin = MapDungeons.getPlugin();

    public ServerData() {

    }

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

    public static List<DungeonSpawner> getMobSpawns(String name) {
        if (plugin.getServerData().get("dungeons." + name + ".spawners") == null)
            return new ArrayList<>();
        return plugin.getServerData().getList("dungeons." + name + ".spawners").stream().map((o -> (DungeonSpawner) o)).collect(Collectors.toList());
    }

    public static void setMobSpawns(String dun, List<DungeonSpawner> points) {
        plugin.getServerData().set("dungeons." + dun + ".spawners", points);
    }

    public static void setSpawnFor(String dungeon, Location loc) {
        plugin.getServerData().set("dungeons." + dungeon + ".spawn", loc);
    }

    public static void createDungeon(String d) {
        plugin.getServerData().set("dungeons." + d + ".name", d);
    }

    public static boolean isEnabled(String dungeon) {
        return plugin.getServerData().getBoolean("dungeons." + dungeon + ".enabled", false);
    }

    public static void setDelayBetweenSpawn(String dungeon, long t) {
        plugin.getServerData().set("dungeons." + dungeon + ".spawnDelay", t);
    }

    public static long getDelayBetweenSpawn(String dungeon) {
        return plugin.getServerData().getLong("dungeons." + dungeon + ".spawnDelay", 0);
    }

    public static void setKillsObjective(String dungeon, int t) {
        plugin.getServerData().set("dungeons." + dungeon + ".objectiveKills", t);
    }

    public static int getKillsObjective(String dungeon) {
        return plugin.getServerData().getInt("dungeons." + dungeon + ".objectiveKills", 0);
    }

    public static ItemStack[] getKit(String dungeon) {
        if (plugin.getServerData().contains("dungeons." + dungeon + ".kit")) {
            return plugin.getServerData().getList("dungeons." + dungeon + ".kit").toArray(new ItemStack[0]);
        }
        return new ItemStack[0];
    }

    public static void setKit(String dungeon, ItemStack[] kit) {
        plugin.getServerData().set("dungeons." + dungeon + ".kit", kit);
    }

    public static void setEnabled(String dungeon, boolean enabled) {
        plugin.getServerData().set("dungeons." + dungeon + ".enabled", enabled);
    }

    public static void save() {
        plugin.getServerData().save();
    }

    public static List<String> getActiveDungeons() {
        return plugin.getServerData().getConfigurationSection("dungeons").getKeys(false).stream().filter((e) -> Dungeon.get(e).isEnabled()).collect(Collectors.toList());
    }

    public static Set<String> getDungeons() {
        return plugin.getServerData().getConfigurationSection("dungeons").getKeys(false);
    }

    public static void delete(String section) {
        plugin.getServerData().set("dungeons." + section, null);
    }
}
