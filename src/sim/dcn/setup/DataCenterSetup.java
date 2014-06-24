package sim.dcn.setup;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import sim.common.ValidationHelper;
import sim.dcn.entity.DataCenter;
import sim.dcn.entity.MultiTreeDataCenter;

public class DataCenterSetup {
	private DataCenterSetup() {
		
	}
	
	public static DataCenter Setup(String setupFilePath) throws IOException {
		Logger.getLogger(DataCenterSetup.class.getName()).log(Level.INFO, String.format("Setting up data center from %s", setupFilePath));
		ValidationHelper.notNullOrEmpty(setupFilePath, "setupFilePath");
		String settingString = DataCenterSetup.getSetupFromFile(setupFilePath);
		
		SetupSettings setupSettings = SetupSettings.ParseSettings(settingString);
		return DataCenterSetup.BuildDataCenter(setupSettings);
	}
	
	private static String getSetupFromFile(String setupFilePath) throws IOException {
		byte[] bytes = Files.readAllBytes(Paths.get(setupFilePath));
		return new String(bytes);
	}
	
	private static DataCenter BuildDataCenter(SetupSettings setupSettings) {
		DataCenter dataCenter = null;
		if (setupSettings.getToplogyType() == ToplogyType.MultiTree) {
			dataCenter = new MultiTreeDataCenter(setupSettings.getTopologyArguments());
		}
		
		return dataCenter;
	}
}
