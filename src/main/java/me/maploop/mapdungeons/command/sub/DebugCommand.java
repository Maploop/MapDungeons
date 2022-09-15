package me.maploop.mapdungeons.command.sub;

import me.maploop.mapdungeons.command.CommandParameters;
import me.maploop.mapdungeons.command.CommandSource;
import me.maploop.mapdungeons.command.DCommand;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Zombie;

import java.util.List;

@CommandParameters(usage = "md debug", description = "Developer mode debug command", permission = "dungeons.developer")
public class DebugCommand extends DCommand
{
    @Override
    public void run(CommandSource sender, String[] args) {
        Zombie zombie = sender.getPlayer().getWorld().spawn(sender.getPlayer().getLocation(), Zombie.class);
        ClientboundRemoveEntitiesPacket packet = new ClientboundRemoveEntitiesPacket(zombie.getEntityId());
        ((CraftPlayer) sender.getPlayer()).getHandle().connection.send(packet);
    }

    @Override
    public List<String> tabCompleters(CommandSender sender, String alias, String[] args) {
        return null;
    }
}
