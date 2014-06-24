package sim.dcn.simulator;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import sim.common.ExceptionHelper;
import sim.dcn.entity.DataCenter;
import sim.dcn.setup.DataCenterSetup;

public class Simulator {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		try {
			if (args.length < 1) {
				String error = "Missing path to Settings file";
				System.out.println(error);
				throw new IllegalArgumentException(error);
			}
			String settingFilePath = args[0];
			Simulator.run(settingFilePath);
		}
		catch (Exception ex) {
			Logger.getLogger(Simulator.class.getName()).log(Level.SEVERE, ExceptionHelper.toString(ex));
		}
	}
	
	private static void run(String settingFilePath) throws IOException {
		Logger.getLogger(Simulator.class.getName()).log(Level.INFO, "Started running simulator");
		DataCenter dataCenter = DataCenterSetup.Setup(settingFilePath);
	}
}
