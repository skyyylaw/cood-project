package service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PopulationService {

    private String parkingViolationFilePath;
    private String propertyValueFilePath;
    private String populationFilePath;

    public PopulationService(String parkingViolationFilePath, String propertyValueFilePath, String populationFilePath) {
        this.parkingViolationFilePath = parkingViolationFilePath;
        this.propertyValueFilePath = propertyValueFilePath;
        this.populationFilePath = populationFilePath;
    }
    
    /**
     * Menu Option 1: Show total population for all ZIP Codes
     * @return a map of zip codes to populations
     */
    public Long getPopulationAllZipCodes() {
        List<PopulationService> populations = PopulationReader.readPopulationFile(populationFilePath);
        long total = 0;
        for (PopulationService p : populations) {
            total += p.getPopulation();
        }
        return total;
    }

}
