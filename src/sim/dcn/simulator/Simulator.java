package sim.dcn.simulator;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

import sim.common.ExceptionHelper;
import sim.dcn.algorithm.Algorithm;
import sim.dcn.entity.DataCenter;
import sim.dcn.entity.Server;
import sim.dcn.entity.VirtualMachine;
import sim.dcn.metrics.MetricsReporter;
import sim.dcn.setup.SimulationSettings;
import sim.dcn.setup.SimulationSetup;

public class Simulator {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		try {
			SimulationSettings simulationSettings = new SimulationSettings(args); 
			
			Simulator.run(simulationSettings);
		}
		catch (Exception ex) {
			Logger.getLogger(Simulator.class.getName()).log(Level.SEVERE, ExceptionHelper.toString(ex));
		}
	}
	
	private static void run(SimulationSettings settings) throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		Logger.getLogger(Simulator.class.getName()).log(Level.INFO, "Started running simulator");
		DataCenter dataCenter = (DataCenter) Class.forName(settings.getTopology())
				.getConstructor(new Class[]{ String[].class })
				.newInstance(new Object[]{ settings.getTopologyArgs() });
		
		dataCenter = ((SimulationSetup) Class.forName(settings.getSimulationSetup())
				.getConstructor((Class[])null).newInstance((Object[])null)).setupSimulation(dataCenter);
		
		Algorithm algorithm = ((Algorithm) Class.forName(settings.getAlgorithm())
				.getConstructor(settings.getAlgorithmArgs() != null ? new Class[]{ String[].class } : (Class[])null)
				.newInstance(settings.getAlgorithmArgs() != null ? new Object[]{ settings.getAlgorithmArgs() } : (Object[])null) );
		
		Simulator.runCycles(dataCenter, algorithm, settings.getCycles());
	}
	
	private static void runCycles(DataCenter dataCenter, Algorithm algorithm, int cycles) {
		Logger.getLogger(Simulator.class.getName()).log(Level.INFO, String.format("Started running cycles. Algorithm: %s cycles %d", algorithm.getClass().getName(), cycles));
		for (int i = 0; i < cycles; i++) {
			Simulator.runOneCycle(dataCenter, algorithm, i);
		}
	}
	
	private static void runOneCycle(DataCenter dataCenter, Algorithm algorithm, int theCycle) {
		Logger.getLogger(Simulator.class.getName()).log(Level.INFO, String.format("Started running cycle %d", theCycle));
		Simulator.bookRequests(dataCenter);
		algorithm.run(dataCenter, theCycle);
		MetricsReporter.reportMetrics(dataCenter, theCycle);
	}
	
	private static void bookRequests(DataCenter dataCenter) {
		for (Server server : dataCenter.getServers()) {
			for (VirtualMachine virtualMachine : server.getVirtualMachines()) {
				virtualMachine.generateRequests();
			}
		}
	}
}
