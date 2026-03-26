package uk.ac.westminster;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import uk.ac.westminster.resources.DiscoveryResource;
import uk.ac.westminster.resources.RoomResource;
import uk.ac.westminster.resources.SensorResource;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api/v1")
public class SmartCampusApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(DiscoveryResource.class);
        resources.add(RoomResource.class);
        resources.add(SensorResource.class);
        resources.add(uk.ac.westminster.filters.LoggingFilter.class);
        resources.add(uk.ac.westminster.exceptions.GlobalExceptionMapper.class);
        return resources;
    }
}