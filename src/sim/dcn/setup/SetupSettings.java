package sim.dcn.setup;

import sim.common.ValidationHelper;

public class SetupSettings {
	
	private SetupSettings() {
		this.topologyType = ToplogyType.None;
		this.topologyArguments = null;
	}
	
	private ToplogyType topologyType;
	
	private String topologyArguments;
	
	public static SetupSettings ParseSettings(String settingString) {
		SetupSettings setupSettings = new SetupSettings();
		SetupSettings.BuildToplogySetting(settingString, setupSettings);
		
		return setupSettings;
	}
	
	public ToplogyType getToplogyType() {
		return this.topologyType;
	}
	
	public String[] getTopologyArguments() {
		String[] arguments = null;
		if (!ValidationHelper.isNullOrEmpty(topologyArguments)) {
			arguments = topologyArguments.split(",");
		}
		
		return arguments;
	}
	
	public void SetTopologyType(String type) {
		ValidationHelper.notNullOrEmpty(type, "type");
		if (type.compareToIgnoreCase("multitree") == 0) {
			this.topologyType = ToplogyType.MultiTree;
		}
		else {
			throw new UnsupportedOperationException();
		}
	}
	
	public void SetTopologyArguments(String topologyArguments) {
		this.topologyArguments = topologyArguments;
	}
	
	private static void BuildToplogySetting(String settingString, SetupSettings setupSettings) {
		int pos = settingString.indexOf("[topology]");
		if (pos == -1) {
			throw new IllegalStateException("topology not defined - expecting [topology]");
		}
		
		pos = settingString.indexOf("type=", pos + 1);
		if (pos == -1) {
			throw new IllegalStateException("topology type not defined - expecting type=");
		}
		
		pos = settingString.indexOf("(", pos + 1);
		if (pos == -1) {
			throw new IllegalStateException("bad formed topology arguments - expecting (");
		}
		
		int taggedPos = pos; 
		pos = settingString.indexOf(")", pos + 1);
		if (pos == -1) {
			throw new IllegalStateException("bad formed topology arguments - expecting )");
		}
		
		String topologyArguments = (taggedPos + 1 == pos) ? null : settingString.substring(taggedPos + 1, pos);
		
		pos = settingString.indexOf("@", pos + 1);
		if (pos == -1) {
			throw new IllegalStateException("bad formed topology type - expecting @");
		}
		
		String topologyType = SetupSettings.readSetting(settingString, pos+1);
		
		setupSettings.SetTopologyType(topologyType);
		setupSettings.SetTopologyArguments(topologyArguments);
	}
	
	private static String readSetting(String settingString, int posStart) {
		int pos = posStart;
		StringBuffer settingValue = new StringBuffer();
		while (pos < settingString.length()) {
			char ch = settingString.charAt(pos);
			pos ++;
			if (SetupSettings.stopReadingSetting(ch)) {
				break;
			}
			
			settingValue.append(ch);
		}
		
		return settingValue.toString();
	}
	
	private static boolean stopReadingSetting(char ch) {
		return ch == ';' || ch == '\r' || ch == '\n' || ch == ' ' || ch == '\t';
	}
}
