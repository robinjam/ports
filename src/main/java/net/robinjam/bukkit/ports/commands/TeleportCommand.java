package net.robinjam.bukkit.ports.commands;

import java.util.List;

import net.robinjam.bukkit.ports.PortTickTask;
import net.robinjam.bukkit.ports.persistence.Port;
import net.robinjam.bukkit.util.Command;
import net.robinjam.bukkit.util.CommandExecutor;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Handles the /port teleport command.
 * 
 * @author robinjam
 */
@Command(name = "teleport", usage = "[name]", permissions = "ports.teleport", playerOnly = true, min = 1, max = 1)
public class TeleportCommand implements CommandExecutor {

	public void onCommand(CommandSender sender, List<String> args) {
		Player player = (Player) sender;
		Port port = Port.get(args.get(0));

		if (port == null) {
			sender.sendMessage(ChatColor.RED + "There is no such port.");
		} else {
			PortTickTask.teleportPlayer(player, port);
		}
	}

}
