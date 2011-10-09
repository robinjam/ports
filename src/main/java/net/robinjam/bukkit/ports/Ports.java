package net.robinjam.bukkit.ports;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import net.robinjam.bukkit.ports.commands.ReloadCommand;
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
    private WorldEdit worldEdit;
    private static final Logger logger = Logger.getLogger("Minecraft");

    public void onEnable() {
        // Read plugin description file
        pdf = this.getDescription();
        
        // Load the configuration file
        this.reload();
        
        // Hook into WorldEdit
        this.hookWorldEdit();
        
        // Register events
//      PluginManager pm = getServer().getPluginManager();
//      pm.registerEvent(Event.Type.PLAYER_INTERACT_ENTITY, playerListener, Event.Priority.Normal, this);
        
        // Register commands
        commandManager = new CommandManager();
        this.getCommand("ports").setExecutor(commandManager);
        commandManager.registerCommand("reload", new ReloadCommand(this));
        
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
        this.worldEdit = ((WorldEditPlugin)plugin).getWorldEdit();
    }
    
}
