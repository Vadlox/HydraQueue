package vadlox.dev.hydraQueue.admin;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import vadlox.dev.hydraQueue.HydraQueue;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SettingsMenuListener implements Listener {
    private final HydraQueue plugin;
    private final Map<UUID, AwaitingInput> awaitingInput = new HashMap<>();

    public SettingsMenuListener(HydraQueue plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String title = event.getView().getTitle();
        
        // Check if it's our settings GUI
        if (!title.contains("HydraQueue Settings")) {
            return;
        }
        
        // Cancel the event to prevent taking items
        event.setCancelled(true);
        
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || !clickedItem.hasItemMeta() || !clickedItem.getItemMeta().hasDisplayName()) {
            return;
        }
        
        String displayName = clickedItem.getItemMeta().getDisplayName();
        
        // Handle different settings based on the clicked item
        if (displayName.contains("Prefix")) {
            player.closeInventory();
            player.sendMessage(ChatColor.GREEN + "Enter a new prefix in chat:");
            awaitingInput.put(player.getUniqueId(), new AwaitingInput("prefix", null));
            
        } else if (displayName.contains("RTP World")) {
            player.closeInventory();
            player.sendMessage(ChatColor.GREEN + "Enter the name of a world to use for random teleport:");
            awaitingInput.put(player.getUniqueId(), new AwaitingInput("rtp_world", null));
            
        } else if (displayName.contains("Messages")) {
            // For simplicity, just modify one message for now
            player.closeInventory();
            player.sendMessage(ChatColor.GREEN + "Enter a new 'joined' message:");
            awaitingInput.put(player.getUniqueId(), new AwaitingInput("messages.joined", null));
            
        } else if (displayName.contains("Reload Config")) {
            plugin.reloadConfig();
            plugin.loadMessages();
            player.sendMessage(ChatColor.GREEN + "Configuration reloaded!");
            player.closeInventory();
            
        } else if (displayName.contains("Save Config")) {
            plugin.saveConfig();
            player.sendMessage(ChatColor.GREEN + "Configuration saved!");
            player.closeInventory();
            
        } else if (displayName.contains("Help")) {
            player.sendMessage(ChatColor.LIGHT_PURPLE + "=== HydraQueue Help ===");
            player.sendMessage(ChatColor.YELLOW + "• Use /queue or /q to join the queue");
            player.sendMessage(ChatColor.YELLOW + "• When 2+ players are in queue, they'll teleport together");
            player.sendMessage(ChatColor.YELLOW + "• Use /hydraqueue admingui to edit config");
            player.closeInventory();
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID id = player.getUniqueId();
        
        if (awaitingInput.containsKey(id)) {
            event.setCancelled(true);
            String input = event.getMessage();
            String configPath = awaitingInput.get(id).getConfigPath();
            
            // Update the config on the main thread
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                plugin.getConfig().set(configPath, input);
                plugin.saveConfig();
                plugin.loadMessages();
                player.sendMessage(ChatColor.GREEN + "Setting updated! Reopen the GUI with /hydraqueue admingui");
            });
            
            awaitingInput.remove(id);
        }
    }
    
    // Simple class to track what a player is editing
    private static class AwaitingInput {
        private final String configPath;
        private final String oldValue;
        
        public AwaitingInput(String configPath, String oldValue) {
            this.configPath = configPath;
            this.oldValue = oldValue;
        }
        
        public String getConfigPath() {
            return configPath;
        }
        
        public String getOldValue() {
            return oldValue;
        }
    }
}
