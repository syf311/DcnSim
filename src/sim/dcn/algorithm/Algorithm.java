package sim.dcn.algorithm;

import sim.dcn.entity.DataCenter;

public interface Algorithm {
	void run(DataCenter dataCenter, int theCycle);
}
