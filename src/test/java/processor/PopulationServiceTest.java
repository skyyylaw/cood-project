package processor;

import common.Population;
import data.PopulationReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PopulationServiceTest {

    private PopulationService populationService;
    private String parkingViolationFilePath;
    private String propertyValueFilePath;
    private String populationFilePath;

    @BeforeEach
    void setUp() {
        parkingViolationFilePath = "parking.json";
        propertyValueFilePath = "property.csv";
        populationFilePath = "population.txt";
        populationService = new PopulationService(parkingViolationFilePath, propertyValueFilePath, populationFilePath);
    }

    @Test
    void testGetPopulationAllZipCodes_SingleZipCode() {
        List<Population> populations = Arrays.asList(
            new Population("15213", 1000)
        );

        try (MockedStatic<PopulationReader> mockedReader = mockStatic(PopulationReader.class)) {
            mockedReader.when(() -> PopulationReader.readPopulationFile(populationFilePath))
                .thenReturn(populations);

            Long result = populationService.getPopulationAllZipCodes();
            assertEquals(1000L, result);
        }
    }

    @Test
    void testGetPopulationAllZipCodes_MultipleZipCodes() {
        List<Population> populations = Arrays.asList(
            new Population("15213", 1000),
            new Population("15217", 2000),
            new Population("15219", 3000)
        );

        try (MockedStatic<PopulationReader> mockedReader = mockStatic(PopulationReader.class)) {
            mockedReader.when(() -> PopulationReader.readPopulationFile(populationFilePath))
                .thenReturn(populations);

            Long result = populationService.getPopulationAllZipCodes();
            assertEquals(6000L, result);
        }
    }

    @Test
    void testGetPopulationAllZipCodes_EmptyList() {
        List<Population> populations = Collections.emptyList();

        try (MockedStatic<PopulationReader> mockedReader = mockStatic(PopulationReader.class)) {
            mockedReader.when(() -> PopulationReader.readPopulationFile(populationFilePath))
                .thenReturn(populations);

            Long result = populationService.getPopulationAllZipCodes();
            assertEquals(0L, result);
        }
    }

    @Test
    void testGetPopulationAllZipCodes_ZeroPopulation() {
        List<Population> populations = Arrays.asList(
            new Population("15213", 0),
            new Population("15217", 1000)
        );

        try (MockedStatic<PopulationReader> mockedReader = mockStatic(PopulationReader.class)) {
            mockedReader.when(() -> PopulationReader.readPopulationFile(populationFilePath))
                .thenReturn(populations);

            Long result = populationService.getPopulationAllZipCodes();
            assertEquals(1000L, result);
        }
    }
}
