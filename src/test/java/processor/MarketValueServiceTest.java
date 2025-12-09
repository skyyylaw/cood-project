package processor;

import common.Population;
import common.PropertyValue;
import data.PopulationReader;
import data.PropertyValueReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MarketValueServiceTest {

    private MarketValueService marketValueService;
    private String parkingViolationFilePath;
    private String propertyValueFilePath;
    private String populationFilePath;

    @BeforeEach
    void setUp() {
        parkingViolationFilePath = "parking.json";
        propertyValueFilePath = "property.csv";
        populationFilePath = "population.txt";
        marketValueService = new MarketValueService(parkingViolationFilePath, propertyValueFilePath, populationFilePath);
    }

    @Test
    void testGetAverageMarketValue_ValidDataAndCache() {
        List<PropertyValue> propertyValues = Arrays.asList(
            new PropertyValue(100000.0, 1000.0, "15213"),
            new PropertyValue(200000.0, 1500.0, "15213"),
            new PropertyValue(300000.0, 2000.0, "15213")
        );

        try (MockedStatic<PropertyValueReader> mockedReader = mockStatic(PropertyValueReader.class)) {
            mockedReader.when(() -> PropertyValueReader.readCsvFile(propertyValueFilePath))
                .thenReturn(propertyValues);

            int result1 = marketValueService.getAverageMarketValue("15213");
            assertEquals(200000, result1);

            int result2 = marketValueService.getAverageMarketValue("15213");
            assertEquals(200000, result2);

            mockedReader.verify(() -> PropertyValueReader.readCsvFile(propertyValueFilePath), times(1));
        }
    }

    @Test
    void testGetAverageMarketValue_NullEmptyZipAndNoMatch() {
        List<PropertyValue> propertyValues = Arrays.asList(
            new PropertyValue(100000.0, 1000.0, "15217")
        );

        try (MockedStatic<PropertyValueReader> mockedReader = mockStatic(PropertyValueReader.class)) {
            mockedReader.when(() -> PropertyValueReader.readCsvFile(propertyValueFilePath))
                .thenReturn(propertyValues);

            assertEquals(0, marketValueService.getAverageMarketValue(null));
            assertEquals(0, marketValueService.getAverageMarketValue(""));
            assertEquals(0, marketValueService.getAverageMarketValue("15213"));
        }
    }

    @Test
    void testGetAverageMarketValue_NullHandling() {
        List<PropertyValue> propertyValues = Arrays.asList(
            null,
            new PropertyValue(null, 1000.0, "15213"),
            new PropertyValue(100000.0, null, "15213"),
            new PropertyValue(200000.0, 1500.0, null),
            new PropertyValue(300000.0, 2000.0, "15213")
        );

        try (MockedStatic<PropertyValueReader> mockedReader = mockStatic(PropertyValueReader.class)) {
            mockedReader.when(() -> PropertyValueReader.readCsvFile(propertyValueFilePath))
                .thenReturn(propertyValues);

            int result = marketValueService.getAverageMarketValue("15213");
            assertEquals(200000, result); // (100000 + 300000) / 2 = 200000
        }
    }

    @Test
    void testGetResidentialMarketValuePerCapita_ValidData() {
        List<PropertyValue> propertyValues = Arrays.asList(
            new PropertyValue(100000.0, 1000.0, "15213"),
            new PropertyValue(200000.0, 1500.0, "15213")
        );
        List<Population> populations = Arrays.asList(new Population("15213", 1000));

        try (MockedStatic<PropertyValueReader> mockedPropertyReader = mockStatic(PropertyValueReader.class);
             MockedStatic<PopulationReader> mockedPopulationReader = mockStatic(PopulationReader.class)) {

            mockedPropertyReader.when(() -> PropertyValueReader.readCsvFile(propertyValueFilePath))
                .thenReturn(propertyValues);
            mockedPopulationReader.when(() -> PopulationReader.readPopulationFile(populationFilePath))
                .thenReturn(populations);

            int result = marketValueService.getResidentialMarketValuePerCapita("15213");
            assertEquals(300, result);
        }
    }

    @Test
    void testGetResidentialMarketValuePerCapita_NullEmptyZipAndZeroPopulation() {
        List<PropertyValue> propertyValues = Arrays.asList(
            new PropertyValue(100000.0, 1000.0, "15213")
        );
        List<Population> populations = Arrays.asList(
            new Population("15213", 0),
            new Population("15217", 1000)
        );

        try (MockedStatic<PropertyValueReader> mockedPropertyReader = mockStatic(PropertyValueReader.class);
             MockedStatic<PopulationReader> mockedPopulationReader = mockStatic(PopulationReader.class)) {

            mockedPropertyReader.when(() -> PropertyValueReader.readCsvFile(propertyValueFilePath))
                .thenReturn(propertyValues);
            mockedPopulationReader.when(() -> PopulationReader.readPopulationFile(populationFilePath))
                .thenReturn(populations);

            assertEquals(0, marketValueService.getResidentialMarketValuePerCapita(null));
            assertEquals(0, marketValueService.getResidentialMarketValuePerCapita(""));
            assertEquals(0, marketValueService.getResidentialMarketValuePerCapita("15213"));
            assertEquals(0, marketValueService.getResidentialMarketValuePerCapita("15219"));
        }
    }

    @Test
    void testGetResidentialMarketValuePerCapita_NullHandling() {
        List<PropertyValue> propertyValues = Arrays.asList(
            null,
            new PropertyValue(null, 1000.0, "15213"),
            new PropertyValue(100000.0, 1500.0, null),
            new PropertyValue(200000.0, 2000.0, "15213")
        );
        List<Population> populations = Arrays.asList(
            null,
            new Population("15213", 1000)
        );

        try (MockedStatic<PropertyValueReader> mockedPropertyReader = mockStatic(PropertyValueReader.class);
             MockedStatic<PopulationReader> mockedPopulationReader = mockStatic(PopulationReader.class)) {

            mockedPropertyReader.when(() -> PropertyValueReader.readCsvFile(propertyValueFilePath))
                .thenReturn(propertyValues);
            mockedPopulationReader.when(() -> PopulationReader.readPopulationFile(populationFilePath))
                .thenReturn(populations);

            int result = marketValueService.getResidentialMarketValuePerCapita("15213");
            assertEquals(200, result);
        }
    }

    @Test
    void testGetAverageMarketValuePerSquareFoot_ValidData() {
        List<PropertyValue> propertyValues = Arrays.asList(
            new PropertyValue(100000.0, 1000.0, "15213"),
            new PropertyValue(200000.0, 2000.0, "15213")
        );

        try (MockedStatic<PropertyValueReader> mockedReader = mockStatic(PropertyValueReader.class)) {
            mockedReader.when(() -> PropertyValueReader.readCsvFile(propertyValueFilePath))
                .thenReturn(propertyValues);

            int result = marketValueService.getAverageMarketValuePerSquareFoot("15213");
            assertEquals(100, result);
        }
    }

    @Test
    void testGetAverageMarketValuePerSquareFoot_NullEmptyZipZeroAreaAndNullHandling() {
        List<PropertyValue> propertyValues = Arrays.asList(
            null,
            new PropertyValue(null, 1000.0, "15213"),
            new PropertyValue(100000.0, null, "15213"),
            new PropertyValue(200000.0, 0.0, "15213"),
            new PropertyValue(300000.0, 2000.0, null),
            new PropertyValue(400000.0, 3000.0, "15217")
        );

        try (MockedStatic<PropertyValueReader> mockedReader = mockStatic(PropertyValueReader.class)) {
            mockedReader.when(() -> PropertyValueReader.readCsvFile(propertyValueFilePath))
                .thenReturn(propertyValues);

            assertEquals(0, marketValueService.getAverageMarketValuePerSquareFoot(null));
            assertEquals(0, marketValueService.getAverageMarketValuePerSquareFoot(""));
            assertEquals(0, marketValueService.getAverageMarketValuePerSquareFoot("15213"));
            assertEquals(0, marketValueService.getAverageMarketValuePerSquareFoot("15219"));
        }
    }
}
