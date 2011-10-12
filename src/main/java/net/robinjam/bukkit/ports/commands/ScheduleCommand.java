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
public class ScheduleCommand implements CommandExecutor {
    
    private final Ports plugin;

    public ScheduleCommand(final Ports plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 3) {
            sender.sendMessage(ChatColor.YELLOW + String.format("Usage: /%s schedule [name] [schedule]", label));
        } else if (!sender.hasPermission("ports.schedule")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission.");
        } else {
            String name = args[1];
            int departureSchedule;
            
            try {
                departureSchedule = Integer.parseInt(args[2]);
            } catch (Exception ex) {
                sender.sendMessage(ChatColor.RED + "Departure schedule must be a number.");
                return true;
            }
            
            if (departureSchedule < 0) {
                sender.sendMessage(ChatColor.RED + "Departure schedule must be positive.");
                return true;
            }
            
            Port port = plugin.getDatabase().find(Port.class).where().ieq("name", name).findUnique();
            
            if (port == null) {
                sender.sendMessage(ChatColor.RED + "There is no such port.");
            } else {
                port.setDepartureSchedule(departureSchedule);
                plugin.getDatabase().update(port);
                sender.sendMessage(ChatColor.AQUA + "Departure schedule updated.");
            }
        }
        
        return true;
    }
    
}
