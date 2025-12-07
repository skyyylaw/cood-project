package common;

import java.time.Instant;
import java.util.Objects;

public class ParkingViolation {
  private Instant timestamp;
  private double fine;
  private String description;
  private String vehicleId;
  private String licensePlateState;
  private String violationId;
  private String zipCode;

  public ParkingViolation(
          Instant timestamp,
          double fine,
          String description,
          String vehicleId,
          String licensePlateState,
          String violationId,
          String zipCode
  ) {
    this.timestamp = timestamp;
    this.fine = fine;
    this.description = description;
    this.vehicleId = vehicleId;
    this.licensePlateState = licensePlateState;
    this.violationId = violationId;
    this.zipCode = zipCode;
  }

  public Instant getTimestamp() {
    return timestamp;
  }
  public void setTimestamp(Instant timestamp) {
    this.timestamp = timestamp;
  }
  public double getFine() {
    return fine;
  }
  public void setFine(double fine) {
    this.fine = fine;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public String getVehicleId() {
    return vehicleId;
  }
  public void setVehicleId(String vehicleId) {
    this.vehicleId = vehicleId;
  }
  public String getLicensePlateState() {
    return licensePlateState;
  }
  public void setLicensePlateState(String licensePlateState) {
    this.licensePlateState = licensePlateState;
  }
  public String getViolationId() {
    return violationId;
  }
  public void setViolationId(String violationId) {
    this.violationId = violationId;
  }
  public String getZipCode() {
    return zipCode;
  }
  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ParkingViolation that = (ParkingViolation) o;
    return Double.compare(that.fine, fine) == 0 && Objects.equals(timestamp, that.timestamp) && Objects.equals(description, that.description) && Objects.equals(vehicleId, that.vehicleId) && Objects.equals(licensePlateState, that.licensePlateState) && Objects.equals(violationId, that.violationId) && Objects.equals(zipCode, that.zipCode);
  }
  @Override
  public int hashCode() {
    return java.util.Objects.hash(timestamp, fine, description, vehicleId, licensePlateState, violationId, zipCode);
  }

  @Override
  public String toString() {
    return "ParkingViolation{" +
            "timestamp=" + timestamp +
            ", fine=" + fine +
            ", description='" + description + '\'' +
            ", vehicleId='" + vehicleId + '\'' +
            ", licensePlateState='" + licensePlateState + '\'' +
            ", violationId='" + violationId + '\'' +
            ", zipCode='" + zipCode + '\'' +
            '}';
  }
}
