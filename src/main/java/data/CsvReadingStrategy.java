package data;

import common.ParkingViolation;
import java.util.List;

/**
 * Strategy for reading CSV files.
 */
public class CsvReadingStrategy implements FileReadingStrategy {
    @Override
    public List<ParkingViolation> read(String fileName) {
        return ParkingViolationReader.readCsvFile(fileName);
    }
}
