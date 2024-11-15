package com.voxloud.provisioning.util;

import com.voxloud.provisioning.entity.Device;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GenerateConfigFileDesk implements ConfigFileGenerator {
    private final Device.DeviceModel deviceModel = Device.DeviceModel.DESK;

    @Override
    public String generateConfigFile(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder();
        data.forEach((field, value) -> {
            sb.append(field).append("=").append(value).append("\n");
        });
        return sb.toString();
    }

    @Override
    public Device.DeviceModel getDeviceModel() {
        return this.deviceModel;
    }
}
