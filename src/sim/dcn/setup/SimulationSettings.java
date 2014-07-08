package sim.dcn.setup;

import sim.common.ValidationHelper;

public class SimulationSettings {
	
	private String topology;
	
	private String topologyArgs;
	
	private String simulationSetup;
	
	public SimulationSettings(String[] args) {
		ValidationHelper.notNullOrEmpty(args, "args");
		this.parseTopologyArgs(args);
		this.parseSimulationSetup(args);
	}
	
	public String getTopology() {
		return this.topology;
	}
	
	public String[] getTopologyArgs() {
		return this.topologyArgs.split(",");
	}
	
	public String getSimulationSetup() {
		return this.simulationSetup;
	}
	
	private void parseSimulationSetup(String[] args) {
		String simulationSetupPrefix = "--simulation=";
		
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith(simulationSetupPrefix)) {
				this.simulationSetup = args[i].substring(simulationSetupPrefix.length());
			}
		}
		
		if (ValidationHelper.isNullOrEmpty(this.simulationSetup)) {
			this.simulationSetup = "sim.dcn.setup.DefaultSimulationSetup";
		}
	}
	
	private void parseTopologyArgs(String[] args) {
		String topologyPrefix = "--toplogy=";
		String topologyArgsPrefix = "--topologyArgs=";
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith(topologyPrefix)) {
				this.topology = args[i].substring(topologyPrefix.length());
			}
			else if (args[i].startsWith(topologyArgsPrefix)) {
				this.topologyArgs = args[i].substring(topologyArgsPrefix.length());
			}
		}
		
		if (ValidationHelper.isNullOrEmpty(this.topology)) {
			this.topology = "sim.dcn.entity.MultiTreeDataCenter";
			this.topologyArgs = "16,3,1024";	//means 16 leaves, each leaf has 3 foreign links, and default link bandwidth capacity is 1024 Mbps
		}
	}
}
