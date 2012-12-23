package net.robinjam.bukkit.ports.commands;

import java.util.List;
import net.robinjam.bukkit.ports.persistence.Port;
import net.robinjam.bukkit.util.Command;
import net.robinjam.bukkit.util.CommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Handles the /port ticket command.
 * 
 * @author robinjam
 */
@Command(name = "ticket", usage = "[name] <item:data>", permissions = "ports.ticket", min = 1, max = 2)
public class TicketCommand implements CommandExecutor {

	public void onCommand(CommandSender sender, List<String> args) {
		Port port = Port.get(args.get(0));

		if (port == null) {
			sender.sendMessage(ChatColor.RED + "There is no such port.");
		} else {
			if (args.size() == 2) {
				String[] tokens = args.get(1).split(":");
				if (tokens.length > 2) {
					sender.sendMessage(ChatColor.RED + "Item must be in the format <item> or <item>:<data>.");
					return;
				}
				
				Integer itemId, dataValue = null;
				try {
					itemId = Integer.parseInt(tokens[0]);
				} catch (Exception ex) {
					sender.sendMessage(ChatColor.RED
							+ "Item ID must be a number.");
					return;
				}
				
				if (tokens.length == 2) {
					try {
						dataValue = Integer.parseInt(tokens[1]);
					} catch (Exception ex) {
						sender.sendMessage(ChatColor.RED
								+ "Item data must be a number.");
						return;
					}
				}
				port.setTicketItemId(itemId);
				port.setTicketDataValue(dataValue);
				sender.sendMessage(ChatColor.AQUA + "Ticket set.");
			} else {
				port.setTicketItemId(null);
				port.setTicketDataValue(null);
				sender.sendMessage(ChatColor.AQUA + "Ticket removed.");
			}
			Port.save();
		}
	}

}
