package sim.dcn.entity;

import java.util.ArrayList;
import java.util.List;

import sim.common.ValidationHelper;

public class Server extends NetworkComponent {
	
	private List<VirtualMachine> virtualMachines;
	
	public Server(int id) {
		super(id, new ArrayList<Link>());
		this.virtualMachines = new ArrayList<VirtualMachine>();
	}
	
	@Override
	public String getType() {
		return "Server";
	}
	
	public List<VirtualMachine> getVirtualMachines() {
		return this.virtualMachines;
	}
	
	public void addVirtualMachine(VirtualMachine virtualMachine) {
		ValidationHelper.notNull(virtualMachine, "virtualMachine");
		this.virtualMachines.add(virtualMachine);
	}
}
