package net.robinjam.bukkit.ports;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.PersistenceException;
import net.robinjam.bukkit.ports.commands.ArriveCommand;
import net.robinjam.bukkit.ports.commands.CreateCommand;
import net.robinjam.bukkit.ports.commands.DeleteCommand;
import net.robinjam.bukkit.ports.commands.ListCommand;
import net.robinjam.bukkit.ports.commands.ReloadCommand;
import net.robinjam.bukkit.ports.commands.SelectCommand;
import net.robinjam.bukkit.ports.commands.UpdateCommand;
import net.robinjam.bukkit.ports.persistence.Port;
import net.robinjam.bukkit.util.CommandManager;
import net.robinjam.bukkit.util.Configuration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author robinjam
 */
public class Ports extends JavaPlugin {
    
    private Configuration config;
    private PluginDescriptionFile pdf;
    private CommandManager commandManager;
    private WorldEditPlugin worldEditPlugin;
    private static final Logger logger = Logger.getLogger("Minecraft");

    public void onEnable() {
        // Read plugin description file
        pdf = this.getDescription();
        
        // Load the configuration file
        this.reload();
        
        // Hook into WorldEdit
        this.hookWorldEdit();
        
        // Set up database
        this.setupDatabase();
        
        // Register events
//      PluginManager pm = getServer().getPluginManager();
//      pm.registerEvent(Event.Type.PLAYER_INTERACT_ENTITY, playerListener, Event.Priority.Normal, this);
        
        // Register commands
        commandManager = new CommandManager();
        this.getCommand("port").setExecutor(commandManager);
        commandManager.registerCommand("reload", new ReloadCommand(this));
        commandManager.registerCommand("list", new ListCommand(this));
        commandManager.registerCommand("create", new CreateCommand(this));
        commandManager.registerCommand("delete", new DeleteCommand(this));
        commandManager.registerCommand("select", new SelectCommand(this));
        commandManager.registerCommand("arrive", new ArriveCommand(this));
        commandManager.registerCommand("update", new UpdateCommand(this));
        
        logger.info(String.format("%s version %s is enabled!", pdf.getName(), pdf.getVersion()));
    }
    
    public void onDisable() {
        // Do nothing
    }

    public void reload() {
        File dataFolder = this.getDataFolder();
        
        if(!dataFolder.exists())
            dataFolder.mkdir();
        
        try {
            config = new Configuration(new File(dataFolder, "config.yml"));
        } catch (IOException ex) {
            logger.severe(String.format("[%s] Unable to create default configuration file!", pdf.getName()));
            logger.severe(String.format("[%s] %s", pdf.getName(), ex.getMessage()));
        }
    }
    
    private void hookWorldEdit() {
        Plugin plugin = this.getServer().getPluginManager().getPlugin("WorldEdit");
        this.worldEditPlugin = (WorldEditPlugin) plugin;
    }
    
    public WorldEditPlugin getWorldEditPlugin() {
        return this.worldEditPlugin;
    }
    
    public WorldEdit getWorldEdit() {
        return this.worldEditPlugin.getWorldEdit();
    }
    
    @Override
    public List<Class<?>> getDatabaseClasses() {
        List<Class<?>> list = new ArrayList<Class<?>>();
        list.add(Port.class);
        return list;
    }

    private void setupDatabase() {
        try {
            getDatabase().find(Port.class).findRowCount();
        } catch (PersistenceException ex) {
            logger.info(String.format("[%s] Creating database", pdf.getName()));
            installDDL();
        }
    }
    
}
