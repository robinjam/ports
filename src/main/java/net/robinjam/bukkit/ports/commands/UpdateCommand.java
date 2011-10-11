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
public class UpdateCommand implements CommandExecutor {
    
    private final Ports plugin;

    public UpdateCommand(final Ports plugin) {
        this.plugin = plugin;
    }
        
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(ChatColor.YELLOW + String.format("Usage: /%s update [name]", label));
        } else if (!sender.hasPermission("ports.update")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission.");
        } else if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players may do that.");
        } else {
            Player player = (Player) sender;
            String name = args[1];
            Port port = plugin.getDatabase().find(Port.class).where("name = :name").setParameter("name", name).findUnique();
            
            if (port == null) {
                sender.sendMessage(ChatColor.RED + "There is no such port.");
            } else if (!player.getWorld().getName().equals(port.getWorld())) {
                sender.sendMessage(ChatColor.RED + "That port is in a different world ('" + port.getWorld() + "').");
            } else {
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
                
                port.setActivationRegion((CuboidRegion) selection);
                plugin.getDatabase().update(port);
                sender.sendMessage(ChatColor.AQUA + "Activation region updated.");
            }
        }
        
        return true;
    }
    
}
