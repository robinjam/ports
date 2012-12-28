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
 * Handles the /port create command.
 * 
 * @author robinjam
 */
@Command(name = "create", usage = "[name]", permissions = "ports.create", playerOnly = true, min = 1, max = 1)
public class CreateCommand implements CommandExecutor {

	public void onCommand(CommandSender sender, List<String> args) {
		String name = args.get(0);

		if (Port.get(name) != null) {
			sender.sendMessage(ChatColor.RED
					+ "There is already a port with that name. Please pick a unique name.");
			return;
		}

		Player player = (Player) sender;
		LocalSession session = WorldEdit.getInstance().getSession(
				player.getName());
		if (session == null) {
			sender.sendMessage(ChatColor.RED
					+ "Please select the activation area using WorldEdit first.");
			return;
		}
		
		Region selection;
		try {
			selection = session
					.getSelection(new BukkitWorld(player.getWorld()));
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

		Port port = new Port();
		port.setName(name);
		port.setActivationRegion(((CuboidRegion) selection).clone());
		port.setArrivalLocation(player.getLocation());
		Port.save();

		sender.sendMessage(ChatColor.AQUA
				+ "The port was created successfully.");
	}

}
