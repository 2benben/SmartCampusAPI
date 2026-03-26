package uk.ac.westminster.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uk.ac.westminster.models.Sensor;
import uk.ac.westminster.models.SensorReading;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SensorReadingResource {
    private Sensor parentSensor;
    // We store the readings in a simple list for this specific sensor
    private static List<SensorReading> readingsHistory = new ArrayList<>();

    public SensorReadingResource(Sensor sensor) {
        this.parentSensor = sensor;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SensorReading> getHistory() {
        return readingsHistory;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addReading(SensorReading reading) {
        // Task 4.2 Side Effect: Update the parent sensor's value automatically
        reading.setId(UUID.randomUUID().toString());
        reading.setTimestamp(System.currentTimeMillis());

        parentSensor.setCurrentValue(reading.getValue());
        readingsHistory.add(reading);

        return Response.status(Response.Status.CREATED).entity(reading).build();
    }
}