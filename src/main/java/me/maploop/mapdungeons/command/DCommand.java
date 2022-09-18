package me.maploop.mapdungeons.command;

import lombok.Getter;
import me.maploop.mapdungeons.MapDungeons;
import me.maploop.mapdungeons.util.Messages;
import me.maploop.mapdungeons.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
public abstract class DCommand implements CommandExecutor, TabCompleter
{
    private static final Map<UUID, HashMap<String, Long>> CMD_COOLDOWN = new HashMap<>();
    public static final String COMMAND_SUFFIX = "Command";

    private final CommandParameters params;
    private final String name;
    private final String description;
    private final String usage;
    private final List<String> aliases;
    private final String permission;

    private CommandSource sender;

    protected DCommand() {
        this.params = this.getClass().getAnnotation(CommandParameters.class);
        this.name = this.getClass().getSimpleName().replace(COMMAND_SUFFIX, "").toLowerCase();
        this.description = this.params.description();
        this.usage = this.params.usage();
        this.aliases = Arrays.asList(this.params.aliases().split(","));
        this.permission = this.params.permission();
    }

    public boolean hasAliases() {
        return !aliases.get(0).equals("");
    }

    public abstract void run(CommandSource sender, String[] args);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public static void register() {
        MapDungeons.getPlugin().commandMap.register("", new ACommand());
    }

    public static class ACommand extends Command {

        public DCommand cmd;

        public ACommand() {
            super("md", "The main command", "/dungeons <param>", new ArrayList<>());
        }

        @Override
        public boolean execute(CommandSender sender, String commandLabel, String[] args) {
            if (!(sender instanceof Player)) {
                System.out.println("Console senders cannot use commands");
                return false;
            }

            if (args.length == 0) {
                ((Player) sender).performCommand("dungeons help");
                return false;
            }

            for (DCommand c : MapDungeons.getPlugin().commandLoader.commands) {
                if (c.getName().equals(args[0])) {
                    this.cmd = c;
                }
            }

            if (this.cmd == null) {
                ((Player) sender).performCommand("dungeons help");
                return false;
            }

            cmd.sender = new CommandSource(sender);

            if (!cmd.permission.equals("") && !sender.hasPermission(cmd.permission)) {
                sender.sendMessage(Messages.get("commands.no-permission"));
                return false;
            }

            if (cmd instanceof CommandCooldown) {
                HashMap<String, Long> cooldowns = new HashMap<>();
                if (CMD_COOLDOWN.containsKey(((Player) sender).getUniqueId())) {
                    cooldowns = CMD_COOLDOWN.get(((Player) sender).getUniqueId());
                    if (cooldowns.containsKey(cmd.getName())) {
                        if (System.currentTimeMillis() - cooldowns.get(cmd.getName()) < ((CommandCooldown) cmd).getCooldown()) {
                            Messages.get("commands.cooldown", Map.ofEntries(Map.entry("{cd}", String.valueOf((double) (System.currentTimeMillis() - cooldowns.get(cmd.getName())) / 1000))));
                            return false;
                        }
                    }
                }
                cooldowns.put(cmd.getName(), System.currentTimeMillis() + ((CommandCooldown) cmd).getCooldown());
                CMD_COOLDOWN.put(((Player) sender).getUniqueId(), cooldowns);
            }

            cmd.run(cmd.sender, args);
            return false;
        }

        @Override
        public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
            if (args.length <= 1) {
                List<String> list = new ArrayList<>();
                MapDungeons.getPlugin().commandLoader.commands.forEach(entry -> list.add(entry.name));

                return list;
            } else {
                for (DCommand c : MapDungeons.getPlugin().commandLoader.commands) {
                    if (c.getName().equals(args[0])) {
                        this.cmd = c;
                        return cmd.tabCompleters(sender, alias, args);
                    }
                }

                this.cmd = null;
                return new ArrayList<>();
            }
        }
    }

    public abstract List<String> tabCompleters(CommandSender sender, String alias, String[] args);

    public void send(String message, CommandSource sender) {
        sender.send(ChatColor.GRAY + message.replace("&", "§"));
    }

    public void send(String message) {
        send(SUtil.translateColorWords(message), sender);
    }

    public void send(List<String> message) {
        SUtil.translateColorWords(message).forEach(message2 -> {
            sender.send(message2);
        });
    }
}
