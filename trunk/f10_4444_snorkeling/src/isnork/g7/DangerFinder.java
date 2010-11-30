package isnork.g7;

import isnork.sim.GameObject.Direction;
import isnork.sim.Observation;
import isnork.sim.SeaLifePrototype;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;


public class DangerFinder {
	
	private HashMap<Direction, Double> directionDanger;
	private static final double DANGER_MULTIPLIER = 2;
	private static final double DANGER_MAX_DISTANCE = 5.0;
	private static final double STATIONARY_DANGER_DISTANCE = 1.5;
	private Point2D myPosition;
	private Set<Observation> whatYouSee;
	private OurBoard ourBoard;
	private Set<SeaLifePrototype> seaLifePossibilities;
	private Direction mySafestDirection;
	private Random random;
	
	// TODO change time-to-get-home based on stats (number of dangerous creatures)
	
	Logger logger = Logger.getLogger(DangerFinder.class);
	

	public DangerFinder(OurBoard ourBoard, Set<SeaLifePrototype> seaLifePossibilities, Random random){
		this.ourBoard = ourBoard;
		this.seaLifePossibilities = seaLifePossibilities;
		this.directionDanger = new HashMap<Direction, Double>();
		this.random = random;
	}
	
	
	/*Should only be called once*/
	private void findDanger(){
			
		for(Observation o : whatYouSee){
//			logger.trace("What I see: " + o.getName() + "\t\tIs it dangerous? " + o.isDangerous());
			if (!o.isDangerous())
				continue;
			
			// 9 locations (predicted actual location + surrounding cells)
			ArrayList<Point2D> predictedLocations = new ArrayList<Point2D>();
			
			int speed = 0;
			int happy = 0;  // can't use o.happiness() because that is 0 when you are on boat!
			for (SeaLifePrototype life : seaLifePossibilities) {
				if (life.getName().equals(o.getName())) {
					speed = life.getSpeed();
					happy = life.getHappiness();
					break;
				}
			}
			
			// Get the location and distance based on predicted location
			// (taking into account whether creature is stationary or moving).
			if (speed == 0 || o.getDirection() == null) {
				predictedLocations.add(o.getLocation());
			} else {
				// predict where it will be
				Point2D loc = new Point2D.Double(
//						o.getLocation().getX() + (o.getDirection().getDx() * (o.getDirection().isDiag() ? 3 : 2)),
//						o.getLocation().getY() + (o.getDirection().getDy() * (o.getDirection().isDiag() ? 3 : 2))
						o.getLocation().getX() + (o.getDirection().getDx()),
						o.getLocation().getY() + (o.getDirection().getDy())
						);
				if (ourBoard.inBounds((int)loc.getX(), (int)loc.getY())) {
					predictedLocations.add(loc);
				} else {
					predictedLocations.add(o.getLocation());
				}
			}
			
			// Consider the Directions surrounding the creature
			for (Direction d : Direction.values()) {
				Point2D loc = new Point2D.Double(
						predictedLocations.get(0).getX() + d.getDx(),
						predictedLocations.get(0).getY() + d.getDy());
				logger.trace("predictedLocations.add("+loc+")");
				predictedLocations.add(loc);
			}
			
			// Calculate the danger for each direction
			for (int i = 0; i < predictedLocations.size(); i++) {
				
				if (!ourBoard.inBounds((int)predictedLocations.get(i).getX(), (int)predictedLocations.get(i).getY()))
					continue;
				
				Direction directionToCreature = ourBoard.getDirectionTowards(myPosition, predictedLocations.get(i));
				double distanceToCreature = myPosition.distance(predictedLocations.get(i));
				
				logger.debug("Direction:"+directionToCreature+" distanceToCreature = " + distanceToCreature);

				// Only consider dangerous creatures if:
				// 1. They are stationary and affect cells next to the diver, OR
				// 2. They are moving and affect cells within DANGER_MAX_DISTANCE of the diver.
				if (o.isDangerous() && ((speed == 0 && distanceToCreature <= STATIONARY_DANGER_DISTANCE)
						|| (speed > 0 && distanceToCreature <= DANGER_MAX_DISTANCE))) {
					
					double formerDirectionDanger = 0;
					
					if (directionDanger.containsKey(directionToCreature)) {
						formerDirectionDanger = directionDanger.get(directionToCreature);
						logger.debug("formerDirectionDanger = " + formerDirectionDanger);
					}
						
					directionDanger.put(directionToCreature, new Double(formerDirectionDanger + Math.abs(happy*DANGER_MULTIPLIER)));
						
				}
			}
		}
	}
	
	public void updateCoordinates(Point2D myPosition, Set<Observation> whatYouSee){
		this.myPosition = myPosition;
		this.whatYouSee = whatYouSee;
		
		for (Direction d: Direction.values()){
			logger.debug("d: " + d);
			directionDanger.clear();
		}
	}
	
	public void printSurroundingDanger(){
		logger.debug("Here's the danger surrounding the diver at " + myPosition + ":");
		
		for (Direction d : directionDanger.keySet()){
			if (d!= null)
				logger.debug(d.toString() + ": " + directionDanger.get(d));
		}
		
		logger.debug("I want to head in direction " + mySafestDirection + ":");

	}
	
	public Direction findSafestDirection(Point2D myPosition, Set<Observation> whatYouSee, Direction preferredDirection){
		updateCoordinates(myPosition, whatYouSee);
		findDanger();
		
		logger.debug("in find safest direction");
		double minDanger = Integer.MAX_VALUE;
		double maxDanger = 0;
		
		ArrayList<Direction> safestDirections = new ArrayList<Direction>();
		
		for (Direction d : Direction.values()){
			double curDanger;
			if (directionDanger.containsKey(d)) {
				curDanger = Math.abs(directionDanger.get(d));
				logger.trace("directionDanger.get("+d+") = "+curDanger);
			} else {
				curDanger = 0;
			}
			
//			logger.debug("current danger in direction " + d + ":" + curDanger);
			
			if (curDanger > maxDanger){
//				safestDirection = ourBoard.getOppositeDirection(d);
				logger.debug("Max Danger so far in Direction: " + d);

				maxDanger = curDanger;
			}
			
			if (curDanger < minDanger) {
				safestDirections.clear();
				safestDirections.add(d);
				logger.debug("Min Danger so far in Direction: " + d);
				
				minDanger = curDanger;
			}
			else if (curDanger == minDanger) {
				safestDirections.add(d);
			}
		} 
		
		// 80% of the time, continue in preferredDirection if it is among the safest
		if (preferredDirection != null && safestDirections.contains(preferredDirection) && random.nextDouble() >= 0.80) {
			mySafestDirection = preferredDirection;
		} else {
			Collections.shuffle(safestDirections);
			mySafestDirection = safestDirections.get(0);
		}
		logger.debug("Safest direction: " + mySafestDirection + " (from among " + safestDirections.toString() + ")");
		return mySafestDirection;
	}

}
