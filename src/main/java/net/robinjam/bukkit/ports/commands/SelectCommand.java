package net.robinjam.bukkit.ports.commands;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import net.robinjam.bukkit.ports.Ports;
import net.robinjam.bukkit.ports.persistence.Port;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author robinjam
 */
public class SelectCommand implements CommandExecutor {
    
    private final Ports plugin;

    public SelectCommand(final Ports plugin) {
        this.plugin = plugin;
    }
        
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(ChatColor.YELLOW + String.format("Usage: /%s select [name]", label));
        } else if (!sender.hasPermission("ports.select")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission.");
        } else if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players may do that.");
        } else {
            Player player = (Player) sender;
            String name = args[1];
            Port port = plugin.getDatabase().find(Port.class).where("name = :name").setParameter("name", name).findUnique();
            
            if (port == null) {
                sender.sendMessage(ChatColor.RED + "There is no such port to select.");
            } else if (!player.getWorld().getName().equals(port.getWorld())) {
                sender.sendMessage(ChatColor.RED + "That port is in a different world ('" + port.getWorld() + "').");
            } else {
                CuboidSelection selection = new CuboidSelection(player.getWorld(), port.getActivationRegion().getPos1(), port.getActivationRegion().getPos2());
                plugin.getWorldEditPlugin().setSelection(player, selection);
                sender.sendMessage(ChatColor.AQUA + "Activation region selected.");
            }
        }
        
        return true;
    }
    
}
