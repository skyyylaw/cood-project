package data;

import common.ParkingViolation;
import java.util.List;

/**
 * Strategy interface for reading parking violation files.
 */
public interface FileReadingStrategy {
    List<ParkingViolation> read(String fileName);
}
