package com.airline.airecommendation.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ImpactEventPayload {

    private UUID          id;
    private UUID          disruptionId;
    private String        flightNumber;
    private String        origin;
    private String        destination;
    private String        disruptionType;
    private int           affectedPassengers;
    private int           affectedFlights;
    private int           totalDelayMinutes;
    private String        severity;
    private LocalDateTime calculatedAt;

    public ImpactEventPayload() {}

    public UUID getId()                        { return id; }
    public void setId(UUID v)                  { this.id = v; }
    public UUID getDisruptionId()              { return disruptionId; }
    public void setDisruptionId(UUID v)        { this.disruptionId = v; }
    public String getFlightNumber()            { return flightNumber; }
    public void setFlightNumber(String v)      { this.flightNumber = v; }
    public String getOrigin()                  { return origin; }
    public void setOrigin(String v)            { this.origin = v; }
    public String getDestination()             { return destination; }
    public void setDestination(String v)       { this.destination = v; }
    public String getDisruptionType()          { return disruptionType; }
    public void setDisruptionType(String v)    { this.disruptionType = v; }
    public int getAffectedPassengers()         { return affectedPassengers; }
    public void setAffectedPassengers(int v)   { this.affectedPassengers = v; }
    public int getAffectedFlights()            { return affectedFlights; }
    public void setAffectedFlights(int v)      { this.affectedFlights = v; }
    public int getTotalDelayMinutes()          { return totalDelayMinutes; }
    public void setTotalDelayMinutes(int v)    { this.totalDelayMinutes = v; }
    public String getSeverity()                { return severity; }
    public void setSeverity(String v)          { this.severity = v; }
    public LocalDateTime getCalculatedAt()     { return calculatedAt; }
    public void setCalculatedAt(LocalDateTime v) { this.calculatedAt = v; }
}
