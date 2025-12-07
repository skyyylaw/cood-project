package data;

import common.PropertyValue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PropertyValueReader {
  
  /**
   * Parses a string to a Double, returning null if invalid
   */
  private static Double parseDoubleSafely(String value) {
    if (value == null || value.trim().isEmpty()) {
      return null;
    }
    try {
      double parsed = Double.parseDouble(value.trim());
      if (parsed <= 0) {
        return null;
      }
      return parsed;
    } catch (NumberFormatException e) {
      return null;
    }
  }

  /**
   * Extracts the first 5 digits from a ZIP code
   */
  private static String extractZipCode(String zipCode) {
    if (zipCode == null || zipCode.trim().isEmpty()) {
      return null;
    }
    String trimmed = zipCode.trim();
    if (trimmed.length() >= 5) {
      return trimmed.substring(0, 5);
    }
    return trimmed;
  }

  /**
   * Finds the index of a column in the header array
   * Returns -1 if not found
   */
  private static int findColumnIndex(String[] headers, String target) {
    if (headers == null || target == null) {
      return -1;
    }
    for (int i = 0; i < headers.length; i++) {
      if (headers[i] != null && headers[i].trim().equals(target)) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Reads property values from a CSV file
   * The first row contains headers that indicate column positions
   * @param fileName the path to the CSV file (must not be null or empty)
   * @return a list of PropertyValue objects parsed from the file
   * @throws IllegalArgumentException if fileName is null or empty
   * @throws RuntimeException if the file cannot be read or required columns are missing
   */
  public static List<PropertyValue> readCsvFile(String fileName) {
    if (fileName == null || fileName.trim().isEmpty()) {
      throw new IllegalArgumentException("File name cannot be null or empty");
    }
    
    ArrayList<PropertyValue> propertyValues = new ArrayList<>();
    
    try {
      List<String> lines = Files.readAllLines(Paths.get(fileName));
      
      if (lines.isEmpty()) {
        return propertyValues;
      }

      String headerLine = lines.get(0);
      String[] headers = headerLine.split(",");
      
      int marketValueIndex = findColumnIndex(headers, "market_value");
      int totalLivableAreaIndex = findColumnIndex(headers, "total_livable_area");
      int zipCodeIndex = findColumnIndex(headers, "zip_code");

      if (marketValueIndex == -1 || totalLivableAreaIndex == -1 || zipCodeIndex == -1) {
        throw new RuntimeException("Required columns not found in CSV header");
      }

      for (int i = 1; i < lines.size(); i++) {
        String line = lines.get(i);
        String[] parts = line.split(",");

        if (parts.length <= Math.max(marketValueIndex, Math.max(totalLivableAreaIndex, zipCodeIndex))) {
          continue;
        }

        String zipCode = extractZipCode(parts[zipCodeIndex]);
        Double marketValue = parseDoubleSafely(parts[marketValueIndex]);
        Double totalLivableArea = parseDoubleSafely(parts[totalLivableAreaIndex]);
        PropertyValue pv = new PropertyValue(marketValue, totalLivableArea, zipCode);
        propertyValues.add(pv);
      }
    } catch (IOException e) {
      throw new RuntimeException("Error reading file: " + fileName, e);
    }

    return propertyValues;
  }
}
