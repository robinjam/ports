package net.robinjam.bukkit.ports.commands;

import java.util.List;
import net.robinjam.bukkit.ports.persistence.Port;
import net.robinjam.bukkit.util.Command;
import net.robinjam.bukkit.util.CommandExecutor;
import net.robinjam.bukkit.util.EconomyUtils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Handles the /port price command.
 * 
 * @author robinjam
 */
@Command(name = "price", usage = "[name] <price>", permissions = "ports.price", min = 1, max = 2)
public class PriceCommand implements CommandExecutor {

	public void onCommand(CommandSender sender, List<String> args) {
		Port port = Port.get(args.get(0));
		
		if (port == null) {
			sender.sendMessage(ChatColor.RED + "There is no such port.");
		}
		else {
			if (args.size() == 2) {
				double price;

				try {
					price = Double.parseDouble(args.get(1));
				} catch (Exception ex) {
					sender.sendMessage(ChatColor.RED + "Price must be a number.");
					return;
				}
				
				port.setPrice(price);
				sender.sendMessage(ChatColor.AQUA + "Price updated.");
				
				if (EconomyUtils.getEconomy() == null) {
					sender.sendMessage(ChatColor.RED + "WARNING: Vault and a compatible economy plugin are required for economy support. Players will be able to use this " + port.getDescription() + " for free until you install Vault and a compatible economy plugin.");
				}
			}
			else {
				port.setPrice(null);
				sender.sendMessage(ChatColor.AQUA + "Price removed.");
			}
			Port.save();
		}
	}

}
