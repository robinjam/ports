package net.robinjam.bukkit.ports;

import java.io.IOException;

import net.robinjam.bukkit.ports.commands.*;
import net.robinjam.bukkit.ports.persistence.PersistentCuboidRegion;
import net.robinjam.bukkit.ports.persistence.PersistentLocation;
import net.robinjam.bukkit.ports.persistence.Port;
import net.robinjam.bukkit.util.CommandExecutor;
import net.robinjam.bukkit.util.CommandManager;

import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

/**
 * The main plugin class, and translation provider for all other classes.
 * 
 * @author robinjam
 */
public class Ports extends JavaPlugin {

	// The singleton instance
	private static Ports instance;

	/**
	 * @return The singleton instance.
	 */
	public static Ports getInstance() {
		return instance;
	}

	// Handles dispatching commands to command handlers
	private CommandManager commandManager = new CommandManager();

	// Handles synchronously processing port ticks and notifications
	private PortTickTask portTickTask = new PortTickTask();

	/**
	 * Constructs a new instance and sets the singleton instance of this plugin
	 * to the newly created instance.
	 */
	public Ports() {
		commandManager.registerCommands(new CommandExecutor[] {
			new ArriveCommand(),
			new CooldownCommand(),
			new CreateCommand(),
			new DeleteCommand(),
			new DescribeCommand(),
			new DestinationCommand(),
			new LinkCommand(),
			new ListCommand(),
			new PermissionCommand(),
			new ReloadCommand(),
			new RenameCommand(),
			new ScheduleCommand(),
			new SelectCommand(),
			new TicketCommand(),
			new UnlinkCommand(),
			new UpdateCommand()
		});

		instance = this;
	}

	@Override
	public void onEnable() {
		// Register events and commands
		getServer().getPluginManager().registerEvents(portTickTask, this);
		this.getCommand("port").setExecutor(commandManager);

		// Load port data
		ConfigurationSerialization.registerClass(Port.class);
		ConfigurationSerialization.registerClass(PersistentLocation.class);
		ConfigurationSerialization.registerClass(PersistentCuboidRegion.class);
		Port.load();
		
		// Save port data so that when the schema is changed the port data migrates automatically
		Port.save();

		// Load config
		getConfig().options().copyDefaults(true);
		saveConfig();

		// Enable plugin metrics
		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
			getLogger().warning("Unable to start plugin metrics.");
		}

		// Schedule ticket manager
		getServer().getScheduler().scheduleSyncRepeatingTask(this, portTickTask, 0L, getConfig().getLong("port-tick-period"));
	}

	@Override
	public void onDisable() {
		// Cancel all scheduled tasks for this plugin
		getServer().getScheduler().cancelTasks(this);
	}

	/**
	 * Translates the string by loading the string from config path
	 * "translations.[path]". The translated string is then formatted using the
	 * given objects. Colour codes beginning with the '&' symbol are also
	 * translated to the format Minecraft expects.
	 * 
	 * @param path
	 *            The localization key.
	 * @param objs
	 *            A variable number of objects to be passed to the string
	 *            formatter.
	 * @return The translated, formatted string with colour codes expanded.
	 */
	public String translate(String path, Object... objs) {
		return ChatColor.translateAlternateColorCodes('&', String.format(getConfig().getString("translations." + path), objs));
	}

}
