package net.robinjam.bukkit.ports.commands;

import java.util.List;
import net.robinjam.bukkit.ports.persistence.Port;
import net.robinjam.bukkit.util.Command;
import net.robinjam.bukkit.util.CommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Handles the /port arrive command.
 * 
 * @author robinjam
 */
@Command(name = "arrive",
         usage = "[name]",
         permissions = "ports.arrive",
         playerOnly = true,
         min = 1, max = 1)
public class ArriveCommand implements CommandExecutor {
    
    public void onCommand(CommandSender sender, List<String> args) {
        Player player = (Player) sender;
        Port port = Port.get(args.get(0));

        if (port == null) {
            sender.sendMessage(ChatColor.RED + "There is no such port.");
        }
        else if (!player.getWorld().getName().equals(port.getWorld())) {
            sender.sendMessage(ChatColor.RED + "That port is in a different world ('" + port.getWorld() + "').");
        }
        else {
            port.setArrivalLocation(player.getLocation());
            port.save();
            sender.sendMessage(ChatColor.AQUA + "Arrival location updated.");
        }
    }
    
}
