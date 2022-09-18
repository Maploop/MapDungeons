package me.maploop.mapdungeons.session;

import lombok.Getter;
import lombok.Setter;
import me.maploop.mapdungeons.MapDungeons;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class DungeonSpawner implements ConfigurationSerializable
{
    private Location location;
    private EntityType entityType;
    private final String id;

    public DungeonSpawner(Location location, EntityType entityType) {
        this.id = UUID.randomUUID().toString();
        this.location = location;
        this.entityType = entityType;
    }

    @Override
    public Map<String, Object> serialize() {
        return Map.ofEntries(Map.entry("location", location), Map.entry("entity", entityType.name()));
    }

    public static DungeonSpawner deserialize(Map<String, Object> map) {
        return new DungeonSpawner((Location) map.get("location"), EntityType.valueOf(map.get("entity").toString()));
    }

    public void execute(DungeonSession owner) {
        Entity entity = location.getWorld().spawnEntity(location, entityType);
        entity.getPersistentDataContainer().set(new NamespacedKey(MapDungeons.getPlugin(), "specMob"), PersistentDataType.STRING, owner.getOwner().toString());
        owner.getEntities().add(entity);
        DungeonSession.OVERALL_ENTITIES.add(entity);
    }
}
