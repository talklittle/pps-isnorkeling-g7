package isnork.g7;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class TaskManager {
	private PriorityQueue<Task> taskList; 
	private HashMap<Task, Boolean> seenObjects; 
	//Hash of the SeaLife creature name Strings mapping to the number of times it was seen
	//by the diver using this class
	private HashMap<String, Integer> seenCreatures;
	
	public TaskManager(){
		taskList = new PriorityQueue<Task>(10, new TaskComparator());
		seenObjects = new HashMap<Task, Boolean>();
	}

	
	public static class TaskComparator implements Comparator<Task>
    {
        public int compare (Task o1, Task o2)
        {
            return ((Comparable) o1).compareTo(o2);
        }
    }
	
	//TO-DO: Finish this implementation
	public void addTask(){
		//Task task = new Task()
		//taskList.add(task);
		//seenObjects.put(task, false);
		//seenCreatures.put(creatureName, new Integer(numCreatureSightings++));

	}
	
	
	/*Will return null if there are no valid remaining tasks in the queue*/
	public Task getNextTask(){
		Task nextTask = taskList.remove();
		
		
		while (!nextTask.getObservation().isValid() || !taskUnseen(nextTask)){
			nextTask = taskList.remove();
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
	}
	
	public void markCreatureSeen(String creatureName){
		int numCreatureSightings = seenCreatures.get(creatureName).intValue();
		seenCreatures.put(creatureName, new Integer(numCreatureSightings++));
	}
}
