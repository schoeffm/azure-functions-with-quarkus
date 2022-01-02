package de.bender.fn.quarkus.vms.boundary;

import de.bender.fn.quarkus.vms.control.VirtualMachineService;
import de.bender.fn.quarkus.vms.model.VirtualMachine;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/vms")
public class VirtualMachineResource {

    @Inject
    VirtualMachineService vmManager;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<VirtualMachine> getAllVirtualMachines() {
        return vmManager.retrieveAllVirtualMachines();
    }

    @POST
    @Path("/start")
    @Consumes(MediaType.APPLICATION_JSON)
    public void startVM(VirtualMachine virtualMachine){
        vmManager.startVM(virtualMachine);
    }

    @POST
    @Path("/stop")
    @Consumes(MediaType.APPLICATION_JSON)
    public void stopVM(VirtualMachine virtualMachine){
        vmManager.stopVM(virtualMachine);
    }
}
