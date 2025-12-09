package processor;

import common.ParkingViolation;
import common.Population;
import data.ParkingViolationReader;
import data.PopulationReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FinesServiceTest {

    private FinesService finesService;
    private String parkingViolationFilePath;
    private String propertyValueFilePath;
    private String populationFilePath;

    @BeforeEach
    void setUp() {
        parkingViolationFilePath = "parking.json";
        propertyValueFilePath = "property.csv";
        populationFilePath = "population.txt";
        finesService = new FinesService(parkingViolationFilePath, propertyValueFilePath, populationFilePath);
    }

    @Test
    void testGetFinesPerCapitaPerZipCode_ValidData() {
        List<ParkingViolation> parkingViolations = Arrays.asList(
            new ParkingViolation(Instant.now(), 50.0, "Violation1", "ABC123", "PA", "T1", "15213"),
            new ParkingViolation(Instant.now(), 75.0, "Violation2", "DEF456", "PA", "T2", "15213"),
            new ParkingViolation(Instant.now(), 100.0, "Violation3", "GHI789", "PA", "T3", "15217")
        );

        List<Population> populations = Arrays.asList(
            new Population("15213", 1000),
            new Population("15217", 2000)
        );

        try (MockedStatic<ParkingViolationReader> mockedParkingReader = mockStatic(ParkingViolationReader.class);
             MockedStatic<PopulationReader> mockedPopulationReader = mockStatic(PopulationReader.class)) {

            mockedParkingReader.when(() -> ParkingViolationReader.readFile(parkingViolationFilePath))
                .thenReturn(parkingViolations);
            mockedPopulationReader.when(() -> PopulationReader.readPopulationFile(populationFilePath))
                .thenReturn(populations);

            Map<String, Double> result = finesService.getFinesPerCapitaPerZipCode();

            assertEquals(2, result.size());
            assertEquals(0.125, result.get("15213"), 0.001);
            assertEquals(0.05, result.get("15217"), 0.001);
        }
    }

    @Test
    void testGetFinesPerCapitaPerZipCode_FilterNonPAStates() {
        List<ParkingViolation> parkingViolations = Arrays.asList(
            new ParkingViolation(Instant.now(), 50.0, "Violation1", "ABC123", null, "T1", "15213"),
            new ParkingViolation(Instant.now(), 75.0, "Violation2", "DEF456", "   ", "T2", "15213"),
            new ParkingViolation(Instant.now(), 100.0, "Violation3", "GHI789", "NY", "T3", "15213"),
            new ParkingViolation(Instant.now(), 125.0, "Violation4", "JKL012", "PA", "T4", "15213")
        );

        List<Population> populations = Arrays.asList(new Population("15213", 1000));

        try (MockedStatic<ParkingViolationReader> mockedParkingReader = mockStatic(ParkingViolationReader.class);
             MockedStatic<PopulationReader> mockedPopulationReader = mockStatic(PopulationReader.class)) {

            mockedParkingReader.when(() -> ParkingViolationReader.readFile(parkingViolationFilePath))
                .thenReturn(parkingViolations);
            mockedPopulationReader.when(() -> PopulationReader.readPopulationFile(populationFilePath))
                .thenReturn(populations);

            Map<String, Double> result = finesService.getFinesPerCapitaPerZipCode();

            assertEquals(1, result.size());
            assertEquals(0.125, result.get("15213"), 0.001);
        }
    }

    @Test
    void testGetFinesPerCapitaPerZipCode_FilterNullEmptyZipCodes() {
        List<ParkingViolation> parkingViolations = Arrays.asList(
            new ParkingViolation(Instant.now(), 50.0, "Violation1", "ABC123", "PA", "T1", null),
            new ParkingViolation(Instant.now(), 75.0, "Violation2", "DEF456", "PA", "T2", ""),
            new ParkingViolation(Instant.now(), 100.0, "Violation3", "GHI789", "PA", "T3", "15213")
        );

        List<Population> populations = Arrays.asList(new Population("15213", 1000));

        try (MockedStatic<ParkingViolationReader> mockedParkingReader = mockStatic(ParkingViolationReader.class);
             MockedStatic<PopulationReader> mockedPopulationReader = mockStatic(PopulationReader.class)) {

            mockedParkingReader.when(() -> ParkingViolationReader.readFile(parkingViolationFilePath))
                .thenReturn(parkingViolations);
            mockedPopulationReader.when(() -> PopulationReader.readPopulationFile(populationFilePath))
                .thenReturn(populations);

            Map<String, Double> result = finesService.getFinesPerCapitaPerZipCode();

            assertEquals(1, result.size());
            assertEquals(0.1, result.get("15213"), 0.001);
        }
    }

    @Test
    void testGetFinesPerCapitaPerZipCode_FilterZeroFinesAndZeroPopulations() {
        List<ParkingViolation> parkingViolations = Collections.emptyList();
        List<Population> populations = Arrays.asList(
            new Population("15213", 0),
            new Population("15217", 1000)
        );

        try (MockedStatic<ParkingViolationReader> mockedParkingReader = mockStatic(ParkingViolationReader.class);
             MockedStatic<PopulationReader> mockedPopulationReader = mockStatic(PopulationReader.class)) {

            mockedParkingReader.when(() -> ParkingViolationReader.readFile(parkingViolationFilePath))
                .thenReturn(parkingViolations);
            mockedPopulationReader.when(() -> PopulationReader.readPopulationFile(populationFilePath))
                .thenReturn(populations);

            Map<String, Double> result = finesService.getFinesPerCapitaPerZipCode();

            assertTrue(result.isEmpty());
        }
    }

    @Test
    void testGetFinesPerCapitaPerZipCode_FilterNullPopulation() {
        List<ParkingViolation> parkingViolations = Arrays.asList(
            new ParkingViolation(Instant.now(), 50.0, "Violation1", "ABC123", "PA", "T1", "15213")
        );
        List<Population> populations = Arrays.asList(new Population("15217", 1000));

        try (MockedStatic<ParkingViolationReader> mockedParkingReader = mockStatic(ParkingViolationReader.class);
             MockedStatic<PopulationReader> mockedPopulationReader = mockStatic(PopulationReader.class)) {

            mockedParkingReader.when(() -> ParkingViolationReader.readFile(parkingViolationFilePath))
                .thenReturn(parkingViolations);
            mockedPopulationReader.when(() -> PopulationReader.readPopulationFile(populationFilePath))
                .thenReturn(populations);

            Map<String, Double> result = finesService.getFinesPerCapitaPerZipCode();

            assertTrue(result.isEmpty());
        }
    }

    @Test
    void testGetFinesPerCapitaPerZipCode_EmptyLists() {
        List<ParkingViolation> parkingViolations = Collections.emptyList();
        List<Population> populations = Collections.emptyList();

        try (MockedStatic<ParkingViolationReader> mockedParkingReader = mockStatic(ParkingViolationReader.class);
             MockedStatic<PopulationReader> mockedPopulationReader = mockStatic(PopulationReader.class)) {

            mockedParkingReader.when(() -> ParkingViolationReader.readFile(parkingViolationFilePath))
                .thenReturn(parkingViolations);
            mockedPopulationReader.when(() -> PopulationReader.readPopulationFile(populationFilePath))
                .thenReturn(populations);

            Map<String, Double> result = finesService.getFinesPerCapitaPerZipCode();

            assertTrue(result.isEmpty());
        }
    }

    @Test
    void testGetFinesPerCapitaPerZipCode_NullTotalFineCheck() {
        // Test case where we have violations but they result in zero total fine
        // This exercises the totalFine == null check (though it shouldn't be null in practice)
        // and the totalFine == 0.0 check
        List<ParkingViolation> parkingViolations = Arrays.asList(
            new ParkingViolation(Instant.now(), 0.0, "Violation1", "ABC123", "PA", "T1", "15213")
        );
        List<Population> populations = Arrays.asList(new Population("15213", 1000));

        try (MockedStatic<ParkingViolationReader> mockedParkingReader = mockStatic(ParkingViolationReader.class);
             MockedStatic<PopulationReader> mockedPopulationReader = mockStatic(PopulationReader.class)) {

            mockedParkingReader.when(() -> ParkingViolationReader.readFile(parkingViolationFilePath))
                .thenReturn(parkingViolations);
            mockedPopulationReader.when(() -> PopulationReader.readPopulationFile(populationFilePath))
                .thenReturn(populations);

            Map<String, Double> result = finesService.getFinesPerCapitaPerZipCode();

            // Should be empty because totalFine is 0.0
            assertTrue(result.isEmpty());
        }
    }

    @Test
    void testGetFinesPerCapitaPerZipCode_ZeroPopulationCheck() {
        // Test case to ensure zero population is properly filtered
        List<ParkingViolation> parkingViolations = Arrays.asList(
            new ParkingViolation(Instant.now(), 50.0, "Violation1", "ABC123", "PA", "T1", "15213")
        );
        List<Population> populations = Arrays.asList(new Population("15213", 0));

        try (MockedStatic<ParkingViolationReader> mockedParkingReader = mockStatic(ParkingViolationReader.class);
             MockedStatic<PopulationReader> mockedPopulationReader = mockStatic(PopulationReader.class)) {

            mockedParkingReader.when(() -> ParkingViolationReader.readFile(parkingViolationFilePath))
                .thenReturn(parkingViolations);
            mockedPopulationReader.when(() -> PopulationReader.readPopulationFile(populationFilePath))
                .thenReturn(populations);

            Map<String, Double> result = finesService.getFinesPerCapitaPerZipCode();

            assertTrue(result.isEmpty());
        }
    }

    @Test
    void testGetFinesPerCapitaPerZipCode_AccumulationAndMultipleZipCodes() {
        // Test case to ensure getOrDefault works for first occurrence and accumulation works
        // This covers the getOrDefault default value path (0.0) when zipCode is first encountered
        List<ParkingViolation> parkingViolations = Arrays.asList(
            new ParkingViolation(Instant.now(), 25.0, "Violation1", "ABC123", "PA", "T1", "15213"),
            new ParkingViolation(Instant.now(), 50.0, "Violation2", "DEF456", "PA", "T2", "15213"),
            new ParkingViolation(Instant.now(), 100.0, "Violation3", "GHI789", "PA", "T3", "15217"),
            new ParkingViolation(Instant.now(), 200.0, "Violation4", "JKL012", "PA", "T4", "15217")
        );

        List<Population> populations = Arrays.asList(
            new Population("15213", 1000),
            new Population("15217", 2000)
        );

        try (MockedStatic<ParkingViolationReader> mockedParkingReader = mockStatic(ParkingViolationReader.class);
             MockedStatic<PopulationReader> mockedPopulationReader = mockStatic(PopulationReader.class)) {

            mockedParkingReader.when(() -> ParkingViolationReader.readFile(parkingViolationFilePath))
                .thenReturn(parkingViolations);
            mockedPopulationReader.when(() -> PopulationReader.readPopulationFile(populationFilePath))
                .thenReturn(populations);

            Map<String, Double> result = finesService.getFinesPerCapitaPerZipCode();

            assertEquals(2, result.size());
            assertEquals(0.075, result.get("15213"), 0.001); // (25 + 50) / 1000 = 0.075
            assertEquals(0.15, result.get("15217"), 0.001); // (100 + 200) / 2000 = 0.15
        }
    }

    @Test
    void testGetFinesPerCapitaPerZipCode_WhitespaceTrimming() {
        // Test case to ensure trim() is called on state with whitespace
        // This covers the trim().equals("PA") path when state has whitespace around "PA"
        List<ParkingViolation> parkingViolations = Arrays.asList(
            new ParkingViolation(Instant.now(), 50.0, "Violation1", "ABC123", "  PA  ", "T1", "15213"),
            new ParkingViolation(Instant.now(), 75.0, "Violation2", "DEF456", "PA ", "T2", "15213"),
            new ParkingViolation(Instant.now(), 100.0, "Violation3", "GHI789", " PA", "T3", "15213")
        );

        List<Population> populations = Arrays.asList(
            new Population("15213", 1000)
        );

        try (MockedStatic<ParkingViolationReader> mockedParkingReader = mockStatic(ParkingViolationReader.class);
             MockedStatic<PopulationReader> mockedPopulationReader = mockStatic(PopulationReader.class)) {

            mockedParkingReader.when(() -> ParkingViolationReader.readFile(parkingViolationFilePath))
                .thenReturn(parkingViolations);
            mockedPopulationReader.when(() -> PopulationReader.readPopulationFile(populationFilePath))
                .thenReturn(populations);

            Map<String, Double> result = finesService.getFinesPerCapitaPerZipCode();

            assertEquals(1, result.size());
            assertEquals(0.225, result.get("15213"), 0.001); // (50 + 75 + 100) / 1000 = 0.225
        }
    }
}
