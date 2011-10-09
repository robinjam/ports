package net.robinjam.bukkit.ports.commands;

import net.robinjam.bukkit.ports.Ports;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author robinjam
 */
public class ReloadCommand implements CommandExecutor {
    
    private final Ports plugin;

    public ReloadCommand(final Ports plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.YELLOW + String.format("Usage: /%s reload", label));
        } else if (!sender.hasPermission("ports.reload")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission.");
        } else {
            plugin.reload();
            sender.sendMessage(ChatColor.AQUA + plugin.getDescription().getName() + " configuration reloaded");
        }
        
        return true;
    }
    
}
