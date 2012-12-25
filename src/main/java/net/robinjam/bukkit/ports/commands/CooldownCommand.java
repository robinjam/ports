package net.robinjam.bukkit.ports.commands;

import java.util.List;
import net.robinjam.bukkit.ports.persistence.Port;
import net.robinjam.bukkit.util.Command;
import net.robinjam.bukkit.util.CommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Handles the /port cooldown command.
 * 
 * @author robinjam
 */
@Command(name = "cooldown", usage = "[name] <cooldown>", permissions = "ports.cooldown", min = 1, max = 2)
public class CooldownCommand implements CommandExecutor {

	public void onCommand(CommandSender sender, List<String> args) {
		Port port = Port.get(args.get(0));
		
		if (port == null) {
			sender.sendMessage(ChatColor.RED + "There is no such port.");
		}
		else {
			if (args.size() == 2) {
				int cooldown;

				try {
					cooldown = Integer.parseInt(args.get(1));
				} catch (Exception ex) {
					sender.sendMessage(ChatColor.RED
							+ "Cooldown must be a number.");
					return;
				}

				if (cooldown < 1) {
					sender.sendMessage(ChatColor.RED
							+ "Cooldown must be at least 1.");
					return;
				}
				
				port.setCooldown(cooldown);
				sender.sendMessage(ChatColor.AQUA + "Cooldown updated.");
			}
			else {
				port.setCooldown(null);
				sender.sendMessage(ChatColor.AQUA + "Cooldown removed.");
			}
			Port.save();
		}
	}

}
