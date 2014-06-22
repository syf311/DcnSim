package sim.dcn.entity;

import java.util.List;

import sim.common.ValidationHelper;

public class DataCenter {
	private List<VirtualMachine> virtualMachines;
	
	private List<Switch> switches;
	
	private List<Link> links;
	
	public DataCenter(List<VirtualMachine> virtualMachines, List<Switch> switches, List<Link> links) {
		ValidationHelper.notNullOrEmpty(virtualMachines, "virtualMachines");
		ValidationHelper.notNullOrEmpty(switches, "switches");
		ValidationHelper.notNullOrEmpty(links, "links");
		
		this.virtualMachines = virtualMachines;
		this.switches = switches;
		this.links = links;
	}
	
	public List<VirtualMachine> getVirtualMachines() {
		return this.virtualMachines;
	}
	
	public List<Switch> getSwitches() {
		return this.switches;
	}
	
	public List<Link> getLinks() {
		return this.links;
	}
}
