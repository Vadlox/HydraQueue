package vadlox.dev.hydraQueue.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import vadlox.dev.hydraQueue.HydraQueue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateListener implements Listener {
    private static boolean checking = false;

    public static void checkForGitHubUpdates(final HydraQueue plugin) {
        if (checking) return;
        checking = true;
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                // Fetch plugin.yml from the GitHub repo
                String pluginYmlUrl = "https://raw.githubusercontent.com/Vadlox/HydraQueue/main/src/main/resources/plugin.yml";
                HttpURLConnection conn = (HttpURLConnection) new URL(pluginYmlUrl).openConnection();
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setRequestMethod("GET");

                if (conn.getResponseCode() == 200) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream())
                    );
                    String line;
                    String remoteVersion = null;
                    while ((line = reader.readLine()) != null) {
                        // Look for the version line, e.g.: version: '0.6.1'
                        if (line.trim().startsWith("version:")) {
                            remoteVersion = line.replace("version:", "")
                                                .replace("'", "")
                                                .trim();
                            break;
                        }
                    }
                    reader.close();

                    // Compare remote version with local version
                    if (remoteVersion != null) {
                        final String localVersion = plugin.getDescription().getVersion();
                        final String remoteVersionFixed = remoteVersion;
                        // If the versions differ, announce an update
                        if (!remoteVersionFixed.equals(localVersion)) {
                            Bukkit.getScheduler().runTask(plugin, () -> {
                                final String msg = plugin.getConfig().getString(
                                        "messages.update_notice",
                                        "An update is available on GitHub!"
                                );
                                for (Player p : Bukkit.getOnlinePlayers()) {
                                    if (p.hasPermission("hydraqueue.admin.settings")
                                            || p.hasPermission("hydraqueue.admin.*")
                                            || p.isOp()) {
                                        p.sendMessage(msg + " (v" + remoteVersionFixed + ")");
                                    }
                                }
                            });
                        }
                    }
                }
            } catch (Exception ignored) {
            }
            checking = false;
        });
    }
}
