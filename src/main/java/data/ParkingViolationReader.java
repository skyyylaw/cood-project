package data;

import common.ParkingViolation;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ParkingViolationReader {
}
package data;

import common.ParkingViolation;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


public class ParkingViolationReader {
  public static List<ParkingViolation> readJsonFile(String fileName) {
    ArrayList<ParkingViolation> parkingViolations = new ArrayList<>();
    JSONArray jsonArray;
    int skip = 0;

    // read the file
    try {
      Object obj = new JSONParser().parse(new FileReader(fileName));
      jsonArray = (JSONArray) obj;
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }

    // Check for malformed, missing, or wrong type of data
    for (Object obj : jsonArray) {
      JSONObject jsonObject = (JSONObject) obj;

      // skip missing data
      if (        jsonObject.size() < 7
              || !jsonObject.containsKey("ticket_number")
              || !jsonObject.containsKey("plate_id")
              || !jsonObject.containsKey("date")
              || !jsonObject.containsKey("zip_code")
              || !jsonObject.containsKey("violation")
              || !jsonObject.containsKey("fine")
              || !jsonObject.containsKey("state")){
        skip++;
        continue;
      }

      // try to parse the data into a ParkingViolation instance, otherwise skip
      try {
        ParkingViolation  pv = new ParkingViolation(
                Instant.parse(jsonObject.get("date").toString()),
                Double.parseDouble(jsonObject.get("fine").toString()),
                jsonObject.get("violation").toString(),
                jsonObject.get("plate_id").toString(),
                jsonObject.get("state").toString(),
                jsonObject.get("ticket_number").toString(),
                jsonObject.get("zip_code").toString()
        );
        parkingViolations.add(pv);
      } catch (Exception e) {
        skip++;
      }

    }
//    System.out.println(skip);
    return parkingViolations;
  }

  public static List<ParkingViolation> readCsvFile(String fileName) {
    ArrayList<ParkingViolation> parkingViolations = new ArrayList<>();
    // read the file
    try {
      for (String line : Files.readAllLines(Paths.get(fileName))) {

        String[] parts = line.split(",");

        if (parts.length != 7) {
          continue;
        }

        // try to parse the data into a ParkingViolation instance, otherwise skip
        try {
          ParkingViolation pv = new ParkingViolation(
                  Instant.parse(parts[0]),
                  Double.parseDouble(parts[1]),
                  parts[2],
                  parts[3],
                  parts[4],
                  parts[5],
                  parts[6]
          );
          parkingViolations.add(pv);
        } catch (Exception e) {
          continue;
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return parkingViolations;
  }

  public static void main(String[] args) {
    System.out.println(readCsvFile("parking.csv"));
  }

}
