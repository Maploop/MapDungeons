package me.maploop.mapdungeons.gui.guis;

import me.maploop.mapdungeons.gui.GUI;
import me.maploop.mapdungeons.gui.GUIOpenEvent;
import me.maploop.mapdungeons.gui.guiitem.GUIClickableItem;
import me.maploop.mapdungeons.session.Dungeon;
import me.maploop.mapdungeons.util.Messages;
import me.maploop.mapdungeons.util.SUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ConfigGUI extends GUI
{
    private final Dungeon dungeon;

    public ConfigGUI(Dungeon dungeon) {
        super("Configure " + dungeon.getName(), 36);
        this.dungeon = dungeon;
    }

    @Override
    public void onClose(InventoryCloseEvent e) throws ExecutionException {
        dungeon.save();
    }

    @Override
    public void onOpen(GUIOpenEvent e) {
        Player player = e.getPlayer();
        border(SUtil.getStack(Material.BLACK_STAINED_GLASS_PANE, 0).build());

        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e) {
                if (dungeon.getSpawnPoint() == null) {
                    dungeon.setSpawnPoint(player.getLocation());
                    dungeon.save();
                    player.sendMessage(Messages.get("config.dungeon-spawn-set", Map.ofEntries(Map.entry("{dungeon}", dungeon.getName()), Map.entry("{location}", SUtil.prettify(player.getLocation())))));
                    player.sendMessage("§7Make sure to click save in the configuration menu to save your dungeon's config!");
                    player.closeInventory();
                    return;
                }
                if (dungeon.getSpawnPoint() != null && e.getClick().equals(ClickType.RIGHT)) {
                    dungeon.setSpawnPoint(player.getLocation());
                    dungeon.save();
                    player.sendMessage(Messages.get("config.dungeon-spawn-set", Map.ofEntries(Map.entry("{dungeon}", dungeon.getName()), Map.entry("{location}", SUtil.prettify(player.getLocation())))));
                    player.sendMessage("§7Make sure to click save in the configuration menu to save your dungeon's config!");
                    player.closeInventory();
                } else {
                    player.teleport(dungeon.getSpawnPoint());
                    player.sendMessage("§aTeleported!");
                }
            }

            @Override
            public int getSlot() {
                return 11;
            }

            @Override
            public ItemStack getItem() {
                List<String> lore = new ArrayList<>();

                if (dungeon.getSpawnPoint() != null) {
                    lore.add("&7Current: &f" + SUtil.prettify(dungeon.getSpawnPoint()));
                    lore.add("");
                    lore.add("&eClick to teleport!");
                    lore.add("&bRight-click to set!");
                } else {
                    lore.add("&cNot set");
                    lore.add("");
                    lore.add("&eClick to set!");
                }

                return SUtil.getStack(Material.GOLD_BLOCK, 0).name("&aSpawn point")
                        .glow(dungeon.getSpawnPoint() != null).lore(lore).build();
            }
        });

        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e) {
                dungeon.setEnabled(!dungeon.isEnabled());
                dungeon.save();
                SUtil.delay(() -> new ConfigGUI(dungeon).open(player), 2);
            }

            @Override
            public int getSlot() {
                return 13;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(dungeon.isEnabled() ? Material.LIME_DYE : Material.GRAY_DYE, 0).name("&fEnabled: " + (dungeon.isEnabled() ? "&aYes" : "&cNo"))
                        .lore("&7This option determines if the", "&7dungeon is currently playable by", "&7players or is still being setup",
                                "&7by an administrator.", "", (dungeon.isEnabled() ? "&eClick to disable!" : "&eClick to enable!")).build();
            }
        });

        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e) {
                new SpawnersGUI(dungeon).open(player);
            }

            @Override
            public int getSlot() {
                return 15;
            }

            @Override
            public ItemStack getItem() {
                List<String> lore = new ArrayList<>();

                if (dungeon.getMobSpawns().size() > 0) {
                    for (int i = 0; i < 5; i++) {
                        Location loc = dungeon.getMobSpawns().get(i);
                        lore.add("&8- &f" + SUtil.prettify(loc));
                    }
                    if (dungeon.getMobSpawns().size() > 5)
                        lore.add("&fand " + (dungeon.getMobSpawns().size() - 5) + " more...");
                    lore.add("");
                    lore.add("&eClick to edit!");
                } else {
                    lore.add("&cNone set");
                    lore.add("");
                    lore.add("&eClick to set!");
                }

                return SUtil.getStack(Material.LIGHT_WEIGHTED_PRESSURE_PLATE, 0).name("&dMob Spawners")
                        .glow(dungeon.getMobSpawns().size() > 0).lore(lore).build();
            }
        });
    }
}
