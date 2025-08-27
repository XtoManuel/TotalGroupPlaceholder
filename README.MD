# TotalGroupPlaceholder

TotalGroupPlaceholder is a plugin for Minecraft servers that allows you to manage and expand custom placeholders using PlaceholderAPI.

## Features

- Integration with PlaceholderAPI
- Custom commands for group management
- Easy expansion of placeholders

## Installation

1. Download the `TotalGroupPlaceholder-<version>.jar` file.
2. Place the `.jar` file in your server's `plugins` folder (Spigot/Paper).
3. Make sure you have [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) installed. (v2.11.6)
4. Restart your server.

## Usage

- Use the `/totalgroup` command to manage groups and placeholders.
- Check the PlaceholderAPI documentation to integrate placeholders into your server.

## Available Placeholders

- `%totalgroup_<group>%` — Shows the number of members in the specified LuckPerms group. Example: `%totalgroup_admin%`
- You can use any group name registered in LuckPerms.

## Available Commands

- `/totalgroup` or `/totalgroup help` - Shows information and group management options.
- `/totalgroup <group>` — Shows the number of members in the specified group.
- `/totalgroup all` — Shows the number of members for all LuckPerms groups.
- `/totalgroup help` — Shows help and usage information.

## Requirements

- Java 8 or higher
- Spigot, Paper, or any Bukkit-compatible server
- PlaceholderAPI
- LuckPerms (for group and permissions management)

## Project Structure

```text
src/main/java/com/usuario/totalgroupplaceholder/
  Main.java
  TotalGroupCommand.java
  TotalGroupExpansion.java
src/main/resources/
  plugin.yml
```

## Build

This project uses Maven. To build:

```powershell
mvn clean package
```

The compiled file will be in the `target/` folder.

## Credits

Developed by XtoManuel.

## License

This project is licensed under the MIT License.
