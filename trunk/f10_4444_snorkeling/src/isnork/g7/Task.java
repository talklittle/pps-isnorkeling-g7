package isnork.g7;

import isnork.sim.Observation;
import isnork.sim.SeaLifePrototype;

import java.awt.geom.Point2D;
import java.util.Set;

public class Task implements Comparable {
	
	private OurObservation observation;
	private double priorityScore = 0; 
	private OurBoard ourBoard;
	private Set<SeaLifePrototype> seaLifePossibilities;
	
	public Task(String creatureName, int playerID, OurBoard ourBoard, Set<SeaLifePrototype> seaLifePossibilities, Set<Observation> playerLocations){
		observation = new OurObservation(creatureName, playerID, seaLifePossibilities, playerLocations);
		this.ourBoard = ourBoard; 
		this.seaLifePossibilities = seaLifePossibilities;
	}
	
	public Task(String creatureName, Point2D coordinate, OurBoard ourBoard, Set<SeaLifePrototype> seaLifePossibilities, Set<Observation> playerLocations){
		observation = new OurObservation(creatureName, coordinate, seaLifePossibilities, playerLocations);
		this.ourBoard = ourBoard; 
		this.seaLifePossibilities = seaLifePossibilities;

	}
	
	public void updatePriorityScore(Point2D myCurrentLocation){
		priorityScore = observation.happiness() / ourBoard.findDistanceToObservation(observation, myCurrentLocation);
	}

	public double getPriorityScore(){
		return priorityScore;
	}
	
	public OurObservation getObservation(){
		return observation;
	}
	
	public int compareTo(Task otherTask){
		if (this.priorityScore < otherTask.getPriorityScore())
			return -1;
		else if (this.priorityScore > otherTask.getPriorityScore())
			return 1;
		else
			return 0;
	}
	
	public void discountPriorityScore(double discount){
		priorityScore *= (1-discount);
	}

	@Override
	public int compareTo(Object otherTask) {
		if (this.priorityScore < ((Task) otherTask).getPriorityScore())
			return -1;
		else if (this.priorityScore > ((Task) otherTask).getPriorityScore())
			return 1;
		else
			return 0;
	}
	
	private SeaLifePrototype getCreature(String creatureName){
		for (SeaLifePrototype s : seaLifePossibilities){
			if (creatureName == s.getName()){
				return s;
			}
		}
		return null;
	}
	
	public void updatePlayerLocations(Set<Observation> playerLocations){
		observation.getTheLocation().setPlayerLocations(playerLocations);
	}
	
}