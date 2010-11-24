package isnork.g7;

import isnork.sim.Observation;
import isnork.sim.GameObject.Direction;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Set;


public class DangerFinder {
	
	private HashMap<Direction, Double> directionDanger;
	private static final double DANGER_MULTIPLIER = 2;
	private Point2D myPosition;
	private Set<Observation> whatYouSee;
	private OurBoard ourBoard;
	private Direction mySafestDirection;
	

	public DangerFinder(OurBoard ourBoard){
		this.ourBoard = ourBoard;
		this.directionDanger = new HashMap<Direction, Double>();
		
		/*Initialize dangerList*/
		
//		System.out.println("initializing direction list: " + Direction.values());
		for (Direction d: Direction.values()){
//			System.out.println("d: " + d);
			directionDanger.put(d, new Double(0));
		}
		
	}
	
	
	/*Should only be called once*/
	private void findDanger(){
			
		for(Observation o : whatYouSee){
//			System.out.println("What I see: " +  o.getName());	
//			System.out.println("Is it dangerous? " + o.isDangerous());
			
			Direction directionToCreature = ourBoard.getDirectionTowards(myPosition, o.getLocation());
			
			if (directionDanger.get(directionToCreature) != null && o.isDangerous()){
//				double formerDirectionDanger = directionDanger.get(directionToCreature);
//				System.out.println("formerDirectionDanger" + formerDirectionDanger);
				
//				directionDanger.put(directionToCreature, new Double(Math.abs(formerDirectionDanger) + o.happiness()*DANGER_MULTIPLIER));
//				System.out.println("o.happiness" + o.happiness());

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
//			System.out.println("d: " + d);
			directionDanger.put(d, new Double(0));
		}
	}
	
	public void printSurroundingDanger(){
		System.out.println("Here's the danger surrounding the diver at " + myPosition + ":\n");
		
		for (Direction d : directionDanger.keySet()){
			if (d!= null)
				System.out.println(d.toString() + ": " + directionDanger.get(d) + "\n");
		}
		
		System.out.println("I want to head in direction " + mySafestDirection + ":\n");

	}
	
	public Direction findSafestDirection(Point2D myPosition, Set<Observation> whatYouSee){
		updateCoordinates(myPosition, whatYouSee);
		findDanger();
		
//		System.out.println("in find safest direction");
		double maxDanger = 0;
		
		Direction safestDirection = null;
		
		for (Direction d : directionDanger.keySet()){
			double curDanger = Math.abs(directionDanger.get(d));
			
//			System.out.println("current danger in direction " + d + ":" + curDanger);
			
			if (curDanger > maxDanger){
				safestDirection = ourBoard.getOppositeDirection(d);
//				System.out.println("Max Danger so far in Direction: " + d);

				maxDanger = curDanger;
			}
		} 
		
		mySafestDirection = safestDirection;
		return safestDirection;
	}

}
