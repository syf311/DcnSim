package sim.dcn.setup;

import sim.common.ValidationHelper;
import sim.dcn.entity.DataCenter;
import sim.dcn.entity.Server;
import sim.dcn.entity.Tenant;
import sim.dcn.entity.VirtualMachine;

public class DefaultSimulationSetup implements SimulationSetup {
	
	@Override
	public DataCenter setupSimulation(DataCenter dataCenter) {
		ValidationHelper.notNull(dataCenter, "dataCenter");
		
		this.setupTenants(dataCenter);
		
		return dataCenter;
	}
	
	private void setupTenants(DataCenter dataCenter) {
		Tenant tenant1 = new Tenant(1);	// tenant B
		dataCenter.addTenant(tenant1);
		this.setupVirtualMachines(
				dataCenter, 
				tenant1, 
				new ConstantNumericValueGenerator(40), 
				1, 
				new GroupRequestsGenerator(
						new ConstantNumericValueGenerator(80), // request bandwidth constantly assigned with 80
						new RandomNumericValueGenerator(1, 10)));	// request cycle randomly chosen from 1 ~ 10
		
		this.setupVirtualMachines(
				dataCenter, 
				tenant1, 
				new ConstantNumericValueGenerator(20), 
				2, 
				new GroupRequestsGenerator(
						new RandomNumericValueGenerator(1, 100), // request bandwidth randomly chosen from 1 ~ 100
						new RandomNumericValueGenerator(1, 10))); // request cycle randomly chosen from 1 ~ 10
		
		Tenant tenant2 = new Tenant(2); // tenant A
		dataCenter.addTenant(tenant2);
		this.setupVirtualMachines(
				dataCenter, 
				tenant2, 
				new RandomNumericValueGenerator(30, 40), 
				1, 
				new RegularRequestsGenerator(
						new RandomNumericValueGenerator(60, 70), // request bandwidth
						new RandomNumericValueGenerator(1, 10), // request cycle
						dataCenter.getServers().size() / 2));	// how many machines that requests will be issued to
	}
	
	private void setupVirtualMachines(
			DataCenter dataCenter, 
			Tenant tenant, 
			NumericValueGenerator minGuaranteedBandwidthGenerator, 
			int groupId,
			RequestsGenerator requestGenerator) {		
		for (int i = 0; i < dataCenter.getServers().size(); i++) {
			Server server = dataCenter.getServers().get(i);
			
			// using server Id as virtual machine Id
			// due to the fact that servers are created left to right with Id from 1
			// Vms for same tenant same group should also have this properties
			// And communication pattern would be based off this
			VirtualMachine virtualMachine = new VirtualMachine(server.getId(), tenant, minGuaranteedBandwidthGenerator.nextDoubleValue(), groupId, requestGenerator);
			virtualMachine.deployToServer(server);
		}
	}
}
