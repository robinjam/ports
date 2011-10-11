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
public class DestinationCommand implements CommandExecutor {
    
    private final Ports plugin;

    public DestinationCommand(final Ports plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 3) {
            sender.sendMessage(ChatColor.YELLOW + String.format("Usage: /%s destination [from] [to]", label));
        } else if (!sender.hasPermission("ports.destination")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission.");
        } else {
            String fromName = args[1];
            String toName = args[2];
            
            Port from = plugin.getDatabase().find(Port.class).where().ieq("name", fromName).findUnique();
            Port to = plugin.getDatabase().find(Port.class).where().ieq("name", toName).findUnique();
            
            if (from == null) {
                sender.sendMessage(ChatColor.RED + "There is no port named '" + fromName + "'.");
            } else if (to == null) {
                sender.sendMessage(ChatColor.RED + "There is no port named '" + toName + "'.");
            } else {
                from.setDestination(to);
                plugin.getDatabase().update(from);
                sender.sendMessage(ChatColor.AQUA + "Destination updated for port '" + fromName + "'.");
            }
        }
        
        return true;
    }
    
}
