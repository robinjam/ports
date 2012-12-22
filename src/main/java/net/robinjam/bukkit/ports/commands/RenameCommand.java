package net.robinjam.bukkit.ports.commands;

import java.util.List;
import net.robinjam.bukkit.ports.persistence.Port;
import net.robinjam.bukkit.util.Command;
import net.robinjam.bukkit.util.CommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Handles the /port rename command.
 * 
 * @author robinjam
 */
@Command(name = "rename", usage = "[old] [new]", permissions = "ports.rename", min = 2, max = 2)
public class RenameCommand implements CommandExecutor {

	public void onCommand(CommandSender sender, List<String> args) {
		Port port = Port.get(args.get(0));

		if (port == null) {
			sender.sendMessage(ChatColor.RED + "There is no such port.");
		} else {
			port.setName(args.get(1));
			Port.save();
			sender.sendMessage(ChatColor.AQUA + "Port renamed.");
		}
	}

}
