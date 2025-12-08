package service;

import java.util.List;

import common.PropertyValue;
import data.PropertyValueReader;

public class ResidentialAreaService {

    private String parkingViolationFilePath;
    private String propertyValueFilePath;
    private String populationFilePath;

    public ResidentialAreaService(String parkingViolationFilePath, String propertyValueFilePath, String populationFilePath) {
        this.parkingViolationFilePath = parkingViolationFilePath;
        this.propertyValueFilePath = propertyValueFilePath;
        this.populationFilePath = populationFilePath;
    }

    /**
     * Menu Option 4: Show average residential area for residences in a specified ZIP Code
     * @param zipCode the ZIP Code to get the average residential area for
     * @return the average residential area for residences in the specified ZIP Code
     */
    public int getAverageResidentialArea(String zipCode) {
        List<PropertyValue> propertyValues = PropertyValueReader.readCsvFile(propertyValueFilePath);
        double totalArea = 0;
        int count = 0;
        if (zipCode == null || zipCode.isEmpty()) {
            return 0;
        }
        for (PropertyValue pv : propertyValues) {
            if (pv == null) continue;
            String pvZip = pv.getZipCode();
            Double area = pv.getTotalLivableArea();
            if (pvZip == null || area == null) continue;
            if (zipCode.equals(pvZip)) {
                totalArea += area;
                count++;
            }
        }
        if (count == 0) {
            return 0;
        }
        return (int)Math.round(totalArea / count);
    }   

    /**
     * Menu Option 7: Show minimum and maximum livable areas for homes in a specified ZIP Code
     * @param zipCode the ZIP Code to get the min and max livable areas for
     * @return an array where the first element is the minimum and the second is the maximum livable area
     */
    public int[] getMinAndMaxLivableAreas(String zipCode) {
        return new int[]{0, 0};
    }
    
}
