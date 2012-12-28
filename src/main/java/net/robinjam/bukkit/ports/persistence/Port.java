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
		
		Ports plugin = Ports.getInstance();
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
		File file = new File(Ports.getInstance().getDataFolder(), "ports.yml");
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
	private Integer departureSchedule;
	private String permission;
	private Integer ticketItemId, ticketDataValue;
	private String worldName;
	private Integer cooldown;
	private Double price;
	
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
	
	public void setDepartureSchedule(Integer departureSchedule) {
		if (departureSchedule!= null && departureSchedule == 0)
			departureSchedule = null;
		this.departureSchedule = departureSchedule;
	}
	
	public String getDescription() {
		return description == null ? "port" : description;
	}
	
	public void setDescription(String description) {
		if ("port".equals(description))
			description = null;
		this.description = description;
	}

	public void setArrivalLocation(Location arrivalLocation) {
		this.arrivalLocation = arrivalLocation;
		World world = arrivalLocation.getWorld();
		if (world != null)
			this.worldName = world.getName();
	}
	
	public String getWorld() {
		return worldName;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("name", getName());
		result.put("world", getWorld());
		if (description != null)
			result.put("description", description);
		result.put("activationRegion", new PersistentCuboidRegion(getActivationRegion()));
		result.put("arrivalLocation", new PersistentLocation(getArrivalLocation()));
		if (getDestination() != null)
			result.put("destination", getDestination().getName());
		if (getDepartureSchedule() != null)
			result.put("departureSchedule", getDepartureSchedule());
		if (getPermission() != null)
			result.put("permission", getPermission());
		if (getTicketItemId() != null)
			result.put("ticketItemId", getTicketItemId());
		if (getTicketDataValue() != null)
			result.put("ticketDataValue", getTicketDataValue());
		if (getCooldown() != null)
			result.put("cooldown", getCooldown());
		if (getPrice() != null)
			result.put("price", getPrice());
		return result;
	}
	
	public static Port deserialize(Map<String, Object> data) {
		Port result = new Port();
		result.setName((String) data.get("name"));
		result.setDescription((String) data.get("description"));
		
		PersistentCuboidRegion activationRegion = (PersistentCuboidRegion) data.get("activationRegion");
		result.setActivationRegion(activationRegion.getRegion());
		
		result.worldName = (String) data.get("world");
		World world = Bukkit.getWorld(result.worldName);
		PersistentLocation arrivalLocation = (PersistentLocation) data.get("arrivalLocation");
		result.setArrivalLocation(arrivalLocation.getLocation(world));
		
		result.destinationName = (String) data.get("destination");
		result.setDepartureSchedule((Integer) data.get("departureSchedule"));
		
		result.setPermission((String) data.get("permission"));
		result.setTicketItemId((Integer) data.get("ticketItemId"));
		result.setTicketDataValue((Integer) data.get("ticketDataValue"));
		result.setCooldown((Integer) data.get("cooldown"));
		result.setPrice((Double) data.get("price"));
		
		return result;
	}
	
	// Boilerplate
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
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
	
	public Integer getDepartureSchedule() {
		return departureSchedule;
	}
	
	public String getPermission() {
		return permission;
	}
	
	public void setPermission(String permission) {
		this.permission = permission;
	}
	
	public Integer getTicketItemId() {
		return ticketItemId;
	}
	
	public void setTicketItemId(Integer ticketItemId) {
		this.ticketItemId = ticketItemId;
	}
	
	public Integer getTicketDataValue() {
		return ticketDataValue;
	}
	
	public void setTicketDataValue(Integer ticketDataValue) {
		this.ticketDataValue = ticketDataValue;
	}
	
	public Integer getCooldown() {
		return cooldown;
	}
	
	public void setCooldown(Integer cooldown) {
		this.cooldown = cooldown;
	}
	
	public Double getPrice() {
		return price;
	}
	
	public void setPrice(Double price) {
		this.price = price;
	}

}
