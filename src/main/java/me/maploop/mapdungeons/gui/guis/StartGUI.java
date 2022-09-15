package me.maploop.mapdungeons.gui.guis;

import me.maploop.mapdungeons.data.ServerData;
import me.maploop.mapdungeons.gui.GUI;
import me.maploop.mapdungeons.gui.GUIOpenEvent;
import me.maploop.mapdungeons.gui.guiitem.GUIClickableItem;
import me.maploop.mapdungeons.util.SUtil;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class StartGUI extends GUI
{
    private int[] interior = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25
    };

    public StartGUI() {
        super("Start a Dungeon", 36);
    }

    @Override
    public void onOpen(GUIOpenEvent e) {
        border(SUtil.getStack(Material.BLACK_STAINED_GLASS_PANE, 0).build());
        set(GUIClickableItem.getCloseItem(31));

        for (int i = 0; i < ServerData.getActiveDungeons().size(); i++) {
            String dung = new ArrayList<>(ServerData.getActiveDungeons()).get(i);
            int finalI = i;
            set(new GUIClickableItem()
            {
                @Override
                public void run(InventoryClickEvent e) {
                    e.getWhoClicked().teleport(ServerData.getSpawnFor(dung));
                    e.getWhoClicked().sendMessage("§aStarting in...");
                }

                @Override
                public int getSlot() {
                    return interior[finalI];
                }

                @Override
                public ItemStack getItem() {
                    return SUtil.getStack(Material.PAPER, 0).name("§a" + SUtil.toNormalCase(dung)).
                            lore("&eClick to start!").build();
                }
            });
        }
    }
}
