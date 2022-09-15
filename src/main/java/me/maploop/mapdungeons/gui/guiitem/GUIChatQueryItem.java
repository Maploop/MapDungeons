package me.maploop.mapdungeons.gui.guiitem;

import me.maploop.mapdungeons.gui.GUI;

public interface GUIChatQueryItem extends GUIClickableItem {
    GUI onQueryFinish(String query);

    default boolean acceptRightClick() {
        return true;
    }
}
