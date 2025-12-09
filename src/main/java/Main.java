/*
Program Entry Point
- Defensive programming has been used to ensure the program is robust and can handle errors gracefully.
*/

import ui.UI;
import java.util.Scanner;

public class Main {
  
  public static void main(String[] args) {
    // Validate number of arguments
    if (args.length != 3) {
      System.err.println("Error: Incorrect number of arguments.");
      System.err.println("Expected 3 arguments: <parking_file> <property_file> <population_file>");
      System.exit(1);
    }
    
    String parkingFile = args[0];
    String propertyFile = args[1];
    String populationFile = args[2];
  
    UI ui = UI.getInstance(parkingFile, propertyFile, populationFile, new Scanner(System.in));
    ui.start();
  }
}
