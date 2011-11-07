package net.robinjam.bukkit.ports.commands;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import java.util.List;
import net.robinjam.bukkit.ports.Ports;
import net.robinjam.bukkit.ports.persistence.Port;
import net.robinjam.bukkit.util.Command;
import net.robinjam.bukkit.util.CommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Handles the /port select command.
 * 
 * @author robinjam
 */
@Command(name = "select",
         usage = "[name]",
         permissions = "ports.select",
         playerOnly = true,
         min = 1, max = 1)
public class SelectCommand implements CommandExecutor {
    
    private final Ports plugin;

    public SelectCommand(final Ports plugin) {
        this.plugin = plugin;
    }
        
    public void onCommand(CommandSender sender, List<String> args) {
        Player player = (Player) sender;
        Port port = Port.get(args.get(0));

        if (port == null) {
            sender.sendMessage(ChatColor.RED + "There is no such port.");
        }
        else if (!player.getWorld().getName().equals(port.getWorld())) {
            sender.sendMessage(ChatColor.RED + "That port is in a different world ('" + port.getWorld() + "').");
        }
        else {
            CuboidSelection selection = new CuboidSelection(player.getWorld(), port.getActivationRegion().getPos1(), port.getActivationRegion().getPos2());
            plugin.getWorldEditPlugin().setSelection(player, selection);
            sender.sendMessage(ChatColor.AQUA + "Activation region selected.");
        }
    }
    
}
