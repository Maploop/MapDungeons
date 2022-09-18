package me.maploop.mapdungeons.gui.guis;

import me.maploop.mapdungeons.MapDungeons;
import me.maploop.mapdungeons.gui.GUI;
import me.maploop.mapdungeons.gui.GUIOpenEvent;
import me.maploop.mapdungeons.gui.guiitem.GUIClickableItem;
import me.maploop.mapdungeons.session.DungeonSession;
import me.maploop.mapdungeons.util.PaginationList;
import me.maploop.mapdungeons.util.SUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SessionGUI extends GUI
{
    private int[] interior = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25
    };

    private final DungeonSession session;
    private final String user;

    public SessionGUI(String user, DungeonSession session, int page) {
        super("Session Summary", 36);
        this.session = session;
        this.user = user;

        List<ItemStack> items = new ArrayList<>();
        items.add(SUtil.getStack(Material.GREEN_WOOL, 0).name("&aSession Started").lore("&7Time: &f" + SUtil.toDate(session.getStartTime())).glow().build());
        for (long kill : session.getKillTimestamps()) {
            items.add(SUtil.getStack(Material.IRON_SWORD, 0).name("&cKill Event").lore("&7Time: &f" + SUtil.toDate(kill)).build());
        }

        PaginationList<ItemStack> paginationList = new PaginationList<>(items, 14);
        List<ItemStack> current = paginationList.getPage(page);
        setTitle("Session Summary " + (paginationList.getPage(1) != null ? " | Page " + page + " of " + paginationList.getPageCount() : ""));

        if (current != null) {
            for (int i = 0; i < current.size(); i++) {
                ItemStack stack = items.get(i);
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
                        return stack;
                    }
                });
            }
        }

        if (paginationList.getPageCount() > 1) {
            set(new GUIClickableItem()
            {
                @Override
                public void run(InventoryClickEvent e) {
                    new SessionGUI(user, session, page + 1);
                }

                @Override
                public int getSlot() {
                    return 35;
                }

                @Override
                public ItemStack getItem() {
                    return SUtil.getStack(Material.ARROW, 0).name("&a->").build();
                }
            });
        }
        if (page > 1) {
            set(new GUIClickableItem()
            {
                @Override
                public void run(InventoryClickEvent e) {
                    new SessionGUI(user, session, page - 1);
                }

                @Override
                public int getSlot() {
                    return 27;
                }

                @Override
                public ItemStack getItem() {
                    return SUtil.getStack(Material.ARROW, 0).name("&a<-").build();
                }
            });
        }
    }

    @Override
    public void onOpen(GUIOpenEvent e) {
        set(GUIClickableItem.createGUIOpenerItem(new SessionsGUI(user, session.getOwner(), 1), e.getPlayer(), "§eBack", 31, Material.ARROW, (short) 0));
    }
}
