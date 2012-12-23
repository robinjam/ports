package net.robinjam.bukkit.ports.commands;

import java.util.List;
import net.robinjam.bukkit.ports.Ports;
import net.robinjam.bukkit.ports.persistence.Port;
import net.robinjam.bukkit.util.Command;
import net.robinjam.bukkit.util.CommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Handles the /port reload command.
 * 
 * @author robinjam
 */
@Command(name = "reload", permissions = "ports.reload")
public class ReloadCommand implements CommandExecutor {

	public void onCommand(CommandSender sender, List<String> args) {
		Ports.getInstance().reloadConfig();
		Port.load();
		sender.sendMessage(ChatColor.AQUA + "Ports configuration reloaded");
	}

}
