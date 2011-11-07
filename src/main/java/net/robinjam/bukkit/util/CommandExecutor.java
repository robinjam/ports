package net.robinjam.bukkit.util;

import java.util.List;
import org.bukkit.command.CommandSender;

/**
 *
 * @author robinjam
 */
public interface CommandExecutor {
    
    public void onCommand(CommandSender sender, List<String> args);
    
}
