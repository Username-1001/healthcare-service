package ru.netology.patient.service.medical;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MedicalServiceImplTest {
    MedicalService medicalService;
    PatientInfoRepository patientInfoRepository;
    SendAlertService sendAlertService;
    HealthInfo healthInfo;
    PatientInfo patientInfo;
    BloodPressure bloodPressureNormal = new BloodPressure(120, 80);

    @BeforeEach
    void setUp() {
        patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        sendAlertService = Mockito.mock(SendAlertService.class);
        medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        patientInfo = Mockito.spy(PatientInfo.class);
        healthInfo = Mockito.spy(HealthInfo.class);

        Mockito.when(patientInfoRepository.getById(Mockito.any()))
                .thenReturn(patientInfo);
        Mockito.when(patientInfo.getHealthInfo())
                .thenReturn(healthInfo);
        Mockito.when(healthInfo.getBloodPressure())
                .thenReturn(bloodPressureNormal);
        Mockito.when(healthInfo.getNormalTemperature())
                .thenReturn(new BigDecimal("36.6"));
    }

    @Test
    void checkBloodPressure_with_message() {
        BloodPressure bloodPressure = new BloodPressure(140, 100);
        medicalService.checkBloodPressure(Mockito.any(), bloodPressure);
        Mockito.verify(sendAlertService, Mockito.times(1)).send(Mockito.any());
    }

    @Test
    void checkTemperature_with_message() {
        BigDecimal temperature = new BigDecimal("34");
        medicalService.checkTemperature(Mockito.any(), temperature);
        Mockito.verify(sendAlertService, Mockito.times(1)).send(Mockito.any());
    }

    @Test
    void checkBloodPressure_normal() {
        medicalService.checkBloodPressure(Mockito.any(), bloodPressureNormal);
        Mockito.verify(sendAlertService, Mockito.times(0)).send(Mockito.any());
    }

    @Test
    void checkTemperature_with_normal() {
        BigDecimal temperature = new BigDecimal("36");
        medicalService.checkTemperature(Mockito.any(), temperature);
        Mockito.verify(sendAlertService, Mockito.times(0)).send(Mockito.any());
    }
}