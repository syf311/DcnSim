package sim.dcn.simulator;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

import sim.common.ExceptionHelper;
import sim.dcn.entity.DataCenter;
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
		
	}
}
