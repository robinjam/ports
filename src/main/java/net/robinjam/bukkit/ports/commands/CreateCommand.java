package net.robinjam.bukkit.ports.commands;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import net.robinjam.bukkit.ports.Ports;
import net.robinjam.bukkit.ports.persistence.Port;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author robinjam
 */
public class CreateCommand implements CommandExecutor {
    
    private final Ports plugin;

    public CreateCommand(final Ports plugin) {
        this.plugin = plugin;
    }
    
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(ChatColor.YELLOW + String.format("Usage: /%s create [name]", label));
        } else if (!sender.hasPermission("ports.create")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission.");
        } else if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players may do that.");
        } else {
            String name = args[1];
            
            if (plugin.getDatabase().find(Port.class).where("name = :name").setParameter("name", name).findRowCount() > 0) {
                sender.sendMessage(ChatColor.RED + "There is already a port with that name. Please pick a unique name.");
                return true;
            }
            
            Player player = (Player) sender;
            WorldEdit worldEdit = plugin.getWorldEdit();
            LocalSession session = worldEdit.getSession(player.getName());
            Region selection;
            try {
                selection = session.getSelection(new BukkitWorld(player.getWorld()));
            } catch (IncompleteRegionException ex) {
                sender.sendMessage(ChatColor.RED + "Please select the activation area using WorldEdit first.");
                return true;
            }
            
            if (!(selection instanceof CuboidRegion)) {
                sender.sendMessage(ChatColor.RED + "Only cuboid regions are supported.");
                return true;
            }
            
            Port port = new Port();
            port.setName(name);
            port.setActivationRegion((CuboidRegion) selection);
            port.setArrivalLocation(player.getLocation());
            
            
            plugin.getDatabase().insert(port);
            
            sender.sendMessage(ChatColor.AQUA + "The port was created successfully.");
        }
        
        return true;
    }
    
}
