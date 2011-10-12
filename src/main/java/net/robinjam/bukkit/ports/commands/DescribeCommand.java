package net.robinjam.bukkit.ports.commands;

import net.robinjam.bukkit.ports.Ports;
import net.robinjam.bukkit.ports.persistence.Port;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author robinjam
 */
public class DescribeCommand implements CommandExecutor {
    
    private final Ports plugin;

    public DescribeCommand(final Ports plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.YELLOW + String.format("Usage: /%s describe [name] [description]", label));
        } else if (!sender.hasPermission("ports.describe")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission.");
        } else {
            String name = args[1];
            String description = "";
            for (int x = 2; x < args.length; x++) {
                description += args[x];
                if (x < args.length - 1)
                    description += " ";
            }
            Port port = plugin.getDatabase().find(Port.class).where().ieq("name", name).findUnique();
            
            if (port == null) {
                sender.sendMessage(ChatColor.RED + "There is no such port.");
            } else {
                port.setDescription(description);
                plugin.getDatabase().update(port);
                sender.sendMessage(ChatColor.AQUA + "Description updated.");
            }
        }
        
        return true;
    }
    
}
