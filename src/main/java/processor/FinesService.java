package processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.ParkingViolation;
import common.Population;
import data.ParkingViolationReader;
import data.PopulationReader;

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
        // Use Strategy pattern to read file
        List<ParkingViolation> parkingViolations = ParkingViolationReader.readFile(parkingViolationFilePath);
        List<Population> populations = PopulationReader.readPopulationFile(populationFilePath);

        Map<String, Double> totalFinesPerZipCode = new HashMap<>();
        for (ParkingViolation parkingViolation : parkingViolations) {
            // Filter: ignore violations where license plate state is not "PA"
            String state = parkingViolation.getLicensePlateState();
            if (state == null || !state.trim().equals("PA")) {
                continue;
            }
            
            // Filter: ignore violations where ZIP Code is unknown (null or empty)
            String zipCode = parkingViolation.getZipCode();
            if (zipCode == null || zipCode.trim().isEmpty()) {
                continue;
            }
            
            Double fine = parkingViolation.getFine();
            // Accumulate fines for valid violations
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
            
            // Filter: don't display ZIP Codes where total aggregate fines = 0
            if (totalFine == null || totalFine == 0.0) {
                continue;
            }
            
            // Filter: don't display ZIP Codes where population = 0
            Integer population = zipToPopulation.get(zipCode);
            if (population == null || population == 0) {
                continue;
            }
            
            finesPerCapitaPerZipCode.put(zipCode, totalFine / population);
        }
        return finesPerCapitaPerZipCode;
    }

}
