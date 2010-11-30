package isnork.g7;

import isnork.sim.Observation;
import isnork.sim.SeaLifePrototype;

import java.awt.geom.Point2D;
import java.util.Comparator;
import java.util.Set;

public class Task {
	
	private OurObservation observation;
	private double priorityScore = 0; 
	private OurBoard ourBoard;
	
	public Task(String creatureName, int playerID, OurBoard ourBoard, Set<SeaLifePrototype> seaLifePossibilities, Set<Observation> playerLocations){
		observation = new OurObservation(creatureName, seaLifePossibilities, playerLocations);
		this.ourBoard = ourBoard; 
	}
	
	public Task(String creatureName, OurBoard ourBoard, Set<SeaLifePrototype> seaLifePossibilities, Set<Observation> playerLocations){
		this.observation = new OurObservation(creatureName, seaLifePossibilities, playerLocations);
		this.ourBoard = ourBoard; 
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
	
}