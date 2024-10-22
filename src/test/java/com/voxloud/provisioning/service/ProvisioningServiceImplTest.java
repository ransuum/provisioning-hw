package com.voxloud.provisioning.service;

import com.voxloud.provisioning.entity.Device;
import com.voxloud.provisioning.exception.NotFoundException;
import com.voxloud.provisioning.repository.DeviceRepository;
import com.voxloud.provisioning.util.ConfigFileGenerator;
import com.voxloud.provisioning.util.OverrideFragmentProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ProvisioningServiceImplTest {

    @InjectMocks
    private ProvisioningServiceImpl provisioningService;

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private OverrideFragmentProcessor overrideFragmentProcessor;

    @Mock
    private ConfigFileGenerator configFileGenerator;

    @Value("${provisioning.domain}")
    private String domain;

    @Value("${provisioning.port}")
    private String port;

    @Value("${provisioning.codecs}")
    private String codecs;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetProvisioningFile_DeviceNotFound() {
        String macAddress = "aa-bb-cc-dd-ee-ff";
        when(deviceRepository.findById(macAddress)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            provisioningService.getProvisioningFile(macAddress);
        });

        Assertions.assertEquals("Device not found with MAC address: " + macAddress, exception.getMessage());
    }

    @Test
    public void testGetProvisioningFile_WithoutOverrideFragment() {
        final String macAddress = "aa-bb-cc-dd-ee-ff";

        Device device = new Device();
        device.setMacAddress(macAddress);
        device.setUsername("john");
        device.setPassword("doe");
        device.setModel(Device.DeviceModel.DESK);
        device.setOverrideFragment(null);

        when(deviceRepository.findById(macAddress)).thenReturn(Optional.of(device));

        Map<String, Object> expectedData = new HashMap<>();
        expectedData.put("username", "john");
        expectedData.put("password", "doe");
        expectedData.put("domain", domain);
        expectedData.put("port", port);
        expectedData.put("codecs", codecs);

        when(configFileGenerator.generateConfigFile(any(), eq(Device.DeviceModel.DESK)))
                .thenReturn("config-file-content");

        String result = provisioningService.getProvisioningFile(macAddress);

        verify(overrideFragmentProcessor, never()).applyOverrideFragment(any(), anyString(), any());
        Assertions.assertEquals("config-file-content", result);
    }

    @Test
    public void testGetProvisioningFile_WithOverrideFragment() {
        final String macAddress = "1a-2b-3c-4d-5e-6f";

        Device device = new Device();
        device.setMacAddress(macAddress);
        device.setUsername("eric");
        device.setPassword("blue");
        device.setModel(Device.DeviceModel.CONFERENCE);
        device.setOverrideFragment("{\"domain\":\"sip.anotherdomain.com\",\"port\":\"5161\",\"timeout\":10}");

        when(deviceRepository.findById(macAddress)).thenReturn(Optional.of(device));

        Map<String, Object> expectedData = new HashMap<>();
        expectedData.put("username", "eric");
        expectedData.put("password", "blue");
        expectedData.put("domain", "sip.defaultdomain.com");
        expectedData.put("port", "5060");
        expectedData.put("codecs", codecs);

        doAnswer(invocation -> {
            Map<String, Object> config = invocation.getArgument(0);
            config.put("domain", "sip.anotherdomain.com");
            config.put("port", "5161");
            return null;
        }).when(overrideFragmentProcessor).applyOverrideFragment(any(), anyString(), eq(Device.DeviceModel.CONFERENCE));

        when(configFileGenerator.generateConfigFile(any(), eq(Device.DeviceModel.CONFERENCE)))
                .thenReturn("conference-config-content");

        String result = provisioningService.getProvisioningFile(macAddress);

        verify(overrideFragmentProcessor).applyOverrideFragment(any(), anyString(), eq(Device.DeviceModel.CONFERENCE));
        Assertions.assertEquals("conference-config-content", result);
    }
}