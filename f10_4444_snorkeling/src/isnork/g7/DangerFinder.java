package isnork.g7;

import isnork.sim.Observation;
import isnork.sim.GameObject.Direction;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Set;

import org.apache.log4j.Logger;


public class DangerFinder {
	
	private HashMap<Direction, Double> directionDanger;
	private static final double DANGER_MULTIPLIER = 2;
	private Point2D myPosition;
	private Set<Observation> whatYouSee;
	private OurBoard ourBoard;
	private Direction mySafestDirection;
	
	Logger logger = Logger.getLogger(DangerFinder.class);
	

	public DangerFinder(OurBoard ourBoard){
		this.ourBoard = ourBoard;
		this.directionDanger = new HashMap<Direction, Double>();
		
		/*Initialize dangerList*/
		
		logger.debug("initializing direction list: " + Direction.values());
		for (Direction d: Direction.values()){
			logger.debug("d: " + d);
			directionDanger.put(d, new Double(0));
		}
		
	}
	
	
	/*Should only be called once*/
	private void findDanger(){
			
		for(Observation o : whatYouSee){
			logger.debug("What I see: " +  o.getName());	
			logger.debug("Is it dangerous? " + o.isDangerous());
			
			Direction directionToCreature = ourBoard.getDirectionTowards(myPosition, o.getLocation());
			
			if (directionDanger.get(directionToCreature) != null && o.isDangerous()){
				double formerDirectionDanger = directionDanger.get(directionToCreature);
				logger.debug("formerDirectionDanger" + formerDirectionDanger);
				
				directionDanger.put(directionToCreature, new Double(Math.abs(formerDirectionDanger) + o.happiness()*DANGER_MULTIPLIER));
				logger.debug("o.happiness" + o.happiness());

			}
			else if (o.isDangerous()){
				directionDanger.put(directionToCreature, new Double(Math.abs(o.happiness()*DANGER_MULTIPLIER)));
			}
		}
	}
	
	public void updateCoordinates(Point2D myPosition, Set<Observation> whatYouSee){
		this.myPosition = myPosition;
		this.whatYouSee = whatYouSee;
		
		for (Direction d: Direction.values()){
			logger.debug("d: " + d);
			directionDanger.put(d, new Double(0));
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
	
	public Direction findSafestDirection(Point2D myPosition, Set<Observation> whatYouSee){
		updateCoordinates(myPosition, whatYouSee);
		findDanger();
		
		logger.debug("in find safest direction");
		double maxDanger = 0;
		
		Direction safestDirection = null;
		
		for (Direction d : directionDanger.keySet()){
			double curDanger = Math.abs(directionDanger.get(d));
			
			logger.debug("current danger in direction " + d + ":" + curDanger);
			
			if (curDanger > maxDanger){
				safestDirection = ourBoard.getOppositeDirection(d);
				logger.debug("Max Danger so far in Direction: " + d);

				maxDanger = curDanger;
			}
		} 
		
		mySafestDirection = safestDirection;
		return safestDirection;
	}

}
