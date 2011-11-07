package net.robinjam.bukkit.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.robinjam.util.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Manages a set of CommandExecutors and delegates commands to them, handling
 * permissions and usage restrictions automatically.
 * 
 * @author robinjam
 */
public class CommandManager implements org.bukkit.command.CommandExecutor {
    
    private Map<String, CommandExecutor> commands; // Maps command names to their executors
    
    public CommandManager() {
        commands = new HashMap();
    }

    public boolean onCommand(final CommandSender sender, final org.bukkit.command.Command command, final String label, final String[] args) {
        if (args.length != 0) {
            if (this.commands.containsKey(args[0])) {
                runCommand(sender, commands.get(args[0]), Arrays.asList(args).subList(1, args.length));
                return true;
            }
            
            sender.sendMessage(ChatColor.RED + "Invalid command!");
        }
        
        sender.sendMessage(ChatColor.YELLOW + String.format("Usage: /%s [%s]", label, StringUtil.join(commands.keySet(), "|")));
        
        return true;
    }
    
    /**
     * Registers all of the given CommandExecutors. They must be annotated with
     * the <code>@Command</code> annotation or an exception will be thrown.
     * 
     * @param executors the array of CommandExecutors to register
     */
    public void registerCommands(CommandExecutor[] executors) {
        for (CommandExecutor executor : executors) {
            String name = executor.getClass().getAnnotation(Command.class).name();
            this.commands.put(name, executor);
        }
    }
    
    private void runCommand(CommandSender sender, CommandExecutor executor, List<String> args) {
        Command annotation = executor.getClass().getAnnotation(Command.class);
        if (!sender.hasPermission(annotation.permissions())) {
            sender.sendMessage(ChatColor.RED + "You do not have permission.");
        }
        else if (annotation.playerOnly() && !(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players may do that.");
        }
        else if (args.size() < annotation.min() || (annotation.max() != -1 && args.size() > annotation.max())) {
            sender.sendMessage(ChatColor.YELLOW + "Usage: /port " + annotation.name() + " " + annotation.usage());
        }
        else {
            executor.onCommand(sender, args);
        }
    }
    
}
