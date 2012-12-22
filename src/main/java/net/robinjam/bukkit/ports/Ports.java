package net.robinjam.bukkit.ports;

import net.robinjam.bukkit.ports.commands.ArriveCommand;
import net.robinjam.bukkit.ports.commands.CreateCommand;
import net.robinjam.bukkit.ports.commands.DeleteCommand;
import net.robinjam.bukkit.ports.commands.DescribeCommand;
import net.robinjam.bukkit.ports.commands.DestinationCommand;
import net.robinjam.bukkit.ports.commands.LinkCommand;
import net.robinjam.bukkit.ports.commands.ListCommand;
import net.robinjam.bukkit.ports.commands.ReloadCommand;
import net.robinjam.bukkit.ports.commands.RenameCommand;
import net.robinjam.bukkit.ports.commands.ScheduleCommand;
import net.robinjam.bukkit.ports.commands.SelectCommand;
import net.robinjam.bukkit.ports.commands.UnlinkCommand;
import net.robinjam.bukkit.ports.commands.UpdateCommand;
import net.robinjam.bukkit.ports.persistence.PersistentLocation;
import net.robinjam.bukkit.ports.persistence.PersistentCuboidRegion;
import net.robinjam.bukkit.ports.persistence.Port;
import net.robinjam.bukkit.util.CommandExecutor;
import net.robinjam.bukkit.util.CommandManager;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

/**
 * 
 * @author robinjam
 */
public class Ports extends JavaPlugin {

	private CommandManager commandManager;
	private WorldEditPlugin worldEditPlugin;
	private PlayerListener playerListener = new PlayerListener(this);
	private VehicleListener vehicleListener = new VehicleListener(this);
	private TicketManager ticketManager = new TicketManager(this);

	@Override
	public void onEnable() {
		// Hook into WorldEdit
		this.hookWorldEdit();

		// Register events
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(playerListener, this);
		pm.registerEvents(vehicleListener, this);

		// Register commands
		commandManager = new CommandManager();
		commandManager.registerCommands(new CommandExecutor[] {
				new ArriveCommand(), new CreateCommand(this),
				new DeleteCommand(), new DescribeCommand(),
				new DestinationCommand(), new LinkCommand(), new ListCommand(),
				new ReloadCommand(this), new RenameCommand(),
				new ScheduleCommand(), new SelectCommand(this),
				new UnlinkCommand(), new UpdateCommand(this) });
		this.getCommand("port").setExecutor(commandManager);
		
		// Load port data
		ConfigurationSerialization.registerClass(Port.class);
		ConfigurationSerialization.registerClass(PersistentLocation.class);
		ConfigurationSerialization.registerClass(PersistentCuboidRegion.class);
		Port.load();

		// Schedule ticket manager
		getServer().getScheduler().scheduleSyncRepeatingTask(this,
				ticketManager, 0L, 100L);
	}

	@Override
	public void onDisable() {
		getServer().getScheduler().cancelTasks(this);
	}

	private void hookWorldEdit() {
		Plugin plugin = this.getServer().getPluginManager()
				.getPlugin("WorldEdit");
		this.worldEditPlugin = (WorldEditPlugin) plugin;
	}

	public WorldEditPlugin getWorldEditPlugin() {
		return this.worldEditPlugin;
	}

	public WorldEdit getWorldEdit() {
		return this.worldEditPlugin.getWorldEdit();
	}

	public void teleportPlayer(Player player, Port port) {
		player.teleport(port.getDestination().getArrivalLocation());

		// Reset the player's fall distance so they don't take fall damage
		player.setFallDistance(0.0f);

		// Refresh the chunk to prevent chunk errors
		World world = player.getWorld();
		Chunk chunk = world.getChunkAt(player.getLocation());
		world.refreshChunk(chunk.getX(), chunk.getZ());
		player.sendMessage(ChatColor.AQUA + "Whoosh!");
	}

	public TicketManager getTicketManager() {
		return ticketManager;
	}

}
