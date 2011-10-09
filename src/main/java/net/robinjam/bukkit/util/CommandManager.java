package net.robinjam.bukkit.util;

import java.util.HashMap;
import java.util.Map;
import net.robinjam.util.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author robinjam
 */
public class CommandManager implements CommandExecutor {
    
    private Map<String, CommandExecutor> commands;
    
    public CommandManager() {
        commands = new HashMap();
    }

    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length != 0) {
            if (this.commands.containsKey(args[0])) {
                commands.get(args[0]).onCommand(sender, command, label, args);
                return true;
            }
            
            sender.sendMessage(ChatColor.RED + "Invalid command!");
        }
        
        sender.sendMessage(ChatColor.YELLOW + String.format("Usage: /%s [%s]", label, StringUtil.join(commands.keySet(), "|")));
        
        return true;
    }
    
    public void registerCommand(final String name, final CommandExecutor executor) {
        this.commands.put(name, executor);
    }
    
    public void unregisterCommand(final String name) {
        this.commands.remove(name);
    }
    
}
