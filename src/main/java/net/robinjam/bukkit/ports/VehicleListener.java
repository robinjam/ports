package net.robinjam.bukkit.ports;

import org.bukkit.entity.Player;
import org.bukkit.event.vehicle.VehicleEnterEvent;

/**
 *
 * @author robinjam
 */
class VehicleListener extends org.bukkit.event.vehicle.VehicleListener {
    
    private Ports plugin;
    
    public VehicleListener(Ports plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onVehicleEnter(VehicleEnterEvent event) {
        if (event.isCancelled() || !(event.getEntered() instanceof Player))
            return;
        
        Player player = (Player) event.getEntered();
        
        plugin.getTicketManager().removeTicket(player);
    }
    
}
