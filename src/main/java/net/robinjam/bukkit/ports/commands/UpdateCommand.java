package net.robinjam.bukkit.ports.commands;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import java.util.List;
import net.robinjam.bukkit.ports.persistence.Port;
import net.robinjam.bukkit.util.Command;
import net.robinjam.bukkit.util.CommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Handles the /port update command.
 * 
 * @author robinjam
 */
@Command(name = "update", usage = "[name]", permissions = "ports.update", playerOnly = true, min = 1, max = 1)
public class UpdateCommand implements CommandExecutor {

	public void onCommand(CommandSender sender, List<String> args) {
		Player player = (Player) sender;
		Port port = Port.get(args.get(0));

		if (port == null) {
			sender.sendMessage(ChatColor.RED + "There is no such port.");
		} else if (!player.getWorld().getName().equals(port.getWorld())) {
			sender.sendMessage(ChatColor.RED
					+ "That port is in a different world ('" + port.getWorld()
					+ "').");
		} else {
			WorldEdit worldEdit = WorldEdit.getInstance();
			LocalSession session = worldEdit.getSession(player.getName());
			Region selection;
			try {
				selection = session.getSelection(new BukkitWorld(player
						.getWorld()));
			} catch (IncompleteRegionException ex) {
				sender.sendMessage(ChatColor.RED
						+ "Please select the activation area using WorldEdit first.");
				return;
			}

			if (!(selection instanceof CuboidRegion)) {
				sender.sendMessage(ChatColor.RED
						+ "Only cuboid regions are supported.");
				return;
			}

			port.setActivationRegion(((CuboidRegion) selection).clone());
			Port.save();
			sender.sendMessage(ChatColor.AQUA + "Activation region updated.");
		}
	}

}
