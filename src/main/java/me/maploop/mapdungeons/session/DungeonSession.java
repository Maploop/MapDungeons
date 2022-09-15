package me.maploop.mapdungeons.session;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class DungeonSession
{
    private final UUID owner;
    private final String map;
    private final List<Long> killTimestamps;
    private final int kills;

    public DungeonSession(String id, UUID owner, String map, List<Long> killTimestamps, int kills) {
        this.owner = owner;
        this.map = map;
        this.killTimestamps = killTimestamps;
        this.kills = kills;
    }
}
