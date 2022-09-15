package me.maploop.mapdungeons.gui.guis;

import me.maploop.mapdungeons.gui.GUI;
import me.maploop.mapdungeons.gui.GUIOpenEvent;
import me.maploop.mapdungeons.gui.guiitem.GUIClickableItem;
import me.maploop.mapdungeons.session.Dungeon;
import me.maploop.mapdungeons.util.SUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SpawnersGUI extends GUI
{
    private int[] interior = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25
    };

    private final Dungeon dungeon;

    public SpawnersGUI(Dungeon dungeon) {
        super("Spawners for " + dungeon.getName(), 36);
        this.dungeon = dungeon;
    }

    @Override
    public void onOpen(GUIOpenEvent e) {
        Player player = e.getPlayer();
        border(SUtil.getStack(Material.BLACK_STAINED_GLASS_PANE, 0).build());

        set(GUIClickableItem.createGUIOpenerItem(new ConfigGUI(dungeon), player, "§eBack", 31, Material.ARROW, (short) 0, "§7Go back to Configure " + dungeon.getName()));

        for (int i = 0; i < dungeon.getMobSpawns().size(); i++) {
            Location loc = dungeon.getMobSpawns().get(i);
            int finalI = i;
            set(new GUIClickableItem()
            {
                @Override
                public void run(InventoryClickEvent e) {

                }

                @Override
                public int getSlot() {
                    return interior[finalI];
                }

                @Override
                public ItemStack getItem() {
                    return SUtil.getStack(Material.BONE_BLOCK)
                }
            });
        }
    }
}
