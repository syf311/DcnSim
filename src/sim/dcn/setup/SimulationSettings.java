package sim.dcn.setup;

import sim.common.ValidationHelper;

public class SimulationSettings {
	
	private String topology;
	
	private String topologyArgs;
	
	private String simulationSetup;
	
	private String algorithm;
	
	private String algorithmArgs;
	
	private int cycles;
	
	public SimulationSettings(String[] args) {
		ValidationHelper.notNullOrEmpty(args, "args");
		this.parseTopologyArgs(args);
		this.parseSimulationSetup(args);
		this.parseAlgorithmArgs(args);
		this.parseCycles(args);
	}
	
	public String getTopology() {
		return this.topology;
	}
	
	public String[] getTopologyArgs() {
		return this.topologyArgs.split(",");
	}
	
	public String getAlgorithm() {
		return this.algorithm;
	}
	
	public String[] getAlgorithmArgs() {
		return this.algorithmArgs.split(",");
	}
	
	public String getSimulationSetup() {
		return this.simulationSetup;
	}
	
	public int getCycles() {
		return this.cycles;
	}
	
	private void parseSimulationSetup(String[] args) {
		String simulationSetupPrefix = "--simulationSetup=";
		
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
	
	private void parseAlgorithmArgs(String[] args) {
		String algorithmPrefix = "--algorithm=";
		String algorithmArgsPrefix = "--algorithmArgs=";
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith(algorithmPrefix)) {
				this.algorithm = args[i].substring(algorithmPrefix.length());
			}
			else if (args[i].startsWith(algorithmArgsPrefix)) {
				this.algorithmArgs = args[i].substring(algorithmArgsPrefix.length());
			}
		}
		
		if (ValidationHelper.isNullOrEmpty(this.algorithm)) {
			this.algorithm = "sim.dcn.algorithm.MinGuaranteeOnly";
			this.algorithmArgs = null;
		}
	}
	
	private void parseCycles(String[] args) {
		String cyclesPrefix = "--cycles=";
		String cyclesArg = null;
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith(cyclesPrefix)) {
				cyclesArg = args[i].substring(cyclesPrefix.length());
			}
		}
		
		this.cycles = cyclesArg == null ? 1000 : Integer.parseInt(cyclesArg);
	}
}
