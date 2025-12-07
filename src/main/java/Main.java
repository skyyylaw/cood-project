/*
Program Entry Point
- Defensive programming has been used to ensure the program is robust and can handle errors gracefully.
*/

import ui.UI;
import java.util.Scanner;

public class Main {
  
  public static void main(String[] args) {
    // Validate number of arguments
    if (args.length != 4) {
      System.err.println("Error: Incorrect number of arguments.");
      System.err.println("Expected 4 arguments: <format> <parking_file> <property_file> <population_file>");
      System.err.println("  format: \"csv\" or \"json\" (case-sensitive)");
      System.exit(1);
    }
    
    String parkingFile = args[1];
    String propertyFile = args[2];
    String populationFile = args[3];
  
    UI ui = new UI(parkingFile, propertyFile, populationFile, new Scanner(System.in));
    ui.start();
  }
}
