package net.robinjam.bukkit.ports.commands;

import java.util.ArrayList;
import java.util.List;
import net.robinjam.bukkit.ports.persistence.Port;
import net.robinjam.bukkit.util.Command;
import net.robinjam.bukkit.util.CommandExecutor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Handles the /port list command.
 * 
 * @author robinjam
 */
@Command(name = "list", permissions = "ports.list")
public class ListCommand implements CommandExecutor {

	public void onCommand(CommandSender sender, List<String> args) {
		List<String> portNames = new ArrayList<String>();
		for (Port port : Port.getAll()) {
			portNames.add(port.getName());
		}

		if (portNames.size() > 0) {
			sender.sendMessage(ChatColor.YELLOW + "Available ports: "
					+ StringUtils.join(portNames, ", "));
		} else {
			sender.sendMessage(ChatColor.YELLOW
					+ "There are no ports defined yet.");
		}
	}

}
