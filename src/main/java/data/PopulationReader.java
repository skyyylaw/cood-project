package data;

import common.Population;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PopulationReader {

  /**
   * Parses a string to the population Integer. Returns null if invalid (0 is allowed).
   */

  private static Integer parseIntSafely(String value) {

    if (value == null || value.trim().isEmpty()) {
      return null;
    }

    try {
      int parsed = Integer.parseInt(value.trim());
      if (parsed < 0) { //ignore negative populations
        return null;
      }

      return parsed;

    } catch (NumberFormatException e) {
      return null;
    }

  }

  /**
   * normalize ZIP Code string:
   */
  private static String normalizeZipCode(String zipCode) {
    if (zipCode == null) {
      return null;
    }

    String trimmed = zipCode.trim();
    return trimmed.isEmpty() ? null : trimmed;
  }

  /**
   * Reads population data from text file non-empty lines separated by whitespaces
   * @param fileName the path to the population file (must not be null or empty)
   * @return a list of Population objects parsed from the file
   * @throws IllegalArgumentException if file name is null/empty
   * @throws RuntimeException if the file cannot be read or required data is missing
   */

  public static List<Population> readPopulationFile(String fileName) {
    if (fileName == null || fileName.trim().isEmpty()) {
      throw new IllegalArgumentException("File name cannot be null or empty");
    }

    ArrayList<Population> populations = new ArrayList<>();

    try {
      List<String> lines = Files.readAllLines(Paths.get(fileName));

      if (lines == null || lines.isEmpty()) {
        return populations;
      }
      // Skip null/blank lines
      for (String line : lines) {
        if (line == null) {
          continue;
        }
        String trimmedLine = line.trim();
        if (trimmedLine.isEmpty()) {
          continue;
        }

        // split with whitespace characters
        String[] parts = trimmedLine.split("\\s+");
        if (parts.length < 2) {
          continue; //skip this malformed line
        }

        String zipCode = normalizeZipCode(parts[0]);
        Integer populationValue = parseIntSafely(parts[1]);
        // skip when invalid ZIP Code or population
        if (zipCode == null || populationValue == null) {
          continue;
        }

        Population p = new Population(zipCode, populationValue);
        populations.add(p);
      }
    } catch (IOException e) {
      throw new RuntimeException("Error reading file: " + fileName, e);
    }

    return populations;
  }

// test run (should output 1526206!):
  /*
  public static void main(String[] args) {
    List<Population> populations = readPopulationFile("population.txt");
    long total = 0;
    for (Population p : populations) {
      total += p.getPopulation();
    }
   System.out.println("Total population = " + total);
  }
  */
}
