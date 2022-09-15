package me.maploop.mapdungeons.session;

import lombok.Getter;

import java.util.*;

@Getter
public class DungeonSession
{
    public static final Map<UUID, DungeonSession> ACTIVE_SESSIONS = new HashMap<>();

    private final String id;
    private final UUID owner;
    private final String map;
    private final List<Long> killTimestamps;
    private final int kills;

    public DungeonSession(String id, UUID owner, String map, List<Long> killTimestamps, int kills) {
        this.id = id;
        this.owner = owner;
        this.map = map;
        this.killTimestamps = killTimestamps;
        this.kills = kills;
    }

    public String getSerializedKillTimestamps() {
        StringBuilder b = new StringBuilder();
        killTimestamps.forEach(s -> b.append(s).append(";"));
        return b.toString();
    }

    public static DungeonSession New(String map, UUID owner) {
        String id = UUID.randomUUID().toString().substring(0, 8);
        return new DungeonSession(id, owner, map, new ArrayList<>(), 0);
    }

    public void start() {
        ACTIVE_SESSIONS.put(owner, this);
    }
}
