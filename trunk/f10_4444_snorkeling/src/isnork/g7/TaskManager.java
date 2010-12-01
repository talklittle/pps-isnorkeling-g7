package isnork.g7;

import isnork.sim.Observation;
import isnork.sim.SeaLifePrototype;

import java.awt.geom.Point2D;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Set;

/*TODO: STILL NEED TO MARK TASKS INVALID AFTER GETTING MESSAGE FROM FOLLOWING PLAYER THAT STOPS FOLLOWING THE CREATURE*/

public class TaskManager {
	private PriorityQueue<Task> taskList; 
	private HashMap<Task, Boolean> seenObjects; 
	//Hash of the SeaLife creature name Strings mapping to the number of times it was seen
	//by the diver using this class
	private HashMap<String, Integer> seenCreatures;
	private OurBoard ourBoard;
	private Set<SeaLifePrototype> seaLifePossibilities;
	private Set<Observation> playerLocations;
	
	public TaskManager(Set<SeaLifePrototype> seaLifePossibilities, OurBoard ourBoard){
		taskList = new PriorityQueue<Task>(10, new TaskComparator());
		seenObjects = new HashMap<Task, Boolean>();
		this.seaLifePossibilities = seaLifePossibilities;
		this.ourBoard = ourBoard;
		
		seenCreatures = new HashMap<String, Integer>();
		
		for(SeaLifePrototype s: seaLifePossibilities){
			seenCreatures.put(s.getName(), 0);
		}
	}
	
	public void setPlayerLocations(Set<Observation> playerLocations){
		this.playerLocations = playerLocations;
	}

	
	public static class TaskComparator implements Comparator<Task>
    {
        @SuppressWarnings("unchecked")
		public int compare (Task o1, Task o2)
        {
            return ((Comparable<Task>) o1).compareTo(o2);
        }
    }
	
	/*To add tasks with a player associated (a player traveling a moving creature)*/
	public void addTask(String creatureName, int playerID){
		Task task = new Task(creatureName, playerID, ourBoard, seaLifePossibilities, playerLocations);
		taskList.add(task);
		seenObjects.put(task, false);
		
		int numPreviousSightings = seenCreatures.get(creatureName).intValue();
	
		task.discountPriorityScore((1.0/(1+numPreviousSightings)));
		seenCreatures.put(creatureName, new Integer(numPreviousSightings++));
	}
	
	/*To add tasks with static creature and fixed position associated*/
	public void addTask(String creatureName, Point2D coordinate){
		Task task = new Task(creatureName, coordinate, ourBoard, seaLifePossibilities, playerLocations);
		//task.getObservation().setLocation(coordinate);
		taskList.add(task);
		seenObjects.put(task, false);
		
		int numPreviousSightings = seenCreatures.get(creatureName).intValue();
		task.discountPriorityScore((1.0/(1+numPreviousSightings)));
		seenCreatures.put(creatureName, new Integer(numPreviousSightings++));
	}
	
	
	/*Will return null if there are no valid remaining tasks in the queue*/
	public Task getNextTask(Point2D myCurrentLocation){
		updatePriorityScores(myCurrentLocation);
		
		Iterator<Task> taskIterator = taskList.iterator();
		
		Task nextTask;

		if(taskIterator.hasNext()){
			nextTask = taskIterator.next();
		
			while(!nextTask.getObservation().isValid() || !taskUnseen(nextTask)){
				
				if(taskIterator.hasNext()){
					nextTask = taskIterator.next();
				}
				else 
					break;
			}
			
			if(nextTask != null){
				markTaskComplete(nextTask);
				return nextTask;
			}
		}
		
		return null;
	}
	
	private boolean taskUnseen(Task task){
		return seenObjects.get(task);
	}
	
	
	/*This isn't getting used yet*/
	public void markTaskInvalid(Task task){
		task.getObservation().setInvalid();
	}
	
	public void markTaskComplete(Task task){
		task.getObservation().setInvalid();
		markCreatureSeen(task.getObservation().getCreatureName());
	}
	
	private void markCreatureSeen(String creatureName){
		int numCreatureSightings = seenCreatures.get(creatureName).intValue();
		seenCreatures.put(creatureName, new Integer(numCreatureSightings++));
	}
	
	private void updatePriorityScores(Point2D myCurrentLocation){
		Iterator<Task> taskIterator = taskList.iterator();
		
		Task nextTask;
		boolean hasNextTask = taskIterator.hasNext();
		
		while (hasNextTask){
			nextTask = taskIterator.next();
			nextTask.updatePriorityScore(myCurrentLocation);
			hasNextTask = taskIterator.hasNext();
		}
	}
	
	public void updatePlayerLocations(Set<Observation> playerLocations){
		for(Task task : taskList){
			task.updatePlayerLocations(playerLocations);
		}
	}
}
