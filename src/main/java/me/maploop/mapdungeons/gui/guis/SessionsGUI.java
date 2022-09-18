package me.maploop.mapdungeons.gui.guis;

import me.maploop.mapdungeons.MapDungeons;
import me.maploop.mapdungeons.gui.GUI;
import me.maploop.mapdungeons.gui.GUIOpenEvent;
import me.maploop.mapdungeons.gui.guiitem.GUIClickableItem;
import me.maploop.mapdungeons.session.DungeonSession;
import me.maploop.mapdungeons.session.DungeonSpawner;
import me.maploop.mapdungeons.util.PaginationList;
import me.maploop.mapdungeons.util.SUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class SessionsGUI extends GUI
{
    private int[] interior = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25
    };

    private final int page;
    private final UUID uuid;
    private final String username;

    public SessionsGUI(String username, UUID uuid, int page) {
        super("", 36);
        this.page = page;
        this.uuid = uuid;
        this.username = username;

        PaginationList<DungeonSession> paginationList = new PaginationList<>(MapDungeons.getPlugin().sql.getSessions(uuid), 14);
        setTitle("Sessions " + (paginationList.getPage(1) != null ? " | Page " + page + " of " + paginationList.getPageCount() : ""));
        border(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));

        if (paginationList.getPageCount() > 1) {
            set(new GUIClickableItem()
            {
                @Override
                public void run(InventoryClickEvent e) {
                    new SessionsGUI(username, uuid, page + 1).open((Player) e.getWhoClicked());
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
                    new SessionsGUI(username, uuid, page - 1).open((Player) e.getWhoClicked());
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
    public void afterOpenAsync(GUIOpenEvent e) {
        PaginationList<DungeonSession> paginationList = new PaginationList<>(MapDungeons.getPlugin().sql.getSessions(uuid), 14);
        List<DungeonSession> current = paginationList.getPage(page);

        if (current != null) {
            for (int i = 0; i < current.size(); i++) {
                DungeonSession loc = current.get(i);
                int finalI = i;
                set(new GUIClickableItem()
                {
                    @Override
                    public void run(InventoryClickEvent e) {
                        new SessionGUI(username, loc, 1).open((Player) e.getWhoClicked());
                    }

                    @Override
                    public int getSlot() {
                        return interior[finalI];
                    }

                    @Override
                    public ItemStack getItem() {
                        return SUtil.getStack(Material.PAPER, 0).name("&aSession #" + (finalI + 1))
                                .lore("&8Session ID: " + loc.getId(), "", "&7Dungeon: &f" + loc.getMap(), "&7Kills: &f" + loc.getKills(), "", "&eClick to view details!").build();
                    }
                });
            }
        }
        set(GUIClickableItem.createGUIOpenerItem(new StatsGUI(username), e.getPlayer(), "§eBack", 31, Material.ARROW, (short) 0));
        refresh(e.getInventory());
    }
}
