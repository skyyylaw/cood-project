package ui;

import service.PopulationService;
import service.FinesService;
import service.MarketValueService;
import service.ResidentialAreaService;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UI {
    private Scanner scanner;
    private PopulationService populationService;
    private FinesService finesService;
    private MarketValueService marketValueService;
    private ResidentialAreaService residentialAreaService;

    public UI(String parkingViolationFilePath, String propertyValueFilePath, String populationFilePath, Scanner scanner) {
        this.scanner = scanner;
        this.populationService = new PopulationService(parkingViolationFilePath, propertyValueFilePath, populationFilePath);
        this.finesService = new FinesService(parkingViolationFilePath, propertyValueFilePath, populationFilePath);
        this.marketValueService = new MarketValueService(parkingViolationFilePath, propertyValueFilePath, populationFilePath);
        this.residentialAreaService = new ResidentialAreaService(parkingViolationFilePath, propertyValueFilePath, populationFilePath);
    }


    


    public void start() {
        if (scanner == null) {
            System.err.println("Error: Scanner is null. Cannot start UI.");
            return;
        }

        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Show total population for all ZIP Codes");
            System.out.println("2. Show parking fines per capita for each ZIP Code");
            System.out.println("3. Show average market value for residences in a specified ZIP Code");
            System.out.println("4. Show average total livable area for residences in a specified ZIP Code");
            System.out.println("5. Show residential market value per capita for a specified ZIP Code");
            System.out.println("6. Show average market value per square foot for residences in a specified ZIP Code");
            System.out.println("7. Show minimum and maximum livable areas for homes in a specified ZIP Code");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            String input;
            try {
                input = scanner.nextLine();
                if (input == null) {
                    System.out.println("Invalid input. Please enter a number between 0 and 7.");
                    continue;
                }
                input = input.trim();
            } catch (Exception e) {
                System.err.println("Error reading input: " + e.getMessage());
                break;
            }

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 0 and 7.");
                continue;
            }

            //declaring zipCode. cases below after 1 still untouched!
            String zipCode;

            switch (choice) {
                case 1:
                    //"If the user enters the number 1, the program should show the total population for all ZIP Codes"
                    try {
                        Long totalPopulation = populationService.getPopulationAllZipCodes();
                        if (totalPopulation == null) {
                            System.out.println("Unable to retrieve total population. Data may be unavailable.");
                        } else {
                            System.out.println("Total population for all ZIP Codes: " + totalPopulation);
                        }
                    } catch (Exception e) {
                        System.err.println("Error retrieving total population: " + e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        Map<String, Double> finesMap = finesService.getFinesPerCapitaPerZipCode();
                        if (finesMap == null || finesMap.isEmpty()) {
                            System.out.println("Error: Unable to retrieve fines data.");
                            break;
                        }
                        // Filter out any ZIP with null/0 fines per capita
                        List<String> filteredZipCodes = new java.util.ArrayList<>();
                        for (Map.Entry<String, Double> entry : finesMap.entrySet()) {
                            Double value = entry.getValue();
                            if (value != null && value > 0.0) {
                                filteredZipCodes.add(entry.getKey());
                            }
                        }
                        // Sort numerically ascending
                        java.util.Collections.sort(filteredZipCodes, (z1, z2) -> {
                            try {
                                return Integer.compare(Integer.parseInt(z1), Integer.parseInt(z2));
                            } catch (NumberFormatException e) {
                                return z1.compareTo(z2);
                            }
                        });

                        for (String zip : filteredZipCodes) {
                            Double finesPerCapita = finesMap.get(zip);
                            if (finesPerCapita == null) continue;
                            String formattedValue = String.format("%.4f", finesPerCapita);
                            System.out.println(zip + " " + formattedValue);
                        }
                    } catch (Exception e) {
                        System.err.println("Error retrieving fines per capita: " + e.getMessage());
                    }
                    break;
                case 3:
                    System.out.print("Enter ZIP Code: ");
                    try {
                        zipCode = scanner.nextLine();
                        if (zipCode == null) {
                            System.out.println("Invalid ZIP Code. Please try again.");
                            break;
                        }
                        zipCode = zipCode.trim();
                        if (zipCode.isEmpty()) {
                            System.out.println("ZIP Code cannot be empty. Please try again.");
                            break;
                        }
                        int avgMarketValue = marketValueService.getAverageMarketValue(zipCode);
                        if (avgMarketValue == 0) {
                            System.out.println("No data found for ZIP Code: " + zipCode);
                        } else {
                            System.out.println("Average market value for ZIP Code " + zipCode + ": " + avgMarketValue);
                        }
                    } catch (Exception e) {
                        System.err.println("Error retrieving average market value: " + e.getMessage());
                    }
                    break;
                case 4:
                    System.out.print("Enter ZIP Code: ");
                    try {
                        zipCode = scanner.nextLine();
                        if (zipCode == null) {
                            System.out.println("Invalid ZIP Code. Please try again.");
                            break;
                        }
                        zipCode = zipCode.trim();
                        if (zipCode.isEmpty()) {
                            System.out.println("ZIP Code cannot be empty. Please try again.");
                            break;
                        }
                        int avgArea = residentialAreaService.getAverageResidentialArea(zipCode);
                        if (avgArea == 0) {
                            System.out.println("No data found for ZIP Code: " + zipCode);
                        } else {
                            System.out.println("Average total livable area for ZIP Code " + zipCode + ": " + avgArea);
                        }
                    } catch (Exception e) {
                        System.err.println("Error retrieving average residential area: " + e.getMessage());
                    }
                    break;
                case 5:
                    System.out.print("Enter ZIP Code: ");
                    try {
                        zipCode = scanner.nextLine();
                        if (zipCode == null) {
                            System.out.println("Invalid ZIP Code. Please try again.");
                            break;
                        }
                        zipCode = zipCode.trim();
                        if (zipCode.isEmpty()) {
                            System.out.println("ZIP Code cannot be empty. Please try again.");
                            break;
                        }
                        int valuePerCapita = marketValueService.getResidentialMarketValuePerCapita(zipCode);
                        if (valuePerCapita == 0) {
                            System.out.println("No data found for ZIP Code: " + zipCode);
                        } else {
                            System.out.println("Residential market value per capita for ZIP Code " + zipCode + ": " + valuePerCapita);
                        }
                    } catch (Exception e) {
                        System.err.println("Error retrieving residential market value per capita: " + e.getMessage());
                    }
                    break;
                case 6:
                    System.out.print("Enter ZIP Code: ");
                    try {
                        zipCode = scanner.nextLine();
                        if (zipCode == null) {
                            System.out.println("Invalid ZIP Code. Please try again.");
                            break;
                        }
                        zipCode = zipCode.trim();
                        if (zipCode.isEmpty()) {
                            System.out.println("ZIP Code cannot be empty. Please try again.");
                            break;
                        }
                        int valuePerSqFoot = marketValueService.getAverageMarketValuePerSquareFoot(zipCode);
                        if (valuePerSqFoot == 0) {
                            System.out.println("No data found for ZIP Code: " + zipCode);
                        } else {
                            System.out.println("Average market value per square foot for ZIP Code " + zipCode + ": " + valuePerSqFoot);
                        }
                    } catch (Exception e) {
                        System.err.println("Error retrieving average market value per square foot: " + e.getMessage());
                    }
                    break;
                case 7:
                    System.out.print("Enter ZIP Code: ");
                    try {
                        zipCode = scanner.nextLine();
                        if (zipCode == null) {
                            System.out.println("Invalid ZIP Code. Please try again.");
                            break;
                        }
                        zipCode = zipCode.trim();
                        if (zipCode.isEmpty()) {
                            System.out.println("ZIP Code cannot be empty. Please try again.");
                            break;
                        }
                        int[] minMaxAreas = residentialAreaService.getMinAndMaxLivableAreas(zipCode);
                        if (minMaxAreas == null || minMaxAreas.length != 2) {
                            System.out.println("Error: Invalid data returned for ZIP Code: " + zipCode);
                        } else if (minMaxAreas[0] == 0 && minMaxAreas[1] == 0) {
                            System.out.println("No data found for ZIP Code: " + zipCode);
                        } else {
                            System.out.println("Minimum and maximum livable areas for ZIP Code " + zipCode + ": Min: " + minMaxAreas[0] + ", Max: " + minMaxAreas[1]);
                        }
                    } catch (Exception e) {
                        System.err.println("Error retrieving min and max livable areas: " + e.getMessage());
                    }
                    break;
                case 0:
                    System.out.println("Exiting program...");
                    System.out.println();
                    return;
                default:
                    System.out.println("Invalid input. Please enter a number between 0 and 7.");
                    continue;
            }
        }
        
    }

}
