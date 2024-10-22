package com.voxloud.provisioning.controller;

import com.voxloud.provisioning.service.ProvisioningService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ProvisioningController {
    private final ProvisioningService provisioningService;

    public ProvisioningController(ProvisioningService provisioningService) {
        this.provisioningService = provisioningService;
    }

    @GetMapping("/provisioning/{macAddress}")
    public ResponseEntity<String> getProvisioningFile(@PathVariable String macAddress) {
        String provisioningFile = this.provisioningService.getProvisioningFile(macAddress);

        return ResponseEntity.ok(provisioningFile);
    }
}