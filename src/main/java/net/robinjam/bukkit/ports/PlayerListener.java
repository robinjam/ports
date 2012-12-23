package net.robinjam.bukkit.ports;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.robinjam.bukkit.ports.persistence.Port;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * 
 * @author robinjam
 */
public class PlayerListener implements Listener {

	private Map<Player, Port> playerLocations = new HashMap<Player, Port>();

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerMove(PlayerMoveEvent event) {
		if (event.isCancelled())
			return;

		Player player = event.getPlayer();

		if (player.isInsideVehicle())
			return;

		Set<Port> ports = new HashSet<Port>(Port.getAll());
		Iterator<Port> it = ports.iterator();
		while (it.hasNext()) {
			Port port = it.next();
			if (!player.getWorld().getName().equals(port.getWorld())) {
				it.remove();
			}
		}

		for (Port port : ports) {
			if (!port.contains(event.getFrom()) && port.contains(event.getTo())) {
				if (playerLocations.get(player) != port) {
					playerLocations.put(player, port);
					onPlayerEnterPort(player, port);
				}
			} else if (port.contains(event.getFrom())
					&& !port.contains(event.getTo())
					&& playerLocations.containsKey(player)) {
				playerLocations.remove(player);
				onPlayerLeavePort(player, port);
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if (event.isCancelled())
			return;

		Player player = event.getPlayer();
		Ports.getInstance().getTicketManager().removeTicket(player);
	}

	private void onPlayerEnterPort(Player player, Port port) {
		if (port.getDestination() == null) {
			player.sendMessage(ChatColor.YELLOW + "This "
					+ port.getDescription() + " has no destination!");
		} else if (port.getPermission() != null && !player.hasPermission(port.getPermission())) {
			player.sendMessage(ChatColor.RED + "You do not have permission to use this " + port.getDescription() + ".");
		} else {
			if (port.getDepartureSchedule() > 0) {
				Ports.getInstance().getTicketManager().addTicket(player, port);
				player.sendMessage(ChatColor.AQUA
						+ "Welcome! Please take this ticket and wait for the "
						+ port.getDescription() + " to depart.");
			} else {
				Ports.getInstance().teleportPlayer(player, port);
			}
		}
	}

	private void onPlayerLeavePort(Player player, Port port) {
		Ports.getInstance().getTicketManager().removeTicket(player);
		player.sendMessage(ChatColor.AQUA + "Please come again soon!");
	}

}
