package sim.dcn.entity;

import java.util.ArrayList;

import sim.common.ValidationHelper;

public final class MultiTreeDataCenter extends DataCenter {

	public MultiTreeDataCenter(String[] arguments) {
		if (arguments.length != 3) {
			throw new IllegalArgumentException("Wrong number of arguments to build a MultiTree topology data center");
		}
		
		int leafCount = Integer.parseInt(arguments[0]);
		int foreighLinksPerLeaf = Integer.parseInt(arguments[1]);
		double defaultLinkBandWidthCapacity = Double.parseDouble(arguments[2]);
		
		this.Initialize(leafCount, foreighLinksPerLeaf, defaultLinkBandWidthCapacity);
	}
	
	public MultiTreeDataCenter(int leafCount, int foreighLinksPerLeaf, double defaultLinkBandWidthCapacity) {
		this.Initialize(leafCount, foreighLinksPerLeaf, defaultLinkBandWidthCapacity);
	}
	
	private void Initialize(int leafCount, int foreighLinksPerLeaf, double defaultLinkBandWidthCapacity) {
		ValidationHelper.powerOfTwo(leafCount, "leafCount");
		if (foreighLinksPerLeaf > (leafCount/2 - 1)) {
			throw new IllegalArgumentException("foreighLinksPerLeaf");
		}
		
		this.servers = new ArrayList<Server>();
		this.switches = new ArrayList<Switch>();
		this.links = new ArrayList<Link>();

		// create servers, switches, and local links
		this.switches.add(new Switch(1));
		for (int i = 1; i < leafCount; i++) {
			Switch theSwitch = this.getSwitch(i);
			NetworkComponent networkComponentLeft = null;
			NetworkComponent networkComponentRight = null;
			if (i < leafCount/2) {
				networkComponentLeft = new Switch(i * 2);
				networkComponentRight = new Switch(i * 2 + 1);
				this.switches.add((Switch)networkComponentLeft);
				this.switches.add((Switch)networkComponentRight);
			}
			else {
				networkComponentLeft = new Server(i * 2);
				networkComponentRight = new Server(i * 2 + 1);
				this.servers.add((Server)networkComponentLeft);
				this.servers.add((Server)networkComponentRight);
			}
			
			Link linkToLeft = new Link(theSwitch, networkComponentLeft, defaultLinkBandWidthCapacity);
			Link linkToRight = new Link(theSwitch, networkComponentLeft, defaultLinkBandWidthCapacity);
			this.links.add(linkToLeft);
			this.links.add(linkToRight);
			
			theSwitch.AddLink(linkToLeft);
			theSwitch.AddLink(linkToRight);
			networkComponentLeft.AddLink(linkToLeft);
			networkComponentRight.AddLink(linkToRight);
		}
		
		// create foreign links
		for (int i = 0; i < leafCount; i++) {
			Server server = this.servers.get(i);
			for (int j = 1; j <= foreighLinksPerLeaf; j++) {
				int idOfForeignSwitch = ((server.getId() + 2 * j) % leafCount + leafCount) / 2;
				NetworkComponent theSwitch = this.getSwitch(idOfForeignSwitch);
				Link link = new Link(server, theSwitch, defaultLinkBandWidthCapacity);
				server.AddLink(link);
				theSwitch.AddLink(link);
				this.links.add(link);
			}
		}
	}
}
