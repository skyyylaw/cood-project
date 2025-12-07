package service;
import java.util.HashMap;
import java.util.Map;

public class Population {

    private String parkingViolationFilePath;
    private String propertyValueFilePath;
    private String populationFilePath;

    public Population(String parkingViolationFilePath, String propertyValueFilePath, String populationFilePath) {
        this.parkingViolationFilePath = parkingViolationFilePath;
        this.propertyValueFilePath = propertyValueFilePath;
        this.populationFilePath = populationFilePath;
    }
    
    /**
     * Menu Option 1: Show total population for all ZIP Codes
     * @return a map of zip codes to populations
     */
    public Map<String, Integer> getPopulationAllZipCodes() {
        return new HashMap<String, Integer>();
    }

}
