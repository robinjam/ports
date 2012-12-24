package net.robinjam.bukkit.util;

import java.util.List;

import org.bukkit.command.CommandSender;

/**
 * The interface that all command executors must implement.
 * 
 * @author robinjam
 */
public interface CommandExecutor {

	/**
	 * Called when the command is executed.
	 * 
	 * @param sender
	 *            The source of the command.
	 * @param args
	 *            The list of arguments given. If no arguments are given, the
	 *            list will be empty (not null).
	 */
	public void onCommand(CommandSender sender, List<String> args);

}
