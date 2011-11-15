package net.robinjam.bukkit.ports.persistence;

import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.CuboidRegion;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import net.robinjam.bukkit.ports.Ports;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 *
 * @author robinjam
 */
@Entity()
public class Port implements Serializable {
    
    private static Ports plugin;
    
    @Id
    private int id;
    
    @Version
    private int version;
    
    @NotEmpty
    private String name;
    
    @NotEmpty
    private String description = "port";
    
    // Activation region
    @NotNull
    private double x1, y1, z1, x2, y2, z2;
    
    // Arrival location
    @NotNull
    private String world;
    @NotNull
    private double x, y, z;
    @NotNull
    private float yaw, pitch;
    
    private int destinationId;
    
    private int departureSchedule;
    
    public static void setPlugin(Ports plugin) {
        Port.plugin = plugin;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public CuboidRegion getActivationRegion() {
        Vector v1 = new Vector(getX1(), getY1(), getZ1());
        Vector v2 = new Vector(getX2(), getY2(), getZ2());
        return new CuboidRegion(v1, v2);
    }
    
    public Location getArrivalLocation() {
        return new Location(Bukkit.getWorld(getWorld()), getX(), getY(), getZ(), getYaw(), getPitch());
    }
    
    public Port getDestination() {
        return plugin.getDatabase().find(Port.class).where().eq("id", destinationId).findUnique();
    }
    
    public int getDepartureSchedule() {
        return departureSchedule;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setActivationRegion(CuboidRegion activationRegion) {
        Vector v1 = activationRegion.getPos1();
        Vector v2 = activationRegion.getPos2();
        setX1(v1.getX());
        setY1(v1.getY());
        setZ1(v1.getZ());
        setX2(v2.getX());
        setY2(v2.getY());
        setZ2(v2.getZ());
    }
    
    public void setArrivalLocation(Location arrivalLocation) {
        setWorld(arrivalLocation.getWorld().getName());
        setX(arrivalLocation.getX());
        setY(arrivalLocation.getY());
        setZ(arrivalLocation.getZ());
        setYaw(arrivalLocation.getYaw());
        setPitch(arrivalLocation.getPitch());
    }
    
    public void setDestination(Port destination) {
        if (destination == null)
            setDestinationId(0);
        else
            setDestinationId(destination.getId());
    }
    
    public void setDepartureSchedule(int departureSchedule) {
        this.departureSchedule = departureSchedule;
    }
    

    public double getX1() {
        return x1;
    }

    public void setX1(double x1) {
        this.x1 = x1;
    }

    public double getY1() {
        return y1;
    }

    public void setY1(double y1) {
        this.y1 = y1;
    }

    public double getZ1() {
        return z1;
    }

    public void setZ1(double z1) {
        this.z1 = z1;
    }

    public double getX2() {
        return x2;
    }

    public void setX2(double x2) {
        this.x2 = x2;
    }

    public double getY2() {
        return y2;
    }

    public void setY2(double y2) {
        this.y2 = y2;
    }

    public double getZ2() {
        return z2;
    }

    public void setZ2(double z2) {
        this.z2 = z2;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public int getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(int destinationId) {
        this.destinationId = destinationId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public int getVersion() {
        return version;
    }
    
    public void setVersion(int version) {
        this.version = version;
    }
    

    public boolean contains(Location location) {
        Vector pt = new Vector(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        return getActivationRegion().contains(pt);
    }
    
}
