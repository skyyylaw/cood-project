package data;

import common.ParkingViolation;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Strategy for reading JSON files.
 */
public class JsonReadingStrategy implements FileReadingStrategy {
    @Override
    public List<ParkingViolation> read(String fileName) {
        return ParkingViolationReader.readJsonFile(fileName);
    }
}
