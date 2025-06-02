package vadlox.dev.hydraQueue;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.clip.placeholderapi.PlaceholderAPI;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import vadlox.dev.hydraQueue.admin.AdminGuiCommand;
import vadlox.dev.hydraQueue.admin.SettingsMenuListener;

public final class HydraQueue extends JavaPlugin implements CommandExecutor {

    private final Queue<Player> queue = new LinkedList<>();
    private final Random random = new Random();

    private String prefix;
    private String onlyPlayersMsg;
    private String joinedMsg;
    private String leftMsg;
    private String teleportedMsg;
    private String rtpWorldName;

    private boolean papiEnabled;

    public int getQueueSize() {
        return queue.size();
    }

    @Override
    public void onEnable() {
        // Save default config if not present
        saveDefaultConfig();
        loadMessages();

        papiEnabled = getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;
        if (papiEnabled) {
            new PlaceholderExpansionHook(this).register();
        }

        this.getCommand("queue").setExecutor(this);
        this.getCommand("q").setExecutor(this);

        // Register /hydraqueue command and tab completer
        getCommand("hydraqueue").setExecutor(new AdminGuiCommand(this));
        getCommand("hydraqueue").setTabCompleter(new AdminGuiCommand(this));
        
        // Register the settings menu listener
        getServer().getPluginManager().registerEvents(new SettingsMenuListener(this), this);

        printBanner(true);
    }

    @Override
    public void onDisable() {
        printBanner(false);
        // Plugin shutdown logic
    }

    private void printBanner(boolean enabled) {
        String version = getDescription().getVersion();
        String serverVersion = getServer().getVersion();
        String javaVersion = System.getProperty("java.version");
        String os = System.getProperty("os.name") + " " + System.getProperty("os.version");
        String buildDate = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss").format(LocalDateTime.now());

        String status = enabled ? "ENABLED" : "DISABLED";
        String color = enabled ? "\u001B[32m" : "\u001B[31m"; // Green or Red
        String reset = "\u001B[0m";

        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(color + "  _    _    ____                        ");
        Bukkit.getConsoleSender().sendMessage(color + " | |  | |  / __ \\                       ");
        Bukkit.getConsoleSender().sendMessage(color + " | |__| | | |  | |_   _  ___ _   _  ___ ");
        Bukkit.getConsoleSender().sendMessage(color + " |  __  | | |  | | | | |/ _ \\ | | |/ _ \\");
        Bukkit.getConsoleSender().sendMessage(color + " | |  | | | |__| | |_| |  __/ |_| |  __/");
        Bukkit.getConsoleSender().sendMessage(color + " |_|  |_|  \\___\\_\\\\__,_|\\___|\\__,_|\\___|");
        Bukkit.getConsoleSender().sendMessage(color + "                                        ");
        Bukkit.getConsoleSender().sendMessage(color + "HydraQueue " + version + "   " + status);
        Bukkit.getConsoleSender().sendMessage(color + serverVersion);
        Bukkit.getConsoleSender().sendMessage(color + "Build Date: " + buildDate);
        Bukkit.getConsoleSender().sendMessage(color + "Java Version: " + javaVersion);
        Bukkit.getConsoleSender().sendMessage(color + "OS: " + os + reset);
        Bukkit.getConsoleSender().sendMessage("");
    }

    public void loadMessages() {
        prefix = getConfig().getString("prefix", "§c§lQueue§8 » §r");
        onlyPlayersMsg = getConfig().getString("messages.only_players", "Only players can use this command.");
        joinedMsg = getConfig().getString("messages.joined", "You have joined the queue. Waiting for more players...");
        leftMsg = getConfig().getString("messages.left", "You have left the queue.");
        teleportedMsg = getConfig().getString("messages.teleported", "You have been teleported with: %players%!");
        rtpWorldName = getConfig().getString("rtp_world", "world");
    }

    private void sendMessage(Player player, String message) {
        String msg = prefix + message;
        if (papiEnabled) {
            msg = PlaceholderAPI.setPlaceholders(player, msg);
        }
        player.sendMessage(msg);
    }

    private void sendMessage(CommandSender sender, String message) {
        String msg = prefix + message;
        if (sender instanceof Player && papiEnabled) {
            msg = PlaceholderAPI.setPlaceholders((Player) sender, msg);
        }
        sender.sendMessage(msg);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sendMessage(sender, onlyPlayersMsg);
            return true;
        }
        Player player = (Player) sender;
        if (queue.contains(player)) {
            queue.remove(player);
            sendMessage(player, leftMsg);
            return true;
        }
        queue.add(player);
        sendMessage(player, joinedMsg);

        // If more than one player is in the queue, teleport all together
        if (queue.size() > 1) {
            Player first = queue.peek();
            World rtpWorld = Bukkit.getWorld(rtpWorldName);
            if (rtpWorld == null) {
                rtpWorld = first.getWorld();
            }
            Location randomLoc = getRandomLocation(rtpWorld);
            StringBuilder names = new StringBuilder();
            for (Player p : queue) {
                names.append(p.getName()).append(", ");
            }
            String allNames = names.substring(0, names.length() - 2);

            for (Player p : queue) {
                p.teleport(randomLoc);
                String msg = teleportedMsg.replace("%players%", allNames);
                sendMessage(p, msg);
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
