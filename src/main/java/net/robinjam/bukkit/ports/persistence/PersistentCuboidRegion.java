package net.robinjam.bukkit.ports.persistence;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.CuboidRegion;

/**
 * Handles serializing and deserializing WorldEdit cuboid regions to/from configuration files.
 * 
 * @author robinjam
 */
public class PersistentCuboidRegion implements ConfigurationSerializable {
	
	private CuboidRegion region;
	
	public PersistentCuboidRegion(CuboidRegion region) {
		this.region = region;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("x1", region.getPos1().getX());
		result.put("y1", region.getPos1().getY());
		result.put("z1", region.getPos1().getZ());
		result.put("x2", region.getPos2().getX());
		result.put("y2", region.getPos2().getY());
		result.put("z2", region.getPos2().getZ());
		return result;
	}
	
	public static PersistentCuboidRegion deserialize(Map<String, Object> data) {
		double x1 = (Double) data.get("x1");
		double y1 = (Double) data.get("y1");
		double z1 = (Double) data.get("z1");
		double x2 = (Double) data.get("x2");
		double y2 = (Double) data.get("y2");
		double z2 = (Double) data.get("z2");
		Vector pos1 = new Vector(x1, y1, z1);
		Vector pos2 = new Vector(x2, y2, z2);
		return new PersistentCuboidRegion(new CuboidRegion(pos1, pos2));
	}
	
	public CuboidRegion getRegion() {
		return region;
	}

}
