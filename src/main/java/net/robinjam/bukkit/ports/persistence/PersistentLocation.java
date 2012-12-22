package net.robinjam.bukkit.ports.persistence;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

/**
 * Handles serializing and deserializing Bukkit Location objects to/from configuration files.
 * 
 * @author robinjam
 */
public class PersistentLocation implements ConfigurationSerializable {
	
	private double x, y, z;
	private float pitch, yaw;
	
	public PersistentLocation(Location location) {
		this(location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw());
	}
	
	private PersistentLocation(double x, double y, double z, float pitch, float yaw) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.pitch = pitch;
		this.yaw = yaw;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("x", x);
		result.put("y", y);
		result.put("z", z);
		result.put("pitch", pitch);
		result.put("yaw", yaw);
		return result;
	}
	
	public static PersistentLocation deserialize(Map<String, Object> data) {
		double x = (Double) data.get("x");
		double y = (Double) data.get("y");
		double z = (Double) data.get("z");
		double pitch = (Double) data.get("pitch");
		double yaw = (Double) data.get("yaw");
		return new PersistentLocation(x, y, z, (float) pitch, (float) yaw);
	}
	
	public Location getLocation(World world) {
		return new Location(world, x, y, z, yaw, pitch);
	}
	
}
