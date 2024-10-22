package com.voxloud.provisioning.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voxloud.provisioning.entity.Device;
import com.voxloud.provisioning.exception.GeneratingFileException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@Component
public class ConfigFileGenerator {
    private final ObjectMapper objectMapper;

    public ConfigFileGenerator(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String generateConfigFile(Map<String, Object> data, Device.DeviceModel model) {
        if (model == Device.DeviceModel.DESK) return generatePropertyFile(data);
        else if (model == Device.DeviceModel.CONFERENCE) return generateJsonFile(data);
        throw new UnsupportedOperationException("Unsupported device model: " + model);
    }

    private String generatePropertyFile(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder();
        data.forEach((field, value) -> {
            sb.append(field).append("=").append(value).append("\n");
        });
        return sb.toString();
    }

    private String generateJsonFile(Map<String, Object> data) {
        try {
            if (data.containsKey("codecs")) {
                String codecsString = data.get("codecs").toString();
                data.put("codecs", Arrays.asList(codecsString.split(",")));
            }
            return this.objectMapper.writeValueAsString(data);
        } catch (IOException e) {
            throw new GeneratingFileException("Error generating JSON file", e);
        }
    }
}
