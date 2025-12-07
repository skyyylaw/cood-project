package service;

import java.util.HashMap;
import java.util.Map;

public class Fines {

    private String parkingViolationFilePath;
    private String propertyValueFilePath;
    private String populationFilePath;

    public Fines(String parkingViolationFilePath, String propertyValueFilePath, String populationFilePath) {
        this.parkingViolationFilePath = parkingViolationFilePath;
        this.propertyValueFilePath = propertyValueFilePath;
        this.populationFilePath = populationFilePath;
    }

    /**
     * Menu Option 2: Show parking fines per capita for each ZIP Code
     * @return a map of zip codes to parking fines per capita
     */
    public Map<String, Double> getFinesPerCapitaPerZipCode() {
        return new HashMap<String, Double>();
    }

}
