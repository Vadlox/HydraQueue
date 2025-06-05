package vadlox.dev.hydraQueue.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import vadlox.dev.hydraQueue.HydraQueue;

public class HydraQueueReloadCommand implements CommandExecutor {
    private final HydraQueue plugin;

    public HydraQueueReloadCommand(HydraQueue plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.reloadConfig();
        plugin.loadMessages();
        sender.sendMessage("§aHydraQueue config reloaded!");
        return true;
    }
}
