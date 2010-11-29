package isnork.g7;

import isnork.sim.Observation;
import isnork.sim.GameObject.Direction;

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
	private Point2D myPosition;
	private Set<Observation> whatYouSee;
	private OurBoard ourBoard;
	private Direction mySafestDirection;
	private Random random;
	
	Logger logger = Logger.getLogger(DangerFinder.class);
	

	public DangerFinder(OurBoard ourBoard, Random random){
		this.ourBoard = ourBoard;
		this.directionDanger = new HashMap<Direction, Double>();
		this.random = random;
		
		/*Initialize dangerList*/
		
//		logger.debug("initializing direction list: " + Direction.values());
//		for (Direction d: Direction.values()){
//			logger.debug("d: " + d);
//			directionDanger.put(d, new Double(0));
//		}
		
	}
	
	
	/*Should only be called once*/
	private void findDanger(){
			
		for(Observation o : whatYouSee){
			logger.trace("What I see: " + o.getName() + "\t\tIs it dangerous? " + o.isDangerous());
			
			Direction directionToCreature;
			if (o.getDirection() == null) {
				directionToCreature = ourBoard.getDirectionTowards(myPosition, o.getLocation());
			} else {
				// predict where it will be
				Point2D predictedLocation = new Point2D.Double(
//						o.getLocation().getX() + (o.getDirection().getDx() * (o.getDirection().isDiag() ? 3 : 2)),
//						o.getLocation().getY() + (o.getDirection().getDy() * (o.getDirection().isDiag() ? 3 : 2))
						o.getLocation().getX() + (o.getDirection().getDx()),
						o.getLocation().getY() + (o.getDirection().getDy())
						);
				directionToCreature = ourBoard.getDirectionTowards(myPosition, predictedLocation);
			}
			
			if (o.isDangerous()) {
				if (directionDanger.containsKey(directionToCreature)) {
					double formerDirectionDanger = directionDanger.get(directionToCreature);
//					logger.debug("formerDirectionDanger" + formerDirectionDanger);
					
					directionDanger.put(directionToCreature, new Double(formerDirectionDanger + Math.abs(o.happiness()*DANGER_MULTIPLIER)));
//					logger.debug("o.happiness" + o.happiness());
					
				} else {
					directionDanger.put(directionToCreature, new Double(Math.abs(o.happiness()*DANGER_MULTIPLIER)));
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
