package net.robinjam.bukkit.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author robinjam
 */
public class Configuration extends org.bukkit.util.config.Configuration {
    
    public Configuration(final File configFile) throws IOException {
        super(configFile);
        
        // If the configuration file is missing, copy the one inside the JAR
        if(!configFile.exists()) {
            InputStream inputStream = Configuration.class.getResourceAsStream("/config.yml");
            FileOutputStream outputStream = new FileOutputStream(configFile);
            
            while (inputStream.available() > 0) {
                outputStream.write(inputStream.read());
            }
            
            outputStream.close();
            inputStream.close();
        }
        
        this.load();
    }
    
}
