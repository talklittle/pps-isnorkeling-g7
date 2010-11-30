package isnork.g7;

import java.util.Comparator;

public class Task {
	
	private OurObservation observation;
	private double priorityScore = 0; 
	private OurBoard ourBoard;
	
	public Task(int creatureID, int playerID, OurBoard ourBoard){
		this.observation = new OurObservation(creatureID, new Location(playerID));
		this.ourBoard = ourBoard; 
	}
	
	public Task(int creatureID, Coordinate coordinate, OurBoard ourBoard){
		this.observation = new OurObservation(creatureID, new Location(coordinate));
		this.ourBoard = ourBoard; 
	}
	
	public double getPriorityScore(){
		return priorityScore;
	}
	
	public OurObservation getObservation(){
		return observation;
	}
	
	private void findPriorityScore(Location myCurrentLocation){
		priorityScore = observation.getHappiness() / ourBoard.findDistanceToObservation(observation, myCurrentLocation);
	}
	
	public int compareTo(Task otherTask){
		if (this.priorityScore < otherTask.getPriorityScore())
			return -1;
		else if (this.priorityScore > otherTask.getPriorityScore())
			return 1;
		else
			return 0;
	}
	
}