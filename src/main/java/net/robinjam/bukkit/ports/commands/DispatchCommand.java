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
public class DispatchCommand implements CommandExecutor {
    
    private final Ports plugin;

    public DispatchCommand(final Ports plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 3) {
            sender.sendMessage(ChatColor.YELLOW + String.format("Usage: /%s dispatch [name] [schedule]", label));
        } else if (!sender.hasPermission("ports.dispatch")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission.");
        } else {
            String name = args[1];
            int dispatchSchedule;
            
            try {
                dispatchSchedule = Integer.parseInt(args[2]);
            } catch (Exception ex) {
                sender.sendMessage(ChatColor.RED + "Dispatch schedule must be a number.");
                return true;
            }
            
            if (dispatchSchedule < 0) {
                sender.sendMessage(ChatColor.RED + "Dispatch schedule must be positive.");
                return true;
            }
            
            Port port = plugin.getDatabase().find(Port.class).where("name = :name").setParameter("name", name).findUnique();
            
            if (port == null) {
                sender.sendMessage(ChatColor.RED + "There is no such port.");
            } else {
                port.setDispatchSchedule(dispatchSchedule);
                plugin.getDatabase().update(port);
                sender.sendMessage(ChatColor.AQUA + "Dispatch schedule updated.");
            }
        }
        
        return true;
    }
    
}
