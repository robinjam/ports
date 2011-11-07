package net.robinjam.bukkit.ports.commands;

import java.util.List;
import net.robinjam.bukkit.ports.persistence.Port;
import net.robinjam.bukkit.util.Command;
import net.robinjam.bukkit.util.CommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Handles the /port schedule command.
 * 
 * @author robinjam
 */
@Command(name = "schedule",
         usage = "[name] [schedule]",
         permissions = "ports.schedule",
         min = 2, max = 2)
public class ScheduleCommand implements CommandExecutor {
    
    public void onCommand(CommandSender sender, List<String> args) {
        String name = args.get(0);
        int departureSchedule;

        try {
            departureSchedule = Integer.parseInt(args.get(1));
        } catch (Exception ex) {
            sender.sendMessage(ChatColor.RED + "Departure schedule must be a number.");
            return;
        }

        if (departureSchedule < 0) {
            sender.sendMessage(ChatColor.RED + "Departure schedule must be positive.");
            return;
        }

        Port port = Port.get(name);

        if (port == null) {
            sender.sendMessage(ChatColor.RED + "There is no such port.");
        }
        else {
            port.setDepartureSchedule(departureSchedule);
            port.save();
            sender.sendMessage(ChatColor.AQUA + "Departure schedule updated.");
        }
    }
    
}
