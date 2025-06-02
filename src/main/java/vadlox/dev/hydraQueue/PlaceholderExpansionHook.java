package vadlox.dev.hydraQueue;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PlaceholderExpansionHook extends PlaceholderExpansion {

    private final HydraQueue plugin;

    public PlaceholderExpansionHook(HydraQueue plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "hydraqueue";
    }

    @Override
    public String getAuthor() {
        return "Vadlox";
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        // Example: %hydraqueue_queue_size%
        if (identifier.equalsIgnoreCase("queue_size")) {
            return String.valueOf(plugin.getQueueSize());
        }
        // Add more placeholders as needed
        return null;
    }
}
