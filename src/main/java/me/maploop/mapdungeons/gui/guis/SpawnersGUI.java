package me.maploop.mapdungeons.gui.guis;

import me.maploop.mapdungeons.gui.GUI;
import me.maploop.mapdungeons.gui.GUIOpenEvent;
import me.maploop.mapdungeons.gui.guiitem.GUIClickableItem;
import me.maploop.mapdungeons.session.Dungeon;
import me.maploop.mapdungeons.util.Messages;
import me.maploop.mapdungeons.util.PaginationList;
import me.maploop.mapdungeons.util.SUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class SpawnersGUI extends GUI
{
    private int[] interior = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25
    };

    private final Dungeon dungeon;
    private int page;

    public SpawnersGUI(Dungeon dungeon) {
        super("Spawners for " + dungeon.getName(), 36);
        this.dungeon = dungeon;
        this.page = 1;
        PaginationList<Location> locationsPaginationList = new PaginationList<>(dungeon.getMobSpawns(), 14);
        setTitle("Spawners" + (locationsPaginationList.getPage(1) != null ? " | Page " + page + " of " + locationsPaginationList.getPageCount() : ""));
    }

    public SpawnersGUI(Dungeon dungeon, int page) {
        super("Spawners for " + dungeon.getName(), 36);
        this.dungeon = dungeon;
        this.page = page;
        PaginationList<Location> locationsPaginationList = new PaginationList<>(dungeon.getMobSpawns(), 14);
        setTitle("Spawners" + (locationsPaginationList.getPage(1) != null ? " | Page " + page + " of " + locationsPaginationList.getPageCount() : ""));
    }

    @Override
    public void onOpen(GUIOpenEvent e) {
        PaginationList<Location> locationsPaginationList = new PaginationList<>(dungeon.getMobSpawns(), 14);
        List<Location> current = locationsPaginationList.getPage(page);

        Player player = e.getPlayer();
        border(SUtil.getStack(Material.BLACK_STAINED_GLASS_PANE, 0).build());

        set(GUIClickableItem.createGUIOpenerItem(new ConfigGUI(dungeon), player, "§eBack", 31, Material.ARROW, (short) 0, "§7Go back to Configure " + dungeon.getName()));
        if (locationsPaginationList.getPageCount() > 1) {
            set(new GUIClickableItem()
            {
                @Override
                public void run(InventoryClickEvent e) {
                    new SpawnersGUI(dungeon, page + 1).open(player);
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
                    new SpawnersGUI(dungeon, page - 1).open(player);
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

        if (current != null) {
            for (int i = 0; i < current.size(); i++) {
                Location loc = current.get(i);
                int finalI = i;
                set(new GUIClickableItem()
                {
                    @Override
                    public void run(InventoryClickEvent e) {
                        switch (e.getClick()) {
                            case RIGHT: {
                                dungeon.getMobSpawns().set(finalI, player.getLocation());
                                dungeon.save();
                                player.sendMessage(Messages.get("config.spawner-set", Map.ofEntries(Map.entry("{dungeon}", dungeon.getName()))));
                                SUtil.delay(() -> new SpawnersGUI(dungeon, page).open(player), 3);
                                break;
                            }
                            case LEFT: {
                                player.teleport(dungeon.getMobSpawns().get(finalI));
                                player.sendMessage(Messages.get("config.teleport"));
                                break;
                            }
                            case SHIFT_LEFT:
                            case SHIFT_RIGHT: {
                                SUtil.openConfirmationForm(player, SpawnersGUI.this, "&7Delete the spawner", "&7Cancel and go back",
                                        (player) -> {
                                            dungeon.getMobSpawns().remove(finalI);
                                            dungeon.save();
                                            player.sendMessage(Messages.get("config.spawner-deleted", Map.ofEntries(Map.entry("{dungeon}", dungeon.getName()))));
                                            SUtil.delay(() -> new SpawnersGUI(dungeon, page).open(player), 3);
                                        });
                                break;
                            }
                        }
                    }

                    @Override
                    public int getSlot() {
                        return interior[finalI];
                    }

                    @Override
                    public ItemStack getItem() {
                        return SUtil.getStack(Material.BOWL, 0).name("&dSpawner #" + (finalI + 1))
                                .lore("&7Location: &f" + SUtil.prettify(loc), "", "&eClick to teleport!", "&bRight-click to set!", "&cShift-click to delete!").build();
                    }
                });
            }
        }

        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e) {
                dungeon.addSpawnPoint(player.getLocation());
                dungeon.save();
                player.sendMessage(Messages.get("config.spawner-added", Map.ofEntries(Map.entry("{dungeon}", dungeon.getName()))));
                SUtil.delay(() -> new SpawnersGUI(dungeon, page).open(player), 3);
            }

            @Override
            public int getSlot() {
                return 8;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(Material.GREEN_CANDLE, 0).name("&aAdd a Spawner").lore("&eClick to add a new spawner!").build();
            }
        });
    }
}
