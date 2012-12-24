package net.robinjam.bukkit.ports;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.robinjam.bukkit.ports.persistence.Port;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.ItemStack;

public class PortTickTask implements Runnable, Listener {
	
	private Map<Player, Port> playerLocations = new HashMap<Player, Port>();
	private Set<Player> authorizedPlayers = new HashSet<Player>();
	private long tickNumber = 1;

	@Override
	public void run() {
		Ports plugin = Ports.getInstance();
		long notifyTickPeriod = plugin.getConfig().getLong("notify-tick-period");
		
		// Iterate over every player on the server
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			// Check if the player was standing in a port's activation zone on the last port tick
			if (playerLocations.keySet().contains(player)) {
				// Check if the player is still standing in the port's activation zone
				Port port = playerLocations.get(player);
				if (port.contains(player.getLocation())) {
					if (authorizedPlayers.contains(player)) {
						// If it's time for the port to depart, teleport the player
						if (portShouldDepart(port)) {
							teleportPlayer(player, port);
							playerLocations.remove(player);
							authorizedPlayers.remove(player);
						}
						// Otherwise, notify the player when the next departure will be if the notification period has elapsed
						else if (tickNumber == notifyTickPeriod) {
							player.sendMessage(plugin.translate("port-tick-task.notify", port.getDescription(), formatNextDeparture(port)));
						}
					}
				}
				// If the player has left the activation zone, bid them farewell and remove them from the player location tracker
				else {
					playerLocations.remove(player);
					authorizedPlayers.remove(player);
					player.sendMessage(plugin.translate("port-tick-task.leave"));
				}
			}
			// The player was not standing in a port's activation zone on the last port tick
			else {
				// Check if the player is now standing in a port's activation zone
				for (Port port : Port.getAll()) {
					if (port.contains(player.getLocation())) {
						// If the player is currently in a vehicle, eject them (otherwise the teleport will be unsuccessful)
						if (player.getVehicle() != null)
							player.getVehicle().eject();
						
						if (playerCanUsePort(player, port)) {
							// If the port is an insta-port, teleport the player immediately
							if (port.getDepartureSchedule() == null) {
								teleportPlayer(player, port);
								break;
							}
							// Otherwise, add them to the player location tracker
							else {
								playerLocations.put(player, port);
								authorizedPlayers.add(player);
								player.sendMessage(plugin.translate("port-tick-task.enter", port.getDescription(), formatNextDeparture(port)));
								break;
							}
						} else {
							playerLocations.put(player, port);
							authorizedPlayers.remove(player);
						}
					}
				}
			}
		}
		
		// Increment the tick counter
		if (tickNumber == notifyTickPeriod)
			tickNumber = 1;
		else
			++tickNumber;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onVehicleEnter(VehicleEnterEvent event) {
		if (event.isCancelled() || !(event.getEntered() instanceof Player))
			return;
		
		playerLocations.remove((Player) event.getEntered());
	}
	
	private String formatNextDeparture(Port port) {
		long t = port.getDepartureSchedule() - Bukkit.getWorld(port.getWorld()).getFullTime() % port.getDepartureSchedule();
		long d = t / 24000;
		long h = (t % 24000) / 1000;
		long m = ((t % 24000) % 1000) / 16;
		return String.format("[%dd %dh %dm]", d, h, m);
	}
	
	private boolean portShouldDepart(Port port) {
		long time = Bukkit.getWorld(port.getWorld()).getFullTime();
		long prevTime = time - Ports.getInstance().getConfig().getLong("port-tick-period");
		return time / port.getDepartureSchedule() != prevTime / port.getDepartureSchedule();
	}
	
	private boolean playerCanUsePort(Player player, Port port) {
		Ports plugin = Ports.getInstance();
		
		// Ensure the port has a destination
		if (port.getDestination() == null) {
			player.sendMessage(plugin.translate("port-tick-task.no-destination", port.getDescription()));
			return false;
		}
		
		// Ensure the player has permission to use the port
		if (port.getPermission() != null && !player.hasPermission(port.getPermission())) {
			player.sendMessage(plugin.translate("port-tick-task.no-permission", port.getDescription()));
			return false;
		}
		
		// Ensure the player is carrying the correct ticket (if required)
		Integer ticketItemId = port.getTicketItemId();
		Integer ticketDataValue = port.getTicketDataValue();
		ItemStack heldItem = player.getItemInHand();
		byte heldData = heldItem.getData().getData();
		if (ticketItemId != null) {
			if (heldItem.getTypeId() != ticketItemId || (ticketDataValue != null && heldData != ticketDataValue)) {
				player.sendMessage(plugin.translate("port-tick-task.no-ticket", port.getDescription()));
				return false;
			}
			
			// Remove the ticket from the player's hand
			int heldItemAmount = player.getItemInHand().getAmount();
			if (heldItemAmount == 1)
				player.setItemInHand(null);
			else
				player.getItemInHand().setAmount(heldItemAmount - 1);
			player.sendMessage(plugin.translate("port-tick-task.ticket-taken"));
		}
		
		return true;
	}
	
	private void teleportPlayer(Player player, Port port) {
		player.teleport(port.getDestination().getArrivalLocation());

		// Reset the player's fall distance so they don't take fall damage
		player.setFallDistance(0.0f);

		// Refresh the chunk to prevent chunk errors
		World world = player.getWorld();
		Chunk chunk = world.getChunkAt(player.getLocation());
		world.refreshChunk(chunk.getX(), chunk.getZ());
		player.sendMessage(Ports.getInstance().translate("port-tick-task.depart"));
	}

}
