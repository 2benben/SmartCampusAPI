package uk.ac.westminster;

import uk.ac.westminster.models.Room;
import uk.ac.westminster.models.Sensor;
import java.util.HashMap;
import java.util.Map;

public class DataStore {
    public static Map<String, Room> rooms = new HashMap<>();
    public static Map<String, Sensor> sensors = new HashMap<>();

    static {
        rooms.put("LIB-01", new Room("LIB-01", "Library North", 50));
    }
}