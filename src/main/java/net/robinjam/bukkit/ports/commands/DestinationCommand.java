package net.robinjam.bukkit.ports.commands;

import java.util.List;
import net.robinjam.bukkit.ports.persistence.Port;
import net.robinjam.bukkit.util.Command;
import net.robinjam.bukkit.util.CommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Handles the /port destination command.
 * 
 * @author robinjam
 */
@Command(name = "destination", usage = "[from] <to>", permissions = "ports.destination", min = 1, max = 2)
public class DestinationCommand implements CommandExecutor {

	public void onCommand(CommandSender sender, List<String> args) {
		String fromName = args.get(0);
		Port from = Port.get(fromName);
		
		if (from == null) {
			sender.sendMessage(ChatColor.RED + "There is no port named '" + fromName + "'.");
			return;
		}
		
		if (args.size() == 1) {
			from.setDestination(null);
			sender.sendMessage(ChatColor.AQUA + "Destination removed for port '" + fromName + "'.");
		} else {
			String toName = args.get(1);
			Port to = Port.get(toName);
			
			if (to == null) {
				sender.sendMessage(ChatColor.RED + "There is no port named '" + toName + "'.");
			} else {
				from.setDestination(to);
				sender.sendMessage(ChatColor.AQUA + "Destination updated for port '" + fromName + "'.");
			}
		}
		Port.save();
	}

}
