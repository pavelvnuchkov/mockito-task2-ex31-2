package ru.netology.patient.service.medical;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MedicalServiceImplTest {

    @ParameterizedTest
    @CsvSource(value = {
            "1, Anna, Ivanova, 36, 120, 80, 120, 80, 0",
            "1, Anna, Ivanova, 36, 120, 80, 130, 90, 1"
    })
    void checkBloodPressure(String id, String name,
                            String surname, String temperature, String pressureHigh, String pressureLow,
                            String newPressureHigh, String newPressureLow, String expected) {

        BloodPressure bloodPressure = new BloodPressure(Integer.parseInt(pressureHigh), Integer.parseInt(pressureLow));
        BloodPressure newBloodPressure = new BloodPressure(Integer.parseInt(newPressureHigh), Integer.parseInt(newPressureLow));

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(id)).thenReturn(new PatientInfo(id, name, surname, LocalDate.now(),
                new HealthInfo(new BigDecimal(Long.parseLong(temperature)),
                        bloodPressure)));

        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkBloodPressure(id, newBloodPressure);

        Mockito.verify(sendAlertService, Mockito.times(Integer.parseInt(expected))).send(Mockito.anyString());


    }

    @ParameterizedTest
    @CsvSource(value = {
            "1, Anna, Ivanova, 36, 120, 80, 36, 0",
            "1, Anna, Ivanova, 38, 120, 80, 36, 1"
    })
    void checkTemperature(String id, String name,
                          String surname, String temperature, String pressureHigh, String pressureLow,
                          String newTemperature, String expected) {

        BloodPressure bloodPressure = new BloodPressure(Integer.parseInt(pressureHigh), Integer.parseInt(pressureLow));

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(id)).thenReturn(new PatientInfo(id, name, surname, LocalDate.now(),
                new HealthInfo(new BigDecimal(Long.parseLong(temperature)),
                        bloodPressure)));

        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);

        medicalService.checkTemperature(id, new BigDecimal(Integer.parseInt(newTemperature)));

        Mockito.verify(sendAlertService, Mockito.times(Integer.parseInt(expected))).send(Mockito.anyString());

    }

    @ParameterizedTest
    @CsvSource(value = {
            "1, Anna, Ivanova, 36, 120, 80, 130, 90",
            "5, Anna, Ivanova, 36, 120, 80, 130, 90"
    })
    void checkBloodPressureMessageTest(String id, String name,
                                       String surname, String temperature, String pressureHigh, String pressureLow,
                                       String newPressureHigh, String newPressureLow) {

        BloodPressure bloodPressure = new BloodPressure(Integer.parseInt(pressureHigh), Integer.parseInt(pressureLow));
        BloodPressure newBloodPressure = new BloodPressure(Integer.parseInt(newPressureHigh), Integer.parseInt(newPressureLow));

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(id)).thenReturn(new PatientInfo(id, name, surname, LocalDate.now(),
                new HealthInfo(new BigDecimal(Long.parseLong(temperature)),
                        bloodPressure)));

        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkBloodPressure(id, newBloodPressure);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        String expected = String.format("Warning, patient with id: %s, need help", id);

        Mockito.verify(sendAlertService).send(argumentCaptor.capture());
        assertEquals(expected, argumentCaptor.getValue());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1, Anna, Ivanova, 38, 120, 80, 36",
            "1266, Anna, Ivanova, 38, 120, 80, 36"
    })
    void checkTemperatureMessageTest(String id, String name,
                                     String surname, String temperature, String pressureHigh, String pressureLow,
                                     String newTemperature) {

        BloodPressure bloodPressure = new BloodPressure(Integer.parseInt(pressureHigh), Integer.parseInt(pressureLow));

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(id)).thenReturn(new PatientInfo(id, name, surname, LocalDate.now(),
                new HealthInfo(new BigDecimal(Long.parseLong(temperature)),
                        bloodPressure)));

        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);

        medicalService.checkTemperature(id, new BigDecimal(Integer.parseInt(newTemperature)));

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        String expected = String.format("Warning, patient with id: %s, need help", id);

        Mockito.verify(sendAlertService).send(argumentCaptor.capture());
        assertEquals(expected, argumentCaptor.getValue());

    }
}