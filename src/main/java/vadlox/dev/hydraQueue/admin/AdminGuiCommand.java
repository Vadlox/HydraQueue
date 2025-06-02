package vadlox.dev.hydraQueue.admin;

import vadlox.dev.hydraQueue.HydraQueue;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AdminGuiCommand implements CommandExecutor, TabCompleter {
    private final HydraQueue plugin;

    public AdminGuiCommand(HydraQueue plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // Only handle subcommand "admingui" here
        if (args.length > 0 && args[0].equalsIgnoreCase("admingui")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Players only.");
                return true;
            }
            Player player = (Player) sender;
            if (!player.hasPermission("hydraqueue.admin.settings")
                    && !player.hasPermission("hydraqueue.admin.*")
                    && !player.isOp()) {
                player.sendMessage(plugin.getConfig().getString("messages.no_permission", "No permission."));
                return true;
            }
            openAdminSettingsGUI(player);
            return true;
        }

        // Show basic usage or help if no recognized subcommand
        sender.sendMessage(ChatColor.RED + "Usage: /" + label + " admingui");
        return true;
    }

    private void openAdminSettingsGUI(Player player) {
        // We need to set a custom inventory holder to identify our GUI
        // But for simplicity, we'll just keep using the title to identify it
        
        // Create a 3-row inventory (27 slots)
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.DARK_PURPLE + "✦ " + 
                                               ChatColor.LIGHT_PURPLE + "HydraQueue Settings" + 
                                               ChatColor.DARK_PURPLE + " ✦");
        
        // Add decorative glass panes for modern look
        ItemStack border = createItem(Material.BLACK_STAINED_GLASS_PANE, " ", "");
        for (int i = 0; i < 27; i++) {
            if (i < 9 || i > 17 || i % 9 == 0 || i % 9 == 8) {
                inv.setItem(i, border);
            }
        }
        
        // Main settings
        ItemStack prefixItem = createItem(
            Material.NAME_TAG,
            ChatColor.GOLD + "✎ " + ChatColor.YELLOW + "Prefix",
            ChatColor.GRAY + "Current: " + ChatColor.WHITE + plugin.getConfig().getString("prefix"),
            ChatColor.ITALIC + "" + ChatColor.DARK_GRAY + "Click to edit"
        );
        inv.setItem(10, prefixItem);
        
        // World setting
        ItemStack worldItem = createItem(
            Material.COMPASS,
            ChatColor.GOLD + "✎ " + ChatColor.YELLOW + "RTP World", 
            ChatColor.GRAY + "Current: " + ChatColor.WHITE + plugin.getConfig().getString("rtp_world"),
            ChatColor.ITALIC + "" + ChatColor.DARK_GRAY + "Click to change world"
        );
        inv.setItem(11, worldItem);
        
        // Messages settings
        ItemStack messagesItem = createItem(
            Material.WRITABLE_BOOK,
            ChatColor.GOLD + "✎ " + ChatColor.YELLOW + "Messages",
            ChatColor.GRAY + "Edit all plugin messages",
            ChatColor.ITALIC + "" + ChatColor.DARK_GRAY + "Click to view and edit"
        );
        inv.setItem(12, messagesItem);
        
        // Reload config
        ItemStack reloadItem = createItem(
            Material.EMERALD,
            ChatColor.GREEN + "⟳ Reload Config",
            ChatColor.GRAY + "Apply changes from config.yml",
            ChatColor.ITALIC + "" + ChatColor.DARK_GRAY + "Click to reload"
        );
        inv.setItem(14, reloadItem);
        
        // Save config
        ItemStack saveItem = createItem(
            Material.PAPER,
            ChatColor.AQUA + "✓ Save Config",
            ChatColor.GRAY + "Save changes to disk",
            ChatColor.ITALIC + "" + ChatColor.DARK_GRAY + "Click to save"
        );
        inv.setItem(15, saveItem);
        
        // Help
        ItemStack helpItem = createItem(
            Material.KNOWLEDGE_BOOK,
            ChatColor.LIGHT_PURPLE + "ℹ Help",
            ChatColor.GRAY + "View plugin information",
            ChatColor.ITALIC + "" + ChatColor.DARK_GRAY + "Click for help"
        );
        inv.setItem(16, helpItem);

        player.openInventory(inv);
    }

    private ItemStack createItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            if (lore.length > 0) {
                meta.setLore(Arrays.asList(lore));
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        // Only suggest "admingui" as the first argument
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            if ("admingui".toLowerCase().startsWith(args[0].toLowerCase())) {
                suggestions.add("admingui");
            }
            return suggestions;
        }
        return Collections.emptyList();
    }
}
