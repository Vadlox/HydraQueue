package vadlox.dev.hydraQueue;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public final class HydraQueue extends JavaPlugin implements CommandExecutor {

    private final Queue<Player> queue = new LinkedList<>();
    private final Random random = new Random();
    private final String PREFIX = "§c§lQueue§8 » §r";

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getCommand("queue").setExecutor(this);
        this.getCommand("q").setExecutor(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(PREFIX + "Only players can use this command.");
            return true;
        }
        Player player = (Player) sender;
        if (queue.contains(player)) {
            queue.remove(player);
            player.sendMessage(PREFIX + "You have left the queue.");
            return true;
        }
        queue.add(player);
        player.sendMessage(PREFIX + "You have joined the queue. Waiting for more players...");

        // If more than one player is in the queue, teleport all together
        if (queue.size() > 1) {
            Player first = queue.peek();
            Location randomLoc = getRandomLocation(first.getWorld());
            StringBuilder names = new StringBuilder();
            for (Player p : queue) {
                names.append(p.getName()).append(", ");
            }
            String allNames = names.substring(0, names.length() - 2);

            for (Player p : queue) {
                p.teleport(randomLoc);
                p.sendMessage(PREFIX + "You have been teleported with: " + allNames + "!");
            }
            queue.clear();
        }
        return true;
    }

    private Location getRandomLocation(World world) {
        int min = -1000, max = 1000;
        int x = random.nextInt(max - min + 1) + min;
        int z = random.nextInt(max - min + 1) + min;
        int y = world.getHighestBlockYAt(x, z) + 1;
        return new Location(world, x + 0.5, y, z + 0.5);
    }
}
