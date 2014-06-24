package sim.dcn.entity;

import java.util.ArrayList;
import java.util.List;

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
}
