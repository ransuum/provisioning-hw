package com.voxloud.provisioning.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voxloud.provisioning.entity.Device;
import com.voxloud.provisioning.exception.GeneratingFileException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.Properties;

@Component
public class OverrideFragmentProcessor {
    private final ObjectMapper objectMapper;

    public OverrideFragmentProcessor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void applyOverrideFragment(Map<String, Object> data, String overrideFragment, Device.DeviceModel model) {
        try {
            if (model == Device.DeviceModel.DESK) {
                Properties properties = new Properties();
                properties.load(new StringReader(overrideFragment));

                String domain = properties.getProperty("domain");
                String port = properties.getProperty("port");
                String timeout = properties.getProperty("timeout");

                data.put("domain", (!domain.trim().isEmpty()) ? domain.trim() : null);
                data.put("port", (!port.trim().isEmpty()) ? port.trim() : null);
                data.put("timeout", (!timeout.trim().isEmpty()) ? timeout.trim() : null);
            } else if (model == Device.DeviceModel.CONFERENCE)
                data.putAll(this.objectMapper.readValue(overrideFragment, Map.class));
        } catch (IOException e) {
            throw new GeneratingFileException("Error applying override fragment", e);
        }
    }
}
