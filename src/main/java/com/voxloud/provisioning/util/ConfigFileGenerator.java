package com.voxloud.provisioning.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voxloud.provisioning.entity.Device;
import com.voxloud.provisioning.exception.GeneratingFileException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@Component
public interface ConfigFileGenerator {

    String generateConfigFile(Map<String, Object> data);

    Device.DeviceModel getDeviceModel();
}
