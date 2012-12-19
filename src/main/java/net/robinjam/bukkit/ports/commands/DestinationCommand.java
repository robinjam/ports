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
@Command(name = "destination", usage = "[from] [to]", permissions = "ports.destination", min = 2, max = 2)
public class DestinationCommand implements CommandExecutor {

	public void onCommand(CommandSender sender, List<String> args) {
		String fromName = args.get(0);
		String toName = args.get(1);

		Port from = Port.get(fromName);
		Port to = Port.get(toName);

		if (from == null) {
			sender.sendMessage(ChatColor.RED + "There is no port named '"
					+ fromName + "'.");
		} else if (to == null) {
			sender.sendMessage(ChatColor.RED + "There is no port named '"
					+ toName + "'.");
		} else {
			from.setDestination(to);
			Port.save();
			sender.sendMessage(ChatColor.AQUA
					+ "Destination updated for port '" + fromName + "'.");
		}
	}

}
