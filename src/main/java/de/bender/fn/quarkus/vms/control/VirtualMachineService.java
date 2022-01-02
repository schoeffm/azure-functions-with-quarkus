package de.bender.fn.quarkus.vms.control;

import com.azure.core.credential.TokenCredential;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.profile.AzureProfile;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.resourcemanager.AzureResourceManager;
import com.azure.resourcemanager.resources.fluentcore.arm.models.HasName;
import de.bender.fn.quarkus.vms.model.VirtualMachine;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class VirtualMachineService {
    private final AzureResourceManager azureResourceManager;

    @Inject
    Logger log;

    public VirtualMachineService() {
        // use the standard-azure profile (we're not in china nor do we use the "german" cloud)
        AzureProfile profile = new AzureProfile(AzureEnvironment.AZURE);

        // retrieve the Service-Principal credentials from these env-variables
        // AZURE_CLIENT_ID, AZURE_CLIENT_SECRET, AZURE_TENANT_ID
        TokenCredential credential = new DefaultAzureCredentialBuilder()
                .authorityHost(profile.getEnvironment().getActiveDirectoryEndpoint())
                .build();

        // use the profile and the token settings to create a resource-manager instance
        azureResourceManager = AzureResourceManager
                .authenticate(credential, profile)
                .withDefaultSubscription();
    }

    /**
     * @return list of _all_ virtual machines of this subscription (determined by the used service-principal)
     */
    public List<VirtualMachine> retrieveAllVirtualMachines() {
        log.info("Retrieving all VMs");
        List<String> resourceGroups = azureResourceManager
                .resourceGroups()
                .list()
                .stream()
                .map(HasName::name)
                .collect(Collectors.toList());

        List<VirtualMachine> vmList = new ArrayList<>();

        for (String group : resourceGroups) {
            azureResourceManager
                    .virtualMachines()
                    .listByResourceGroup(group)
                    .forEach(vm -> vmList.add(new VirtualMachine(vm.name(), vm.powerState().toString(), group)));
        }

        return vmList;
    }

    public void startVM(VirtualMachine vmDTO) {
        log.info("Starting VM: " + vmDTO);
        azureResourceManager
                .virtualMachines()
                .start(vmDTO.getResourceGroup(), vmDTO.getName());
    }

    public void stopVM(VirtualMachine vmDTO) {
        log.info("Stopping VM: " + vmDTO);
        azureResourceManager
                .virtualMachines()
                .deallocate(vmDTO.getResourceGroup(), vmDTO.getName());
    }
}
