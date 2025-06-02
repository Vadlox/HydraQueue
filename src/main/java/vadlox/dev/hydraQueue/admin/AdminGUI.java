package vadlox.dev.hydraQueue.admin;

import vadlox.dev.hydraQueue.HydraQueue;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

public class AdminGUI implements CommandExecutor {
    private final HydraQueue plugin;

    public AdminGUI(HydraQueue plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Players only.");
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("hydraqueue.admin.settings")
                && !player.hasPermission("hydraqueue.admin.*")
                && !player.isOp()) {
            player.sendMessage("No permission.");
            return true;
        }
        openAdminSettingsGUI(player);
        return true;
    }

    private void openAdminSettingsGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, ChatColor.BLUE + "HydraQueue Settings");

        // Example: Prefix item
        ItemStack prefixItem = createItem(Material.NAME_TAG,
                ChatColor.YELLOW + "Prefix",
                "Current: " + plugin.getConfig().getString("prefix"));
        inv.setItem(0, prefixItem);

        // Additional items for messages, rtp_world, etc.
        // ...existing code for creating items...

        player.openInventory(inv);
    }

    private ItemStack createItem(Material mat, String name, String lore) {
        ItemStack item = new ItemStack(mat);
        // ...existing code for modifying item meta...
        return item;
    }
}
