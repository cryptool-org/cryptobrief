package ffapl.java.util;

import java.util.HashMap;
import java.util.Map;

/**
 * A restricted version of a HashMap, which only allows to append new key-value pairs, but does not allow to modify or delete existing ones
 *
 * @param <K> - the type of the Key of the restricted HashMap
 * @param <V> - the type of the Value of the restricted HashMap
 */
public class RestrictedHashMap<K,V> {
	private Map<K,V> map;
	
	/**
	 * Constructor
	 */
	public RestrictedHashMap() {
		map = new HashMap<K,V>();
	}
	
	/**
	 * Only performs the put operation, if the map does not contain the key already.
	 * @param key key to put into the map
	 * @param value value to put into the map
	 * @return true if the put operation succeeded, otherwise false
	 * 
	 */
	public boolean put(K key, V value) {
		if (map.containsKey(key)) {
			return false;
		} else {					
			map.put(key, value);
			return true;
		}
	}
	
	/**
	 * Corresponds to the get method of a HashMap
	 * @param key the key of the corresponding entry
	 * @return if the key exists, the value of the corresponding entry is returned, otherwise false
	 */
	public V get(K key) {
		return map.get(key);
	}
	
	/**
	 * Corresponds to the containsKey method of a HashMap
	 * @param key the key to search for
	 * @return true if the map contains key, false otherwise
	 */
	public boolean containsKey(String key) {
		return map.containsKey(key);
	}
}

