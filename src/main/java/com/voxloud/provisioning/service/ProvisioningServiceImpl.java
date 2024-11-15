package com.voxloud.provisioning.service;

import com.voxloud.provisioning.entity.Device;
import com.voxloud.provisioning.exception.NotFoundException;
import com.voxloud.provisioning.repository.DeviceRepository;
import com.voxloud.provisioning.util.ConfigFileGenerator;
import com.voxloud.provisioning.util.OverrideFragmentProcessor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProvisioningServiceImpl implements ProvisioningService {

    private final DeviceRepository deviceRepository;
    private final OverrideFragmentProcessor overrideFragmentProcessor;
    private final Map<Device.DeviceModel, ConfigFileGenerator> configFileGenerators;

    @Value("${provisioning.domain}")
    private String domain;

    @Value("${provisioning.port}")
    private String port;

    @Value("${provisioning.codecs}")
    private String codecs;

    public ProvisioningServiceImpl(DeviceRepository deviceRepository, OverrideFragmentProcessor overrideFragmentProcessor, List<ConfigFileGenerator> configFileGenerators) {
        this.deviceRepository = deviceRepository;
        this.overrideFragmentProcessor = overrideFragmentProcessor;
        this.configFileGenerators = configFileGenerators.stream().collect(Collectors.toMap(ConfigFileGenerator::getDeviceModel, cfg -> cfg));
    }

    @Override
    public String getProvisioningFile(String macAddress) {
        Device device = this.deviceRepository.findById(macAddress)
                .orElseThrow(()
                        -> new NotFoundException("Device not found with MAC address: " + macAddress));

        Map<String, Object> data = new HashMap<>();
        data.put("username", device.getUsername());
        data.put("password", device.getPassword());
        data.put("domain", domain);
        data.put("port", port);
        data.put("codecs", codecs);

        if (device.getOverrideFragment() != null && !device.getOverrideFragment().isEmpty())
            this.overrideFragmentProcessor.applyOverrideFragment(data, device.getOverrideFragment(), device.getModel());


        return this.configFileGenerators.get(device.getModel()).generateConfigFile(data);
    }
}
