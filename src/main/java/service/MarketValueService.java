package service;

import java.util.List;

import common.Population;
import common.PropertyValue;
import data.PopulationReader;
import data.PropertyValueReader;

public class MarketValueService {

    private String parkingViolationFilePath;
    private String propertyValueFilePath;
    private String populationFilePath;

    public MarketValueService(String parkingViolationFilePath, String propertyValueFilePath, String populationFilePath) {
        this.parkingViolationFilePath = parkingViolationFilePath;
        this.propertyValueFilePath = propertyValueFilePath;
        this.populationFilePath = populationFilePath;
    }

    /**
     * Menu Option 3: Show average market value for residences in a specified ZIP Code
     * @param zipCode the ZIP Code to get the average market value for
     * @return the average market value for residences in the specified ZIP Code
     */
    public int getAverageMarketValue(String zipCode) {
        List<PropertyValue> propertyValues = PropertyValueReader.readCsvFile(propertyValueFilePath);
        double totalMarketValue = 0;
        int count = 0;

        if (zipCode == null || zipCode.isEmpty()) {
            return 0;
        }

        for (PropertyValue pv : propertyValues) {
            if (pv == null) continue;
            String pvZip = pv.getZipCode();
            Double marketValue = pv.getMarketValue();
            if (pvZip == null || marketValue == null) continue;
            if (zipCode.equals(pvZip)) {
                totalMarketValue += marketValue;
                count++;
            }
        }

        if (count == 0) {
            return 0;
        }
        return (int)Math.round(totalMarketValue / count);
    }


    /**
     * Menu Option 5: Show total residential market value for a specified ZIP Code
     * i.e.  the total market value for all residences in the ZIP Code divided 
     * by the population of that ZIP Code, as provided in the population input file
     * @param zipCode the ZIP Code to get the total residential market value for
     * @return the total residential market value for the specified ZIP Code
     */
    public int getResidentialMarketValuePerCapita(String zipCode) {
        return 0;
    }

    /** 
     * Menu Option 6: Show average market value per square foot for residences in a specified ZIP Code
     * i.e., the average of (market value divided by livable area) for each residence in a ZIP Code
     * @param zipCode the ZIP Code to get the average market value per square foot for
     * @return the average market value per square foot for residences in the specified ZIP Code
     */
    public int getAverageMarketValuePerSquareFoot(String zipCode) {
        return 0;
    }

}
