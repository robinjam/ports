package net.robinjam.bukkit.ports.commands;

import java.util.ArrayList;
import java.util.List;

import net.robinjam.bukkit.ports.Ports;
import net.robinjam.bukkit.ports.persistence.Port;
import net.robinjam.bukkit.util.Command;
import net.robinjam.bukkit.util.CommandExecutor;
import org.apache.commons.lang.StringUtils;
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
		
		Ports plugin = Ports.getInstance();

		if (portNames.size() > 0) {
			sender.sendMessage(plugin.translate("port-list", StringUtils.join(portNames, ", ")));
		} else {
			sender.sendMessage(plugin.translate("no-ports-defined"));
			
		}
	}

}
