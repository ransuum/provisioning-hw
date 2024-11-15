package com.voxloud.provisioning.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voxloud.provisioning.entity.Device;
import com.voxloud.provisioning.exception.GeneratingFileException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@Component
public class GenerateConfigFileCONFERENCE implements ConfigFileGenerator {
    private final Device.DeviceModel deviceModel = Device.DeviceModel.CONFERENCE;
    private final ObjectMapper objectMapper;

    public GenerateConfigFileCONFERENCE(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String generateConfigFile(Map<String, Object> data) {
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

    @Override
    public Device.DeviceModel getDeviceModel() {
        return this.deviceModel;
    }
}
