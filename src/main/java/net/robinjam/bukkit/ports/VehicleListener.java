package net.robinjam.bukkit.ports;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;

/**
 * 
 * @author robinjam
 */
class VehicleListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void onVehicleEnter(VehicleEnterEvent event) {
		if (event.isCancelled() || !(event.getEntered() instanceof Player))
			return;

		Player player = (Player) event.getEntered();

		Ports.getInstance().getTicketManager().removeTicket(player);
	}

}
