package me.maploop.mapdungeons.listener.events;

import lombok.Getter;
import me.maploop.mapdungeons.session.Dungeon;
import me.maploop.mapdungeons.session.DungeonSession;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class PlayerJoinDungeonEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final DungeonSession session;
    private final Dungeon dungeon;

    public PlayerJoinDungeonEvent(Player player, Dungeon dungeon, DungeonSession session) {
        this.player = player;
        this.session = session;
        this.dungeon = dungeon;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
