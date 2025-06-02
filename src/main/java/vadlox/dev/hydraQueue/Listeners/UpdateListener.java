package vadlox.dev.hydraQueue.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import vadlox.dev.hydraQueue.HydraQueue;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateListener implements Listener {
    private static boolean checking = false;

    public static void checkForGitHubUpdates(HydraQueue plugin) {
        if (checking) return;
        checking = true;
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                // URL to one of the files in GitHub repo (example: HydraQueue.java)
                String fileUrl = "https://raw.githubusercontent.com/Vadlox/HydraQueue/main/src/main/java/vadlox/dev/hydraQueue/HydraQueue.java";
                HttpURLConnection conn = (HttpURLConnection) new URL(fileUrl).openConnection();
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setRequestMethod("GET");
                if (conn.getResponseCode() == 200) {
                    int remoteSize = conn.getContentLength();
                    // Compare with local HydraQueue.java file's size
                    int localSize = plugin.getClass().getResource("HydraQueue.class").openConnection().getContentLength();
                    if (remoteSize > 0 && localSize > 0 && remoteSize != localSize) {
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            String msg = plugin.getConfig().getString(
                                "messages.update_notice",
                                "An update is available on GitHub!"
                            );
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                if (p.hasPermission("hydraqueue.admin.settings")
                                        || p.hasPermission("hydraqueue.admin.*")
                                        || p.isOp()) {
                                    p.sendMessage(msg);
                                }
                            }
                        });
                    }
                }
            } catch (Exception ignored) {
            }
            checking = false;
        });
    }
}
