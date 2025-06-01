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

No configuration is required. Just drop the plugin into your server's `plugins` folder and reload/restart the server.

---

**Website:** [vadlox.hydraquest.net](https://vadlox.hydraquest.net)
