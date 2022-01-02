package de.bender.fn.quarkus.vms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VirtualMachine {
    private String name;
    private String status;
    private String resourceGroup;
}
