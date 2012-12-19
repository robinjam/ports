package net.robinjam.bukkit.ports.persistence;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.CuboidRegion;

/**
 * 
 * @author robinjam
 */
public class Port {
	
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
	}
	
	public static void save() {
		System.out.println("TODO: Save port data here");
	}
	
	private String name, description = "port";
	private CuboidRegion activationRegion;
	private Location arrivalLocation;
	private WeakReference<Port> destination = new WeakReference<Port>(null);
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
