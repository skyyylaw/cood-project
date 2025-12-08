package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.ParkingViolation;
import common.Population;
import common.PropertyValue;
import data.ParkingViolationReader;
import data.PopulationReader;
import data.PropertyValueReader;

public class FinesService {

    private String parkingViolationFilePath;
    private String propertyValueFilePath;
    private String populationFilePath;

    public FinesService(String parkingViolationFilePath, String propertyValueFilePath, String populationFilePath) {
        this.parkingViolationFilePath = parkingViolationFilePath;
        this.propertyValueFilePath = propertyValueFilePath;
        this.populationFilePath = populationFilePath;
    }

    /**
     * Menu Option 2: Show parking fines per capita for each ZIP Code
     * @return a map of zip codes to parking fines per capita
     */
    public Map<String, Double> getFinesPerCapitaPerZipCode() {
        // Judge file type by extension and use appropriate reader (JSON or CSV)
        List<ParkingViolation> parkingViolations;
        if (parkingViolationFilePath != null && parkingViolationFilePath.toLowerCase().endsWith(".json")) {
            parkingViolations = ParkingViolationReader.readJsonFile(parkingViolationFilePath);
        } else if (parkingViolationFilePath != null && parkingViolationFilePath.toLowerCase().endsWith(".csv")) {
            parkingViolations = ParkingViolationReader.readCsvFile(parkingViolationFilePath);
        } else {
            throw new IllegalArgumentException("Unsupported file format for parking violations. Only .json or .csv are allowed.");
        }
        List<Population> populations = PopulationReader.readPopulationFile(populationFilePath);

        // Sum all fines for each ZIP Code
        Map<String, Double> totalFinesPerZipCode = new HashMap<>();
        for (ParkingViolation parkingViolation : parkingViolations) {
            String zipCode = parkingViolation.getZipCode();
            Double fine = parkingViolation.getFine();
            // Accumulate
            totalFinesPerZipCode.put(zipCode, totalFinesPerZipCode.getOrDefault(zipCode, 0.0) + fine);
        }

        // populations is a List<Population>, so build a ZipCode->Population map first
        Map<String, Integer> zipToPopulation = new HashMap<>();
        for (Population pop : populations) {
            zipToPopulation.put(pop.getZipCode(), pop.getPopulation());
        }

        Map<String, Double> finesPerCapitaPerZipCode = new HashMap<>();
        for (Map.Entry<String, Double> entry : totalFinesPerZipCode.entrySet()) {
            String zipCode = entry.getKey();
            Double totalFine = entry.getValue();
            Integer population = zipToPopulation.get(zipCode);
            if (population != null && population > 0) {
                finesPerCapitaPerZipCode.put(zipCode, totalFine / population);
            }
        }
        return finesPerCapitaPerZipCode;
    }

}
