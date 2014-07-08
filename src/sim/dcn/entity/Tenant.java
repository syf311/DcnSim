package sim.dcn.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tenant extends Entity {
	
	private Map<Integer, List<VirtualMachine>> groupOfVirtualMachines; 
	
	public Tenant(int id) {
		super(id);
		
		this.groupOfVirtualMachines = new HashMap<Integer, List<VirtualMachine>>();
	}
	
	public void addVirtualMachine(VirtualMachine virtualMachine) {
		Integer groupId = new Integer(virtualMachine.getGroupId());
		
		if (this.groupOfVirtualMachines.containsKey(groupId)) {
			this.groupOfVirtualMachines.get(groupId).add(virtualMachine); 
		} else {
			List<VirtualMachine> virtualMachines =
					new ArrayList<VirtualMachine>();
			virtualMachines.add(virtualMachine);
			this.groupOfVirtualMachines.put(groupId, virtualMachines);
		}
	}
	
	public VirtualMachine getVirtualMachine(Integer groupId, int virtualMachineId) {
		VirtualMachine virtualMachine = null;
		
		List<VirtualMachine> group = this.getVirtualMachines(groupId);
		virtualMachine = Entity.getEntity(
				group, 
				virtualMachineId, 
				VirtualMachine.class.getSimpleName());
		
		return virtualMachine;
	}
	
	public int getGroupCount(Integer groupId) {
		return this.getVirtualMachines(groupId).size();
	}
	
	public List<VirtualMachine> getVirtualMachines(Integer groupId) {
		List<VirtualMachine> virtualMachines = null;
		if (this.groupOfVirtualMachines.containsKey(groupId)) {
			virtualMachines = this.groupOfVirtualMachines.get(groupId);
		} else {
			throw new IllegalStateException(
					String.format("Cannot find group %d in tenant %d", groupId.intValue(), this.id));
		}
		
		return virtualMachines;
	}
}
