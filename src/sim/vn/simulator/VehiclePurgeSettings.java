package sim.vn.simulator;

import sim.common.ValidationHelper;

public class VehiclePurgeSettings {

	private String rootFullPath;
	
	private VehiclePurgeSettings(String rootFullPath) {
		ValidationHelper.notNullOrEmpty(rootFullPath, "rootFullPath");	
		this.rootFullPath = rootFullPath;
	}

	public String getRootFullPath() {
		return this.rootFullPath;
	}

	public static VehiclePurgeSettings getSettingsFromArgs(String[] args) {
		final String rootFullPathPrefix = "--rootFullPath=";
		
		String rootFullPath = null;
		
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith(rootFullPathPrefix)) {
				rootFullPath = args[i].substring(rootFullPathPrefix.length()).replace("\"", "");
			} 
		}
		
		if (ValidationHelper.isNullOrEmpty(rootFullPath)) {
			rootFullPath = "c:\\lingding\\shanghai_taxi";
		}
		
		return new VehiclePurgeSettings(rootFullPath);
	}
}
