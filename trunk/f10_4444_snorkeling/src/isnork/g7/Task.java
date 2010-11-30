package isnork.g7;

import java.util.Comparator;

public class Task {
	
	private OurObservation observation;
	private double priorityScore = 0; 
	private OurBoard ourBoard;
	
	public Task(String creatureName, int playerID, OurBoard ourBoard){
		this.observation = new OurObservation(creatureName, new Location(playerID));
		this.ourBoard = ourBoard; 
	}
	
	public Task(String creatureName, Coordinate coordinate, OurBoard ourBoard){
		this.observation = new OurObservation(creatureName, new Location(coordinate));
		this.ourBoard = ourBoard; 
	}
	
	public void updatePriorityScore(Location myCurrentLocation){
		priorityScore = observation.getHappiness() / ourBoard.findDistanceToObservation(observation, myCurrentLocation);
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