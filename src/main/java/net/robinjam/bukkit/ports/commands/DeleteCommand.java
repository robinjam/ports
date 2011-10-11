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
public class DeleteCommand implements CommandExecutor {
    
    private final Ports plugin;

    public DeleteCommand(final Ports plugin) {
        this.plugin = plugin;
    }
        
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(ChatColor.YELLOW + String.format("Usage: /%s delete [name]", label));
        } else if (!sender.hasPermission("ports.delete")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission.");
        } else {
            String name = args[1];
            Port port = plugin.getDatabase().find(Port.class).where("name = :name").setParameter("name", name).findUnique();
            
            if (port == null)
                sender.sendMessage(ChatColor.RED + "There is no such port to delete.");
            else {
                plugin.getDatabase().delete(port);
                sender.sendMessage(ChatColor.AQUA + "The port was deleted successfully.");
            }
        }
        
        return true;
    }
    
}
