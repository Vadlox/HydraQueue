# HydraQueue

HydraQueue is a simple Minecraft plugin that allows players to queue up and be teleported together to a random location in the world. It's inspired by queue plugins used for group teleportation and event coordination.

## Features

- Players can join or leave the teleport queue using `/queue` or `/q`.
- When two or more players are in the queue, all are instantly teleported to the same random location in the world.
- Players can leave the queue by running the command again.
- All messages are prefixed for clarity.
- Admin GUI for editing config in-game.
- Full PlaceholderAPI support in all messages.
- **Automatic update notification:** The plugin checks the [GitHub repository](https://github.com/Vadlox/HydraQueue) for updates on enable/reload and notifies admins in-game if a new version is available. The update notification message is configurable.

## Commands

| Command                    | Description                                       |
|----------------------------|---------------------------------------------------|
| `/queue` / `/q`            | Join or leave the teleport queue.                 |
| `/hydraqueue admingui`     | Open the admin settings GUI (requires permission).|

## How It Works

1. **Join the Queue:**  
   Players use `/queue` or `/q` to join the teleport queue.  
   If a player is already in the queue and runs the command again, they are removed from the queue.

2. **Teleportation:**  
   When two or more players are in the queue, everyone in the queue is teleported together to a random location in the world.  
   The queue is then cleared.

3. **Message Prefix:**  
   All plugin messages are prefixed with `§c§lQueue§8 »` for easy identification.

## PlaceholderAPI Integration

HydraQueue supports [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) for custom placeholders and allows you to use PlaceholderAPI placeholders in all configurable messages.

### Available Placeholders

| Placeholder                  | Description                       |
|------------------------------|-----------------------------------|
| `%hydraqueue_queue_size%`    | Number of players in the queue    |

To use these placeholders, ensure PlaceholderAPI is installed and reload your server or use `/papi reload`.

You can also use any other PlaceholderAPI placeholders (such as `%player_name%`) in your `config.yml` messages.

## Example

- Player1 runs `/queue` and waits.
- Player2 runs `/queue`.
- Both Player1 and Player2 are teleported together to a random spot in the world.

## Configuration

You can customize the prefix, the world for random teleportation, and all plugin messages in `config.yml`:

```yaml
prefix: "§c§lQueue§8 » §r"
rtp_world: "world" # The world players will be randomly teleported in
messages:
  only_players: "Only players can use this command."
  joined: "You have joined the queue. Waiting for more players..."
  left: "You have left the queue."
  teleported: "You have been teleported with: %players%!"
  no_permission: "You do not have permission to use this command."
  update_notice: "A new version of HydraQueue is available on GitHub!"
```

- **rtp_world:** Set this to the name of the world where you want players to be randomly teleported. If the world does not exist, the plugin will use the world of the first player in the queue.
- **All messages** support [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) placeholders.  
  For example:  
  ```yaml
  joined: "Welcome %player_name% to the queue! There are now %hydraqueue_queue_size% players waiting."
  ```
- **Admin GUI and update notification messages** are fully configurable in `config.yml` under the `messages:` section.

Edit `config.yml` in your server's `plugins/HydraQueue` folder to change these messages.  
If you add new PlaceholderAPI expansions, use `/papi reload` or restart your server to make them available in messages.

## Admin Settings GUI

Users with `hydraqueue.admin.settings`, `hydraqueue.admin.*`, or operator status can run:
```
/hydraqueue admingui
```
This opens a GUI to edit config settings (prefix, messages, RTP world, etc.) in-game. Changes are saved automatically.  
**All messages shown in the admin GUI are configurable in `config.yml`.**

## Update Notification

When the plugin is enabled or reloaded, it checks the [GitHub repository](https://github.com/Vadlox/HydraQueue) for updates.  
If a new version is detected, all online admins (with `hydraqueue.admin.settings`, `hydraqueue.admin.*`, or operator status) will receive a configurable update notification message.

---

**Website:** [vadlox.hydraquest.net](https://vadlox.hydraquest.net)

