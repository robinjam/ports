package net.robinjam.bukkit.ports.commands;

import java.util.List;
import net.robinjam.bukkit.ports.persistence.Port;
import net.robinjam.bukkit.util.Command;
import net.robinjam.bukkit.util.CommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Handles the /port delete command
 * 
 * @author robinjam
 */
@Command(name = "delete", usage = "[name]", permissions = "ports.delete", min = 1, max = 1)
public class DeleteCommand implements CommandExecutor {

	public void onCommand(CommandSender sender, List<String> args) {
		Port port = Port.get(args.get(0));

		if (port == null)
			sender.sendMessage(ChatColor.RED + "There is no such port.");
		else {
			Port.remove(port);
			sender.sendMessage(ChatColor.AQUA
					+ "The port was deleted successfully.");
		}
	}

}
