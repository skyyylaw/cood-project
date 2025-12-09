package processor;

import common.PropertyValue;
import data.PropertyValueReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ResidentialAreaServiceTest {

    private ResidentialAreaService residentialAreaService;
    private String parkingViolationFilePath;
    private String propertyValueFilePath;
    private String populationFilePath;

    @BeforeEach
    void setUp() {
        parkingViolationFilePath = "parking.json";
        propertyValueFilePath = "property.csv";
        populationFilePath = "population.txt";
        residentialAreaService = new ResidentialAreaService(parkingViolationFilePath, propertyValueFilePath, populationFilePath);
    }

    @Test
    void testGetAverageResidentialArea_ValidData() {
        List<PropertyValue> propertyValues = Arrays.asList(
            new PropertyValue(100000.0, 1000.0, "15213"),
            new PropertyValue(200000.0, 2000.0, "15213"),
            new PropertyValue(300000.0, 3000.0, "15213")
        );

        try (MockedStatic<PropertyValueReader> mockedReader = mockStatic(PropertyValueReader.class)) {
            mockedReader.when(() -> PropertyValueReader.readCsvFile(propertyValueFilePath))
                .thenReturn(propertyValues);

            int result = residentialAreaService.getAverageResidentialArea("15213");
            assertEquals(2000, result);
        }
    }

    @Test
    void testGetAverageResidentialArea_NullEmptyZipAndNoMatch() {
        List<PropertyValue> propertyValues = Arrays.asList(
            new PropertyValue(100000.0, 1000.0, "15217")
        );

        try (MockedStatic<PropertyValueReader> mockedReader = mockStatic(PropertyValueReader.class)) {
            mockedReader.when(() -> PropertyValueReader.readCsvFile(propertyValueFilePath))
                .thenReturn(propertyValues);

            assertEquals(0, residentialAreaService.getAverageResidentialArea(null));
            assertEquals(0, residentialAreaService.getAverageResidentialArea(""));
            assertEquals(0, residentialAreaService.getAverageResidentialArea("15213"));
        }
    }

    @Test
    void testGetAverageResidentialArea_NullHandling() {
        List<PropertyValue> propertyValues = Arrays.asList(
            null,
            new PropertyValue(100000.0, null, "15213"),
            new PropertyValue(200000.0, 2000.0, null),
            new PropertyValue(300000.0, 3000.0, "15213")
        );

        try (MockedStatic<PropertyValueReader> mockedReader = mockStatic(PropertyValueReader.class)) {
            mockedReader.when(() -> PropertyValueReader.readCsvFile(propertyValueFilePath))
                .thenReturn(propertyValues);

            int result = residentialAreaService.getAverageResidentialArea("15213");
            assertEquals(3000, result);
        }
    }

    @Test
    void testGetMinAndMaxLivableAreas_ValidData() {
        List<PropertyValue> propertyValues = Arrays.asList(
            new PropertyValue(100000.0, 1000.0, "15213"),
            new PropertyValue(200000.0, 3000.0, "15213"),
            new PropertyValue(300000.0, 2000.0, "15213")
        );

        try (MockedStatic<PropertyValueReader> mockedReader = mockStatic(PropertyValueReader.class)) {
            mockedReader.when(() -> PropertyValueReader.readCsvFile(propertyValueFilePath))
                .thenReturn(propertyValues);

            int[] result = residentialAreaService.getMinAndMaxLivableAreas("15213");
            assertNotNull(result);
            assertEquals(2, result.length);
            assertEquals(1000, result[0]);
            assertEquals(3000, result[1]);
        }
    }

    @Test
    void testGetMinAndMaxLivableAreas_SingleProperty() {
        List<PropertyValue> propertyValues = Arrays.asList(
            new PropertyValue(100000.0, 1500.0, "15213")
        );

        try (MockedStatic<PropertyValueReader> mockedReader = mockStatic(PropertyValueReader.class)) {
            mockedReader.when(() -> PropertyValueReader.readCsvFile(propertyValueFilePath))
                .thenReturn(propertyValues);

            int[] result = residentialAreaService.getMinAndMaxLivableAreas("15213");
            assertNotNull(result);
            assertEquals(2, result.length);
            assertEquals(1500, result[0]);
            assertEquals(1500, result[1]);
        }
    }

    @Test
    void testGetMinAndMaxLivableAreas_NullEmptyZipAndNoMatch() {
        List<PropertyValue> propertyValues = Arrays.asList(
            new PropertyValue(100000.0, 1000.0, "15217")
        );

        try (MockedStatic<PropertyValueReader> mockedReader = mockStatic(PropertyValueReader.class)) {
            mockedReader.when(() -> PropertyValueReader.readCsvFile(propertyValueFilePath))
                .thenReturn(propertyValues);

            int[] result1 = residentialAreaService.getMinAndMaxLivableAreas(null);
            assertNotNull(result1);
            assertEquals(2, result1.length);
            assertEquals(0, result1[0]);
            assertEquals(0, result1[1]);

            int[] result2 = residentialAreaService.getMinAndMaxLivableAreas("");
            assertNotNull(result2);
            assertEquals(2, result2.length);
            assertEquals(0, result2[0]);
            assertEquals(0, result2[1]);

            int[] result3 = residentialAreaService.getMinAndMaxLivableAreas("15213");
            assertNotNull(result3);
            assertEquals(2, result3.length);
            assertEquals(0, result3[0]);
            assertEquals(0, result3[1]);
        }
    }

    @Test
    void testGetMinAndMaxLivableAreas_NullHandling() {
        List<PropertyValue> propertyValues = Arrays.asList(
            null,
            new PropertyValue(100000.0, null, "15213"),
            new PropertyValue(200000.0, 2000.0, null),
            new PropertyValue(300000.0, 3000.0, "15213")
        );

        try (MockedStatic<PropertyValueReader> mockedReader = mockStatic(PropertyValueReader.class)) {
            mockedReader.when(() -> PropertyValueReader.readCsvFile(propertyValueFilePath))
                .thenReturn(propertyValues);

            int[] result = residentialAreaService.getMinAndMaxLivableAreas("15213");
            assertNotNull(result);
            assertEquals(2, result.length);
            assertEquals(3000, result[0]);
            assertEquals(3000, result[1]);
        }
    }
}
