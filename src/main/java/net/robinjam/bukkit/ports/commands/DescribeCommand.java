package net.robinjam.bukkit.ports.commands;

import java.util.List;
import net.robinjam.bukkit.ports.persistence.Port;
import net.robinjam.bukkit.util.Command;
import net.robinjam.bukkit.util.CommandExecutor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Handles the /port describe command.
 * 
 * @author robinjam
 */
@Command(name = "describe", usage = "[name] <description>", permissions = "ports.describe", min = 1, max = -1)
public class DescribeCommand implements CommandExecutor {

	public void onCommand(CommandSender sender, List<String> args) {
		Port port = Port.get(args.get(0));

		if (port == null) {
			sender.sendMessage(ChatColor.RED + "There is no such port.");
		} else {
			if (args.size() < 2) {
				port.setDescription(null);
				sender.sendMessage(ChatColor.AQUA + "Description removed.");
			} else {
				port.setDescription(StringUtils.join(args.subList(1, args.size()), " "));
				sender.sendMessage(ChatColor.AQUA + "Description updated.");
			}
			Port.save();
		}
	}

}
