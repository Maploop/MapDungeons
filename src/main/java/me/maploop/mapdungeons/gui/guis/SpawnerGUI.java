package me.maploop.mapdungeons.gui.guis;

import me.maploop.mapdungeons.gui.GUI;
import me.maploop.mapdungeons.gui.GUIOpenEvent;
import me.maploop.mapdungeons.gui.guiitem.GUIChatQueryItem;
import me.maploop.mapdungeons.gui.guiitem.GUIClickableItem;
import me.maploop.mapdungeons.session.Dungeon;
import me.maploop.mapdungeons.session.DungeonSpawner;
import me.maploop.mapdungeons.util.Messages;
import me.maploop.mapdungeons.util.SUtil;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class SpawnerGUI extends GUI
{
    private final Dungeon dungeon;
    private final DungeonSpawner spawner;

    public SpawnerGUI(Dungeon dungeon, DungeonSpawner spawner) {
        super("Spawner", 36);

        border(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        this.dungeon = dungeon;
        this.spawner = spawner;
    }

    @Override
    public void onOpen(GUIOpenEvent e) {
        Player player = e.getPlayer();

        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e) {
                switch (e.getClick()) {
                    case RIGHT: {
                        spawner.setLocation(player.getLocation());
                        dungeon.setSpawner(spawner);
                        dungeon.save();
                        player.sendMessage(Messages.get("config.spawner-set", Map.ofEntries(Map.entry("{dungeon}", dungeon.getName()))));
                        SUtil.delay(() -> new SpawnerGUI(dungeon, spawner).open(player), 3);
                        break;
                    }
                    case LEFT: {
                        player.teleport(spawner.getLocation());
                        player.sendMessage(Messages.get("config.teleport"));
                        break;
                    }
                    case SHIFT_LEFT:
                }
            }

            @Override
            public int getSlot() {
                return 11;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(Material.GOLD_BLOCK, 0).name("§aLocation").lore("&7Current: &f" + SUtil.prettify(e.getPlayer().getLocation()),
                        "", "&eClick to teleport!", "&bRight-click to change!").build();
            }
        });

        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e) {
                SUtil.openConfirmationForm(player, SpawnerGUI.this, "&7Delete the spawner", "&7Cancel and go back",
                        (player) -> {
                            dungeon.deleteSpawner(spawner);
                            dungeon.save();
                            player.sendMessage(Messages.get("config.spawner-deleted", Map.ofEntries(Map.entry("{dungeon}", dungeon.getName()))));
                            SUtil.delay(() -> new SpawnersGUI(dungeon, 1).open(player), 3);
                        });
            }

            @Override
            public int getSlot() {
                return 13;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(Material.BARRIER, 0).name("&cDelete Spawner").lore("&7This will permanently delete this", "&7spawner from your dungeon.", "", "&eClick to delete!").build();
            }
        });

        set(new GUIChatQueryItem()
        {
            @Override
            public GUI onQueryFinish(String query) {
                EntityType type;
                try {
                    type = EntityType.valueOf(query);
                } catch (Exception e) {
                    player.sendMessage(Messages.get("config.invalid-entity"));
                    return new SpawnerGUI(dungeon, spawner);
                }

                spawner.setEntityType(type);
                dungeon.setSpawner(spawner);
                dungeon.save();
                return new SpawnerGUI(dungeon, spawner);
            }

            @Override
            public void run(InventoryClickEvent e) {

            }

            @Override
            public int getSlot() {
                return 15;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(Material.CREEPER_SPAWN_EGG, 0).name("&bEntity Type").lore("&7Set the entity type that", "&7your spawner will spawn!", "",
                        "&7Current: &f" + spawner.getEntityType().name(), "", "&eClick to change!").build();
            }
        });

        set(GUIClickableItem.createGUIOpenerItem(new SpawnersGUI(dungeon, 1), player, "§eBack", 31, Material.ARROW, (short) 0, "§7Go back"));
    }
}
