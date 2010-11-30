package isnork.g7;

import isnork.sim.SeaLifePrototype;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Set;

public class TaskManager {
	private PriorityQueue<Task> taskList; 
	private HashMap<Task, Boolean> seenObjects; 
	//Hash of the SeaLife creature name Strings mapping to the number of times it was seen
	//by the diver using this class
	private HashMap<String, Integer> seenCreatures;
	private OurBoard ourBoard;
	private Set<SeaLifePrototype> seaLifePossibilities;
	
	public TaskManager(Set<SeaLifePrototype> seaLifePossibilities, OurBoard ourBoard){
		taskList = new PriorityQueue<Task>(10, new TaskComparator());
		seenObjects = new HashMap<Task, Boolean>();
		this.seaLifePossibilities = seaLifePossibilities;
		this.ourBoard = ourBoard;
	}

	
	public static class TaskComparator implements Comparator<Task>
    {
        @SuppressWarnings("unchecked")
		public int compare (Task o1, Task o2)
        {
            return ((Comparable<Task>) o1).compareTo(o2);
        }
    }
	
	//TO-DO: Finish this implementation
	public void addTask(String creatureName, int playerID){
		Task task = new Task(creatureName, playerID, ourBoard);
		taskList.add(task);
		seenObjects.put(task, false);
		
		int numPreviousSightings = seenCreatures.get(creatureName).intValue();
		seenCreatures.put(creatureName, new Integer(numPreviousSightings++));

	}
	
	
	/*Will return null if there are no valid remaining tasks in the queue*/
	public Task getNextTask(){
		Task nextTask = taskList.remove();
		
		while (!nextTask.getObservation().isValid() || !taskUnseen(nextTask)){
			nextTask = taskList.remove();
		}
		
		if(nextTask != null){
			markTaskComplete(nextTask);
		}
		
		return nextTask;
	}
	
	private boolean taskUnseen(Task task){
		return seenObjects.get(task);
	}
	
	private boolean creatureUnseen(String seaLifeName){
		if (seenCreatures.get(seaLifeName).intValue()==0){
			return true;
		}
		return false;	
	}
	
	public void markTaskComplete(Task task){
		seenObjects.put(task, true);
		task.getObservation().setInvalid();
		markCreatureSeen(task.getObservation().getCreatureName());
	}
	
	private void markCreatureSeen(String creatureName){
		int numCreatureSightings = seenCreatures.get(creatureName).intValue();
		seenCreatures.put(creatureName, new Integer(numCreatureSightings++));
	}
	
	private SeaLifePrototype getCreature(String creatureName){
		Iterator<SeaLifePrototype> seaLifeIt = seaLifePossibilities.iterator();
		
		SeaLifePrototype nextCreature = seaLifeIt.next();
		
		while(nextCreature!=null){
			if (nextCreature.getName() == creatureName) {
				return nextCreature;
			}
		}
		
		return nextCreature;
	}
}
