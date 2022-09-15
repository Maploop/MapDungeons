package me.maploop.mapdungeons.util;

import me.maploop.mapdungeons.MapDungeons;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Messages
{
    public static String get(String path, Map<String, Object> placeholders) {
        final String[] msg = {MapDungeons.getPlugin().getMessages().getString(path)};
        placeholders.forEach((k, v) -> msg[0] = msg[0].replace(k, String.valueOf(v)));
        return SUtil.translateColorWords(msg[0]);
    }

    public static String get(String path, Player player) {
        return get(path, Map.ofEntries(
                Map.entry("{player}", player.getName()),
                Map.entry("{player.uuid}", player.getUniqueId())
        ));
    }

    public static String get(String path) {
        return get(path, new HashMap<>());
    }
}
