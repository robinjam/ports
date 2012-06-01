package net.robinjam.bukkit.ports.commands;

import java.util.List;
import net.robinjam.bukkit.ports.persistence.Port;
import net.robinjam.bukkit.util.Command;
import net.robinjam.bukkit.util.CommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Handles the /port link command.
 * 
 * @author robinjam
 */
@Command(name = "link", usage = "[port1] [port2]", permissions = "ports.link", min = 2, max = 2)
public class LinkCommand implements CommandExecutor {

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
		} else if (fromName.equals(toName)) {
			from.setDestination(from);
			from.save();
			sender.sendMessage(ChatColor.AQUA
					+ "Destination updated for port '" + from.getName() + "'.");
		} else {
			from.setDestination(to);
			from.save();
			to.setDestination(from);
			to.save();
			sender.sendMessage(ChatColor.AQUA
					+ "Destinations updated for ports '" + from.getName()
					+ "' and '" + to.getName() + "'.");
		}
	}

}
