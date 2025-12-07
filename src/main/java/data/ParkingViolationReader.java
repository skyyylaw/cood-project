/*
Defensive programming has been used to ensure the program is robust and can handle errors gracefully.
*/


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


public class ParkingViolationReader {
  public static List<ParkingViolation> readJsonFile(String fileName) {
    // Input validation
    if (fileName == null || fileName.trim().isEmpty()) {
      throw new IllegalArgumentException("File name cannot be null or empty");
    }

    ArrayList<ParkingViolation> parkingViolations = new ArrayList<>();
    JSONArray jsonArray;
    int skip = 0;

    // Validate file exists and is readable
    Path filePath = Paths.get(fileName);
    if (!Files.exists(filePath)) {
      throw new IllegalArgumentException("File does not exist: " + fileName);
    }
    if (!Files.isReadable(filePath)) {
      throw new IllegalArgumentException("File is not readable: " + fileName);
    }

    // read the file with proper resource management
    try (FileReader fileReader = new FileReader(fileName)) {
      Object obj = new JSONParser().parse(fileReader);
      
      // Type safety check
      if (!(obj instanceof JSONArray)) {
        throw new IllegalArgumentException("JSON file does not contain a JSON array");
      }
      
      jsonArray = (JSONArray) obj;
      
      // Null check
      if (jsonArray == null) {
        return parkingViolations; // Return empty list if array is null
      }
    } catch (IOException e) {
      throw new RuntimeException("Error reading file: " + fileName, e);
    } catch (ParseException e) {
      throw new RuntimeException("Error parsing JSON file: " + fileName, e);
    }

    // Check for malformed, missing, or wrong type of data
    for (Object obj : jsonArray) {
      // Type safety check
      if (!(obj instanceof JSONObject)) {
        skip++;
        continue;
      }
      
      JSONObject jsonObject = (JSONObject) obj;
      
      // Null check
      if (jsonObject == null) {
        skip++;
        continue;
      }

      // skip missing data
      if (jsonObject.size() < 7
              || !jsonObject.containsKey("ticket_number")
              || !jsonObject.containsKey("plate_id")
              || !jsonObject.containsKey("date")
              || !jsonObject.containsKey("zip_code")
              || !jsonObject.containsKey("violation")
              || !jsonObject.containsKey("fine")
              || !jsonObject.containsKey("state")) {
        skip++;
        continue;
      }

      // Validate that values are not null before calling toString()
      Object dateObj = jsonObject.get("date");
      Object fineObj = jsonObject.get("fine");
      Object violationObj = jsonObject.get("violation");
      Object plateIdObj = jsonObject.get("plate_id");
      Object stateObj = jsonObject.get("state");
      Object ticketNumberObj = jsonObject.get("ticket_number");
      Object zipCodeObj = jsonObject.get("zip_code");

      if (dateObj == null || fineObj == null || violationObj == null
              || plateIdObj == null || stateObj == null
              || ticketNumberObj == null || zipCodeObj == null) {
        skip++;
        continue;
      }

      // try to parse the data into a ParkingViolation instance, otherwise skip
      try {
        String dateStr = dateObj.toString().trim();
        String fineStr = fineObj.toString().trim();
        String violationStr = violationObj.toString().trim();
        String plateIdStr = plateIdObj.toString().trim();
        String stateStr = stateObj.toString().trim();
        String ticketNumberStr = ticketNumberObj.toString().trim();
        String zipCodeStr = zipCodeObj.toString().trim();

        // Validate non-empty strings
        if (dateStr.isEmpty() || fineStr.isEmpty() || violationStr.isEmpty()
                || plateIdStr.isEmpty() || stateStr.isEmpty()
                || ticketNumberStr.isEmpty() || zipCodeStr.isEmpty()) {
          skip++;
          continue;
        }

        // Parse and validate fine
        double fine = Double.parseDouble(fineStr);
        if (fine < 0) {
          skip++;
          continue;
        }

        Instant timestamp = Instant.parse(dateStr);
        
        ParkingViolation pv = new ParkingViolation(
                timestamp,
                fine,
                violationStr,
                plateIdStr,
                stateStr,
                ticketNumberStr,
                zipCodeStr
        );
        parkingViolations.add(pv);
      } catch (NumberFormatException e) {
        skip++;
      } catch (java.time.format.DateTimeParseException e) {
        skip++;
      } catch (IllegalArgumentException e) {
        skip++;
      }
    }
//    System.out.println(skip);
    return parkingViolations;
  }

  public static List<ParkingViolation> readCsvFile(String fileName) {
    // Input validation
    if (fileName == null || fileName.trim().isEmpty()) {
      throw new IllegalArgumentException("File name cannot be null or empty");
    }

    ArrayList<ParkingViolation> parkingViolations = new ArrayList<>();
    
    // Validate file exists and is readable
    Path filePath = Paths.get(fileName);
    if (!Files.exists(filePath)) {
      throw new IllegalArgumentException("File does not exist: " + fileName);
    }
    if (!Files.isReadable(filePath)) {
      throw new IllegalArgumentException("File is not readable: " + fileName);
    }

    // read the file
    try {
      List<String> lines = Files.readAllLines(filePath);
      
      // Handle empty file
      if (lines == null || lines.isEmpty()) {
        return parkingViolations;
      }

      for (String line : lines) {
        // Skip empty lines
        if (line == null || line.trim().isEmpty()) {
          continue;
        }

        // Trim the line and split by comma
        String trimmedLine = line.trim();
        String[] parts = trimmedLine.split(",");

        if (parts.length != 7) {
          continue;
        }

        // Trim each part and validate non-empty
        String[] trimmedParts = new String[7];
        boolean hasEmptyField = false;
        for (int i = 0; i < 7; i++) {
          if (parts[i] == null) {
            hasEmptyField = true;
            break;
          }
          trimmedParts[i] = parts[i].trim();
          if (trimmedParts[i].isEmpty()) {
            hasEmptyField = true;
            break;
          }
        }

        if (hasEmptyField) {
          continue;
        }

        // try to parse the data into a ParkingViolation instance, otherwise skip
        try {
          // Validate fine is non-negative
          double fine = Double.parseDouble(trimmedParts[1]);
          if (fine < 0) {
            continue;
          }

          Instant timestamp = Instant.parse(trimmedParts[0]);
          
          ParkingViolation pv = new ParkingViolation(
                  timestamp,
                  fine,
                  trimmedParts[2],
                  trimmedParts[3],
                  trimmedParts[4],
                  trimmedParts[5],
                  trimmedParts[6]
          );
          parkingViolations.add(pv);
        } catch (NumberFormatException e) {
          // Skip invalid number format
          continue;
        } catch (java.time.format.DateTimeParseException e) {
          // Skip invalid date format
          continue;
        } catch (IllegalArgumentException e) {
          // Skip other validation errors
          continue;
        }
      }
    } catch (IOException e) {
      throw new RuntimeException("Error reading file: " + fileName, e);
    }
    return parkingViolations;
  }

  public static void main(String[] args) {
    // System.out.println(readCsvFile("parking.csv"));
    // System.out.println(readJsonFile("parking.json"));
    System.out.println("Equal contents (order does not matter): " +
        (new java.util.HashSet<>(readCsvFile("parking.csv")).equals(new java.util.HashSet<>(readJsonFile("parking.json")))));
  }

}
