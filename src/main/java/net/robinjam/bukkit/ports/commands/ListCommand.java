package net.robinjam.bukkit.ports.commands;

import java.util.ArrayList;
import java.util.List;
import net.robinjam.bukkit.ports.Ports;
import net.robinjam.bukkit.ports.persistence.Port;
import net.robinjam.util.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author robinjam
 */
public class ListCommand implements CommandExecutor {
    
    private final Ports plugin;

    public ListCommand(final Ports plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.YELLOW + String.format("Usage: /%s list", label));
        } else if (!sender.hasPermission("ports.list")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission.");
        } else {
            List<String> portNames = new ArrayList();
            for (Port port : plugin.getDatabase().find(Port.class).findList()) {
                portNames.add(port.getName());
            }
            if (portNames.size() > 0) {
                sender.sendMessage(ChatColor.YELLOW + "Available ports: " + StringUtil.join(portNames, ", "));
            } else {
                sender.sendMessage(ChatColor.YELLOW + "There are no ports defined yet.");
            }
        }
        
        return true;
    }
    
}
