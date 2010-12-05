package isnork.g7;

import java.awt.geom.Point2D;

import org.apache.log4j.Logger;

import isnork.sim.SeaLifePrototype;
import isnork.sim.GameObject.Direction;

public class Tracker {
	private static SeaLifePrototype seaLifePrototype; 
	private static final Logger logger = Logger.getLogger(Tracker.class);
	
	public static Direction track(SeaLifePrototype s, Point2D me, Point2D beast)
	{
		seaLifePrototype = s;
		return tightTrack(me, beast);
	}

	private static Direction safeTrack(Point2D me, Point2D beast) {
		return null;
	}

	private static Direction tightTrack(Point2D me, Point2D beast) 
	{
		if(beast == null)
			return null;
		return OurBoard.getDirectionTowards(me, beast);
	}
	
	public SeaLifePrototype getSeaLifePrototype(){
		return seaLifePrototype;
	}
	
}
