package net.robinjam.bukkit.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks the annotated method as a command.
 * 
 * @author robinjam
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Command {
    
    /**
     * The name of the command.
     */
    String name();
    
    /**
     * The text that should be appended to appended to "/port commandname " in
     * order to produce usage instructions. Example:
     * <code>[name] [description]</code>
     */
    String usage() default "";
    
    /**
     * The permission node required to use this command, or the empty string if
     * no permission node is required.
     */
    String permissions() default "";
    
    /**
     * Determines whether only players can perform the command.
     */
    boolean playerOnly() default false;
    
    /**
     * The minimum number of arguments required.
     */
    int min() default 0;
    
    /**
     * The maximum number of arguments acceptable, or -1 if there is no limit.
     */
    int max() default 0;
    
}
