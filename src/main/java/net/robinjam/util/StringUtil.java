package net.robinjam.util;

import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author robinjam
 */
public class StringUtil {
    
    public static String join(Collection<String> strings, String separator) {
        StringBuilder result = new StringBuilder();
        Iterator it = strings.iterator();
        while(it.hasNext()) {
            result.append(it.next());
            if (!it.hasNext())
                break;
            result.append(separator);
        }
        return result.toString();
    }
    
}
