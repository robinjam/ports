package net.robinjam.bukkit.ports.persistence;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.robinjam.bukkit.ports.Ports;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.CuboidRegion;

/**
 * 
 * @author robinjam
 */
public class Port implements ConfigurationSerializable {
	
	private static Set<Port> ports = new HashSet<Port>();
	
	public static Port get(String name) {
		for (Port port : ports) {
			if (port.getName().equals(name))
				return port;
		}
		
		return null;
	}
	
	public static Set<Port> getAll() {
		return ports;
	}
	
	public static void remove(Port port) {
		ports.remove(port);
		save();
	}
	
	public static void save() {
		YamlConfiguration config = new YamlConfiguration();
		config.set("ports", new ArrayList<Port>(ports));
		
		Ports plugin = (Ports) Bukkit.getPluginManager().getPlugin("Ports");
		File file = new File(plugin.getDataFolder(), "ports.yml");
		try {
			config.save(file);
		} catch (IOException e) {
			plugin.getLogger().severe("Unable to save " + file + "!");
			plugin.getLogger().severe(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void load() {
		// Load the ports.yml file from the plugin's data folder
		Ports plugin = (Ports) Bukkit.getPluginManager().getPlugin("Ports");
		File file = new File(plugin.getDataFolder(), "ports.yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		// Load the port data
		Object data = config.get("ports");
		if (data != null)
			ports = new HashSet<Port>((List<Port>) data);
		
		// Iterate over the newly loaded ports and set up destinations accordingly.
		for (Port port : ports) {
			if (port.destinationName != null) {
				for (Port p : ports) {
					if (p.getName().equals(port.destinationName)) {
						port.setDestination(p);
						port.destinationName = null;
					}
				}
			}
		}
	}
	
	private String name, description = "port";
	private CuboidRegion activationRegion;
	private Location arrivalLocation;
	private WeakReference<Port> destination = new WeakReference<Port>(null);
	private String destinationName;
	private int departureSchedule;
	
	public Port()
	{
		ports.add(this);
	}
	
	public boolean contains(Location location) {
		Vector vec = new Vector(location.getBlockX(), location.getBlockY(), location.getBlockZ());
		return activationRegion.contains(vec);
	}
	
	public Port getDestination() {
		Port destination = this.destination.get();
		return ports.contains(destination) ? destination : null;
	}
	
	public void setDestination(Port destination) {
		this.destination = new WeakReference<Port>(destination);
	}
	
	public String getWorld() {
		return getArrivalLocation().getWorld().getName();
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("name", getName());
		result.put("world", getWorld());
		result.put("description", getDescription());
		result.put("activationRegion", new PersistentCuboidRegion(getActivationRegion()));
		result.put("arrivalLocation", new PersistentLocation(getArrivalLocation()));
		if (getDestination() != null)
			result.put("destination", getDestination().getName());
		result.put("departureSchedule", getDepartureSchedule());
		return result;
	}
	
	public static Port deserialize(Map<String, Object> data) {
		Port result = new Port();
		result.setName((String) data.get("name"));
		result.setDescription((String) data.get("description"));
		
		PersistentCuboidRegion activationRegion = (PersistentCuboidRegion) data.get("activationRegion");
		result.setActivationRegion(activationRegion.getRegion());
		
		World world = Bukkit.getWorld((String) data.get("world"));
		PersistentLocation arrivalLocation = (PersistentLocation) data.get("arrivalLocation");
		result.setArrivalLocation(arrivalLocation.getLocation(world));
		
		result.destinationName = (String) data.get("destination");
		result.setDepartureSchedule((Integer) data.get("departureSchedule"));
		
		return result;
	}
	
	// Boilerplate
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public CuboidRegion getActivationRegion() {
		return activationRegion;
	}
	
	public void setActivationRegion(CuboidRegion activationRegion) {
		this.activationRegion = activationRegion;
	}
	
	public Location getArrivalLocation() {
		return arrivalLocation;
	}
	
	public void setArrivalLocation(Location arrivalLocation) {
		this.arrivalLocation = arrivalLocation;
	}
	
	public int getDepartureSchedule() {
		return departureSchedule;
	}
	
	public void setDepartureSchedule(int departureSchedule) {
		this.departureSchedule = departureSchedule;
	}

}
