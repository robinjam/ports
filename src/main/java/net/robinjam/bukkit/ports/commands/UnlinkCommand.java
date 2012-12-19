package net.robinjam.bukkit.ports.commands;

import java.util.List;
import net.robinjam.bukkit.ports.persistence.Port;
import net.robinjam.bukkit.util.Command;
import net.robinjam.bukkit.util.CommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Handles the /port unlink command.
 * 
 * @author robinjam
 */
@Command(name = "unlink", usage = "[name]", permissions = "ports.unlink", min = 1, max = 1)
public class UnlinkCommand implements CommandExecutor {

	public void onCommand(CommandSender sender, List<String> args) {
		Port port = Port.get(args.get(0));

		if (port == null) {
			sender.sendMessage(ChatColor.RED + "There is no such port.");
		} else {
			port.setDestination(null);
			Port.save();
			sender.sendMessage(ChatColor.AQUA + "Port destination removed.");
		}
	}

}
