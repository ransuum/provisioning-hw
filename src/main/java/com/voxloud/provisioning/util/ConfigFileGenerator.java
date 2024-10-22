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
       switch (model){
           case DESK : return generateProperty(data);
           case CONFERENCE : return generateJson(data);
           default : throw new UnsupportedOperationException("Unsupported device model: " + model);
       }
    }

    private String generateProperty(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder();
        data.forEach((field, value) -> {
            sb.append(field).append("=").append(value).append("\n");
        });
        return sb.toString();
    }

    private String generateJson(Map<String, Object> data) {
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
