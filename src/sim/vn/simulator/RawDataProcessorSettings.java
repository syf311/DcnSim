package sim.vn.simulator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import sim.common.ValidationHelper;

public class RawDataProcessorSettings {
	private String rootFullPath;
	
	private double cellSize;
	
	private Calendar from;
	
	private Calendar to;
	
	private int windowSizeUnitInSeconds;
	
	private int windowSizeInUnits;
	
	private String outputRootFullPath;
	
	public RawDataProcessorSettings(
			String rootFullPath, 
			String cellSize,
			String fromString,
			String toString,
			String windowSizeUnitInSeconds,
			String windowSizeInUnits,
			String outputRootFullPath) throws ParseException {
		ValidationHelper.notNullOrEmpty(rootFullPath, "rootFullPath");
		ValidationHelper.notNullOrEmpty(outputRootFullPath, "outputRootFullPath");
		
		this.rootFullPath = rootFullPath;
		this.cellSize = Double.parseDouble(cellSize);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.from = Calendar.getInstance();
		this.from.setTime(formatter.parse(fromString));
		this.to = Calendar.getInstance();
		this.to.setTime(formatter.parse(toString));
		
		this.windowSizeUnitInSeconds = Integer.parseInt(windowSizeUnitInSeconds);
		this.windowSizeInUnits = Integer.parseInt(windowSizeInUnits);
		this.outputRootFullPath = outputRootFullPath;
	}

	public String getRootFullPath() {
		return this.rootFullPath;
	}
	
	public double getCellSize() {
		return this.cellSize;
	}
	
	public Calendar getFrom() {
		return this.from;
	}
	
	public Calendar getTo() {
		return this.to;
	}
	
	public int getwindowSizeInUnits() {
		return this.windowSizeInUnits;
	}
	
	public int getWindowSizeUnitInSeconds() {
		return this.windowSizeUnitInSeconds;
	}
	
	public String getOutputRootFullPath() {
		return this.outputRootFullPath;
	}
	
	public static RawDataProcessorSettings getSettingsFromArgs(String[] args) throws ParseException {
		final String rootFullPathPrefix = "--rootFullPath=";
		final String cellSizePrefix = "--cellSize=";
		final String fromPrefix = "--from=";
		final String toPrefix = "--to=";
		final String windowSizeUnitInSecondsPrefix = "--windowSizeUnitInSeconds=";
		final String windowSizeInUnitsPrefix = "--windowSizeInUnits=";
		final String outputRootFullPathPrefix = "--outputRootFullPath=";
		
		String rootFullPath = null;
		String cellSize = null;
		String from = null;
		String to = null;
		String windowSizeUnitInSeconds = null;
		String windowSizeInUnits = null;
		String outputRootFullPath = null;
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith(rootFullPathPrefix)) {
				rootFullPath = args[i].substring(rootFullPathPrefix.length()).replace("\"", "");
			} else if (args[i].startsWith(cellSizePrefix)) {
				cellSize = args[i].substring(cellSizePrefix.length()).replace("\"", "");
			} else if (args[i].startsWith(fromPrefix)) {
				from = args[i].substring(fromPrefix.length()).replace("\"", "");
			} else if (args[i].startsWith(toPrefix)) {
				to = args[i].substring(toPrefix.length()).replace("\"", "");
			} else if (args[i].startsWith(windowSizeUnitInSecondsPrefix)) {
				windowSizeUnitInSeconds = args[i].substring(windowSizeUnitInSecondsPrefix.length()).replace("\"", "");
			} else if (args[i].startsWith(windowSizeInUnitsPrefix)) {
				windowSizeInUnits = args[i].substring(windowSizeInUnitsPrefix.length()).replace("\"", "");
			} 
			else if (args[i].startsWith(outputRootFullPathPrefix)) {
				outputRootFullPath = args[i].substring(outputRootFullPathPrefix.length()).replace("\"", "");
			} 
		}
		
		if (ValidationHelper.isNullOrEmpty(rootFullPath)) {
			rootFullPath = "c:\\lingding\\shanghai_taxi";
		}
		
		if (ValidationHelper.isNullOrEmpty(cellSize)) {
			cellSize = "0.001";
		}
		
		if (ValidationHelper.isNullOrEmpty(from)) {
			from = "2007-02-01 00:00:00";
		}
		
		if (ValidationHelper.isNullOrEmpty(to)) {
			to = "2007-02-28 23:59:59";
		}
		
		if (ValidationHelper.isNullOrEmpty(windowSizeUnitInSeconds)) {
			windowSizeUnitInSeconds = "10";
		}
		
		if (ValidationHelper.isNullOrEmpty(windowSizeInUnits)) {
			windowSizeInUnits = "12";
		}
		
		if (ValidationHelper.isNullOrEmpty(outputRootFullPath)) {
			outputRootFullPath = "c:\\lingding\\probability";
		}
		
		return new RawDataProcessorSettings(
				rootFullPath, 
				cellSize,
				from,
				to,
				windowSizeUnitInSeconds,
				windowSizeInUnits,
				outputRootFullPath);
	}
}
