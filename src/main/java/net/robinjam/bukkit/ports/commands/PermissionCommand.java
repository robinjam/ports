package net.robinjam.bukkit.ports.commands;

import java.util.List;
import net.robinjam.bukkit.ports.persistence.Port;
import net.robinjam.bukkit.util.Command;
import net.robinjam.bukkit.util.CommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Handles the /port permission command.
 * 
 * @author robinjam
 */
@Command(name = "permission", usage = "[name] <permission>", permissions = "ports.permission", min = 1, max = 2)
public class PermissionCommand implements CommandExecutor {

	public void onCommand(CommandSender sender, List<String> args) {
		Port port = Port.get(args.get(0));

		if (port == null) {
			sender.sendMessage(ChatColor.RED + "There is no such port.");
		} else {
			if (args.size() == 2) {
				port.setPermission(args.get(1));
				sender.sendMessage(ChatColor.AQUA + "Permission set.");
			} else {
				port.setPermission(null);
				sender.sendMessage(ChatColor.AQUA + "Permission removed.");
			}
			Port.save();
		}
	}

}
