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
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigGUI extends GUI
{
    private final Dungeon dungeon;

    public ConfigGUI(Dungeon dungeon) {
        super("Configure " + dungeon.getName(), 36);
        this.dungeon = dungeon;
    }

    @Override
    public void onOpen(GUIOpenEvent e) {
        Player player = e.getPlayer();
        border(SUtil.getStack(Material.BLACK_STAINED_GLASS_PANE, 0).build());

        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e) {
                dungeon.save();
                player.sendMessage("§aSaved dungeon " + dungeon.getName() + "!");
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
                player.closeInventory();
            }

            @Override
            public int getSlot() {
                return 31;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(Material.FILLED_MAP, 0).glow().name("&a&lSave")
                        .lore("&7Click this to save all of the", "&7changes you have made to",
                                "&f" + dungeon.getName() + "&7 so far!",
                                "",
                                "&eClick to save!").build();
            }
        });

        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e) {
                if (dungeon.getSpawnPoint() == null) {
                    dungeon.setSpawnPoint(player.getLocation());
                    player.sendMessage(Messages.get("config.dungeon-spawn-set", Map.ofEntries(Map.entry("{dungeon}", dungeon.getName()))));
                    player.sendMessage("&7Make sure to click save in the configuration menu to save your dungeon's config!");
                    player.closeInventory();
                    return;
                }
                if (dungeon.getSpawnPoint() != null && e.getClick().equals(ClickType.RIGHT)) {
                    dungeon.setSpawnPoint(player.getLocation());
                    player.sendMessage(Messages.get("config.dungeon-spawn-set", Map.ofEntries(Map.entry("{dungeon}", dungeon.getName()))));
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
                if (dungeon.getSpawnPoint() == null) {
                    dungeon.setSpawnPoint(player.getLocation());
                    player.sendMessage(Messages.get("config.dungeon-spawn-set", Map.ofEntries(Map.entry("{dungeon}", dungeon.getName()))));
                    player.sendMessage("&7Make sure to click save in the configuration menu to save your dungeon's config!");
                    player.closeInventory();
                    return;
                }
                if (dungeon.getSpawnPoint() != null && e.getClick().equals(ClickType.RIGHT)) {
                    dungeon.setSpawnPoint(player.getLocation());
                    player.sendMessage(Messages.get("config.dungeon-spawn-set", Map.ofEntries(Map.entry("{dungeon}", dungeon.getName()))));
                    player.sendMessage("§7Make sure to click save in the configuration menu to save your dungeon's config!");
                    player.closeInventory();
                } else {
                    player.teleport(dungeon.getSpawnPoint());
                    player.sendMessage("§aTeleported!");
                }
            }

            @Override
            public int getSlot() {
                return 15;
            }

            @Override
            public ItemStack getItem() {
                List<String> lore = new ArrayList<>();

                if (dungeon.getMobSpawns().size() > 0) {
                    for (Location loc : dungeon.getMobSpawns()) {
                        lore.add("&8- &f" + SUtil.prettify(loc));
                    }
                    lore.add("&7About " + dungeon.getMobSpawns().size() + " spawners set.");
                    lore.add("");
                    lore.add("&eClick to edit!");
                } else {
                    lore.add("&cNone set");
                    lore.add("");
                    lore.add("&eClick to set!");
                }

                return SUtil.getStack(Material.LIGHT_WEIGHTED_PRESSURE_PLATE, 0).name("&eMob Spawners")
                        .glow(dungeon.getMobSpawns().size() > 0).lore(lore).build();
            }
        });
    }
}
