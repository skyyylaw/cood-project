package processor;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

import common.Population;
import common.PropertyValue;
import data.PopulationReader;
import data.PropertyValueReader;

public class MarketValueService {

    private String parkingViolationFilePath;
    private String propertyValueFilePath;
    private String populationFilePath;
    
    // Memoization cache for getAverageMarketValue results
    private final Map<String, Integer> averageMarketValueCache = new ConcurrentHashMap<>();

    public MarketValueService(String parkingViolationFilePath, String propertyValueFilePath, String populationFilePath) {
        this.parkingViolationFilePath = parkingViolationFilePath;
        this.propertyValueFilePath = propertyValueFilePath;
        this.populationFilePath = populationFilePath;
    }

    /**
     * Menu Option 3: Show average market value for residences in a specified ZIP Code
     * Uses memoization to cache results for previously computed ZIP codes.
     * @param zipCode the ZIP Code to get the average market value for
     * @return the average market value for residences in the specified ZIP Code
     */
    public int getAverageMarketValue(String zipCode) {
        if (zipCode == null || zipCode.isEmpty()) {
            return 0;
        }
        
        // Check cache first (memoization)
        return averageMarketValueCache.computeIfAbsent(zipCode, key -> {
            
            System.out.println("did not hit cache");
            
            List<PropertyValue> propertyValues = PropertyValueReader.readCsvFile(propertyValueFilePath);
            double totalMarketValue = 0;
            int count = 0;

            for (PropertyValue pv : propertyValues) {
                if (pv == null) continue;
                String pvZip = pv.getZipCode();
                Double marketValue = pv.getMarketValue();
                if (pvZip == null || marketValue == null) continue;
                if (key.equals(pvZip)) {
                    totalMarketValue += marketValue;
                    count++;
                }
            }

            if (count == 0) {
                return 0;
            }
            return (int)Math.round(totalMarketValue / count);
        });
    }


    /**
     /**
      * Menu Option 5: Show residential market value per capita for a specified ZIP Code.
      * The residential market value per capita is the total market value for all residences
      * in the ZIP Code divided by the population of that ZIP Code (as provided in the population input file).
      * If there are no residences or the ZIP Code is not found in the population input file, returns 0.
      * The returned value is rounded to the nearest integer.
      * 
      * @param zipCode the ZIP Code to compute the residential market value per capita for
      * @return the residential market value per capita for the specified ZIP Code, rounded to the nearest integer; or 0 if data is missing
      */
    public int getResidentialMarketValuePerCapita(String zipCode) {
        if (zipCode == null || zipCode.isEmpty()) {
            return 0;
        }

        List<PropertyValue> propertyValues = PropertyValueReader.readCsvFile(propertyValueFilePath);
        double totalMarketValue = 0;

        for (PropertyValue pv : propertyValues) {
            if (pv == null) continue;
            String pvZip = pv.getZipCode();
            Double marketValue = pv.getMarketValue();
            if (pvZip == null || marketValue == null) continue;
            if (zipCode.equals(pvZip)) {
                totalMarketValue += marketValue;
            }
        }

        // now read population from the population file
        List<Population> populationList = PopulationReader.readPopulationFile(populationFilePath);
        int populationForZip = 0;
        for (Population pop : populationList) {
            if (pop == null) continue;
            if (zipCode.equals(pop.getZipCode())) {
                populationForZip = pop.getPopulation();
                break;
            }
        }

        if (populationForZip == 0) {
            return 0;
        }

        return (int) Math.round(totalMarketValue / populationForZip);
    }

    /** 
     * Menu Option 6: Show average market value per square foot for residences in a specified ZIP Code
     * i.e., the average of (market value divided by livable area) for each residence in a ZIP Code
     * @param zipCode the ZIP Code to get the average market value per square foot for
     * @return the average market value per square foot for residences in the specified ZIP Code
     */
    public int getAverageMarketValuePerSquareFoot(String zipCode) {
        if (zipCode == null || zipCode.isEmpty()) {
            return 0;
        }
        List<PropertyValue> propertyValues = PropertyValueReader.readCsvFile(propertyValueFilePath);
        double totalMarketValue = 0;
        double totalLivableArea = 0;
        for (PropertyValue pv : propertyValues) {
            if (pv == null) continue;
            String pvZip = pv.getZipCode();
            Double marketValue = pv.getMarketValue();
            Double livableArea = pv.getTotalLivableArea();
            if (pvZip == null || marketValue == null || livableArea == null) continue;
            if (zipCode.equals(pvZip) && livableArea > 0) {
                totalMarketValue += marketValue;
                totalLivableArea += livableArea;
            }
        }
        if (totalLivableArea == 0) {
            return 0;
        }
        return (int) Math.round(totalMarketValue / totalLivableArea);
    }

}
