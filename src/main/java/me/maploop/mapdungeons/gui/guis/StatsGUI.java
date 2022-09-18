package me.maploop.mapdungeons.gui.guis;

import me.maploop.mapdungeons.MapDungeons;
import me.maploop.mapdungeons.gui.GUI;
import me.maploop.mapdungeons.gui.GUIOpenEvent;
import me.maploop.mapdungeons.gui.guiitem.GUIClickableItem;
import me.maploop.mapdungeons.gui.guiitem.GUIItem;
import me.maploop.mapdungeons.util.SUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class StatsGUI extends GUI
{
    private final UUID uuid;

    public StatsGUI(String user) {
        super("Stats for " + user, 36);

        UUID uuid = MapDungeons.plugin.sql.getUUID(user);
        this.uuid = uuid;

        border(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e) {
                new SessionsGUI(user, uuid, 1).open((Player) e.getWhoClicked());
            }

            @Override
            public int getSlot() {
                return 22;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(Material.FILLED_MAP, 0).name("&aView Sessions").lore("&7View all the sessions that", "&f" + user + " &7has played in!", "", "&eClick to view!").build();
            }
        });
    }

    @Override
    public void afterOpenAsync(GUIOpenEvent e) {
        set(new GUIItem()
        {
            @Override
            public int getSlot() {
                return 11;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(Material.PLAYER_HEAD, 0).name("&aStatistics").lore("&7Kills: &f" + MapDungeons.plugin.sql.getMobKills(uuid),
                        "&7KP/S: &f" + MapDungeons.plugin.sql.getAverageKPS(uuid)).build();
            }
        });

        set(new GUIItem()
        {
            @Override
            public int getSlot() {
                return 15;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(Material.SKELETON_SKULL, 0).name("&cDeaths").lore("&f" + MapDungeons.plugin.sql.getDeaths(uuid)).build();
            }
        });

        refresh(e.getInventory());
    }
}
