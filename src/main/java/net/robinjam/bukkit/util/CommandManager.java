package net.robinjam.bukkit.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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

	// Maps command names to their executors
	private Map<String, CommandExecutor> commands = new HashMap<String, CommandExecutor>();

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
		if (args.length != 0) {
			// Check if the command is registered
			if (this.commands.containsKey(args[0])) {
				runCommand(sender, commands.get(args[0]), Arrays.asList(args).subList(1, args.length));
				return true;
			}

			sender.sendMessage(ChatColor.RED + "Invalid command!");
		}

		sender.sendMessage(ChatColor.YELLOW + String.format("Usage: /%s [%s]", label, StringUtils.join(commands.keySet(), "|")));

		return true;
	}

	/**
	 * Registers all of the given CommandExecutors. They must be annotated with
	 * the <code>@Command</code> annotation or an exception will be thrown.
	 * 
	 * @param executors
	 *            the array of CommandExecutors to register
	 */
	public void registerCommands(CommandExecutor[] executors) {
		for (CommandExecutor executor : executors) {
			String name = executor.getClass().getAnnotation(Command.class).name();
			this.commands.put(name, executor);
		}
	}

	/**
	 * Checks that the sender has permission to use the command, ensures that
	 * the parameters are valid, and then executes the command.
	 * 
	 * @param sender
	 *            The source of the command.
	 * @param executor
	 *            The command executor for the command to execute.
	 * @param args
	 *            The arguments to pass to the command executor.
	 */
	private void runCommand(CommandSender sender, CommandExecutor executor, List<String> args) {
		Command annotation = executor.getClass().getAnnotation(Command.class);

		// Ensure that the sender has permission to use the command
		if (!sender.hasPermission(annotation.permissions())) {
			sender.sendMessage(ChatColor.RED + "You do not have permission.");
		}

		// If the command is only usable by players, ensure the sender is a player
		else if (annotation.playerOnly() && !(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players may do that.");
		}

		// Ensure the correct number of arguments is given, and print a usage
		// message otherwise
		else if (args.size() < annotation.min() || (annotation.max() != -1 && args.size() > annotation.max())) {
			sender.sendMessage(ChatColor.YELLOW + "Usage: /port " + annotation.name() + " " + annotation.usage());
		}

		// If everything is correct, execute the command
		else
			executor.onCommand(sender, args);
	}

}
