package net.robinjam.bukkit.ports;

import java.util.HashMap;
import java.util.Map;
import net.robinjam.bukkit.ports.persistence.Port;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 *
 * @author robinjam
 */
public class TicketManager implements Runnable {
    
    Ports plugin;
    Map<Player, Port> tickets = new HashMap();
    Map<World, Long> previousTimes = new HashMap();
    Map<World, Long> portsTimes = new HashMap();
    
    public TicketManager(Ports plugin) {
        this.plugin = plugin;
    }
    
    public void addTicket(Player player, Port port) {
        tickets.put(player, port);
    }
    
    public void removeTicket(Player player) {
        tickets.remove(player);
    }

    public void run() {
        for (Map.Entry<Player, Port> ticket : tickets.entrySet()) {
            Player player = ticket.getKey();
            Port port = ticket.getValue();
            
            World world = Bukkit.getWorld(port.getWorld());
            long time = world.getTime();
            
            if (portsTimes.get(world) == null) {
                portsTimes.put(world, time);
            }
            
            if (previousTimes.get(world) == null) {
                previousTimes.put(world, time);
            }
            
            long previousTime = portsTimes.get(world);
            long newTime = previousTime + time - previousTimes.get(world);
            
            portsTimes.put(world, newTime);
            previousTimes.put(world, time);
            
            boolean teleported = false;
            for (long x = previousTime; x <= newTime; x++) {
                if (x % port.getDispatchSchedule() == 0) {
                    plugin.teleportPlayer(player, port);
                    teleported = true;
                }
            }
            
            if (!teleported)
                player.sendMessage(ChatColor.AQUA + "This " + port.getDescription() + " will depart in " + (port.getDispatchSchedule() - newTime % port.getDispatchSchedule()));
        }
    }
    
}
