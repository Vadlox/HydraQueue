# HydraQueue

HydraQueue is a simple Minecraft plugin that allows players to queue up and be teleported together to a random location in the world. It's inspired by queue plugins used for group teleportation and event coordination.

## Features

- Players can join or leave the teleport queue using `/queue` or `/q`.
- When two or more players are in the queue, all are instantly teleported to the same random location in the world.
- Players can leave the queue by running the command again.
- All messages are prefixed for clarity.

## Commands

| Command    | Description                                   |
|------------|-----------------------------------------------|
| `/queue`   | Join or leave the teleport queue.             |
| `/q`       | Alias for `/queue`.                           |

## How It Works

1. **Join the Queue:**  
   Players use `/queue` or `/q` to join the teleport queue.  
   If a player is already in the queue and runs the command again, they are removed from the queue.

2. **Teleportation:**  
   When two or more players are in the queue, everyone in the queue is teleported together to a random location in the world.  
   The queue is then cleared.

3. **Message Prefix:**  
   All plugin messages are prefixed with `§c§lQueue§8 »` for easy identification.

## Example

- Player1 runs `/queue` and waits.
- Player2 runs `/queue`.
- Both Player1 and Player2 are teleported together to a random spot in the world.

## Configuration

You can customize the prefix and all plugin messages in `config.yml`:

```yaml
prefix: "§c§lQueue§8 » §r"
messages:
  only_players: "Only players can use this command."
  joined: "You have joined the queue. Waiting for more players..."
  left: "You have left the queue."
  teleported: "You have been teleported with: %players%!"
```

Edit `config.yml` in your server's `plugins/HydraQueue` folder to change these messages.

---

**Website:** [vadlox.hydraquest.net](https://vadlox.hydraquest.net)
