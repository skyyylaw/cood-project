package ui;

import data.ParkingViolationReader;
import data.PropertyValueReader;
import data.PopulationReader;
import service.Population;
import service.Fines;
import service.MarketValue;
import service.ResidentialArea;
import java.util.Scanner;

public class UI {
    private  String parkingViolationFilePath;
    private  String propertyValueFilePath;
    private  String populationFilePath;
    private Scanner scanner;
    private Population populationService;
    private Fines finesService;
    private MarketValue marketValueService;
    private ResidentialArea residentialAreaService;

    public UI(String parkingViolationFilePath, String propertyValueFilePath, String populationFilePath, Scanner scanner) {
        this.parkingViolationFilePath = parkingViolationFilePath;
        this.propertyValueFilePath = propertyValueFilePath;
        this.populationFilePath = populationFilePath;
        this.scanner = scanner;
        this.populationService = new Population(parkingViolationFilePath, propertyValueFilePath, populationFilePath);
        this.finesService = new Fines(parkingViolationFilePath, propertyValueFilePath, populationFilePath);
        this.marketValueService = new MarketValue(parkingViolationFilePath, propertyValueFilePath, populationFilePath);
        this.residentialAreaService = new ResidentialArea(parkingViolationFilePath, propertyValueFilePath, populationFilePath);
    }


    


    public void start() {
        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Show total population for all ZIP Codes");
            System.out.println("2. Show parking fines per capita for each ZIP Code");
            System.out.println("3. Show average market value for residences in a specified ZIP Code");
            System.out.println("4. Show average total livable area for residences in a specified ZIP Code");
            System.out.println("5. Show residential market value per capita for a specified ZIP Code");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            String input = scanner.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 0 and 5.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.println("Enter ZIP Code: ");
                    String zipCode = scanner.nextLine().trim();
                    System.out.println("Total population for ZIP Code " + zipCode + ": " + populationService.getPopulationAllZipCodes().get(zipCode));
                    break;
                case 2:
                    System.out.println("Enter ZIP Code: ");
                    zipCode = scanner.nextLine().trim();
                    System.out.println("Parking fines per capita for ZIP Code " + zipCode + ": " + finesService.getFinesPerCapitaPerZipCode().get(zipCode));
                    break;
                case 3:
                    System.out.println("Enter ZIP Code: ");
                    zipCode = scanner.nextLine().trim();
                    System.out.println("Average market value for ZIP Code " + zipCode + ": " + marketValueService.getAverageMarketValue(zipCode));
                    break;
                case 4:
                    System.out.println("Enter ZIP Code: ");
                    zipCode = scanner.nextLine().trim();
                    System.out.println("Average total livable area for ZIP Code " + zipCode + ": " + residentialAreaService.getAverageResidentialArea(zipCode));
                    break;
                case 5:
                    System.out.println("Enter ZIP Code: ");
                    zipCode = scanner.nextLine().trim();
                    System.out.println("Residential market value per capita for ZIP Code " + zipCode + ": " + marketValueService.getResidentialMarketValuePerCapita(zipCode));
                    break;
                case 6:
                    System.out.println("Enter ZIP Code: ");
                    zipCode = scanner.nextLine().trim();
                    System.out.println("Average market value per square foot for ZIP Code " + zipCode + ": " + marketValueService.getAverageMarketValuePerSquareFoot(zipCode));
                    break;
                case 7:
                    System.out.println("Enter ZIP Code: ");
                    zipCode = scanner.nextLine().trim();
                    System.out.println("Minimum and maximum livable areas for ZIP Code " + zipCode + ": " + residentialAreaService.getMinAndMaxLivableAreas(zipCode));
                    break;
                case 0:
                    System.out.println("Exiting program...");
                    System.out.println();
                    break;
                default:
                    System.out.println("Invalid input. Please enter a number between 0 and 5.");
                    scanner.close();
                    return;
            }
        }
        
    }

}
