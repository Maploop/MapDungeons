package me.maploop.mapdungeons.command;

public interface CommandCooldown {
    long cooldownSeconds();

    default long getCooldown() {
        return cooldownSeconds() * 1000;
    }
}
