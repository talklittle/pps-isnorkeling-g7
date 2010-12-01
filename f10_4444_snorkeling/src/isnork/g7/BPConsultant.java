package isnork.g7;


import isnork.sim.GameConfig;
import isnork.sim.Player;
import isnork.sim.SeaLifePrototype;
import isnork.sim.iSnorkMessage;
import isnork.sim.GameObject.Direction;
import isnork.sim.Observation;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


import org.apache.log4j.Logger;



public class BPConsultant extends Player {

	// A buffer to give you some extra time to go back to boat.
	// Why 6: allows you to make 1 useless diagonal move away from boat. 3 minutes each way.
	private static final int BOAT_TIME_BUFFER = 6;
	
	private static final Logger logger = Logger.getLogger(BPConsultant.class);
	
	private Direction direction;
	Point2D whereIAm = null;
	// having this flag prevents him from wavering between going and not going to boat
	private boolean shouldReturnToBoat = false;
	int boatTimeBufferAdjusted;
	
	Set<SeaLifePrototype> seaLifePossibilities;
	int d = -1;  // parameter "d" == half the board length
	int r = -1;  // parameter "r" == number of cells away you can see
	int n = -1;  // parameter "n" == number of snorkelers
	int penalty;
	int round = 0;
	
	private OurBoard ourBoard;
	private DangerFinder dangerFinder;
	private Point2D myPosition = null;
	private Point2D myPreviousPosition = null;
	private Set<Observation> whatYouSee = null;
	private Point2D beast = null;
	private TaskManager taskManager;
	private boolean isTracker = false;
	private boolean isSeeker = false;
	private boolean curTracking = false;
	private SeaLifePrototype toTrack = null;
	private Point2D trackLocal = null;
	private Task task = null;
	
	@Override
	public String getName() {
		return "BP Consultant";
	}
	
	@Override
	public String tick(Point2D myPosition, Set<Observation> whatYouSee,
			Set<iSnorkMessage> incomingMessages,Set<Observation> playerLocations) {
		this.myPreviousPosition = this.myPosition;
		this.myPosition = myPosition;
		this.whatYouSee = whatYouSee;
		taskManager.updatePlayerLocations(playerLocations);
		
		Observation[] a = new Observation[whatYouSee.size()];
		ArrayList<OurObservation> o = new ArrayList<OurObservation>();
		whatYouSee.toArray(a);
		for(int i=0; i<a.length;i++)
		{
			if(a[i].getId()>1)
			{
				o.add(new OurObservation(a[i]));
			}
		}
		Collections.sort(o);
		
		String snorkMessage = null;
		whereIAm = myPosition;
		round++;
		
		if(isTracker)
		{
			//look at all creatures
			//if tracking, return highest static creature
			if(curTracking)
			{
				//update trackLocal
				boolean change = false;
				for(OurObservation ob : o)
				{
					if(ob.getName() == toTrack.getName())
					{
						change = true;
						trackLocal = ob.getLocation();
					}
				}
				if(!change)
					curTracking=false;
				
				for(OurObservation ob: o)
				{
					snorkMessage = MessageTranslator.getMessage(ob.o.getName());
					if(MessageTranslator.hm.get(snorkMessage).getSpeed() == 0)
					{
						logger.debug("tracker sees: " +snorkMessage);
						return snorkMessage;
					}
				}
				return null;
			}
			//else
			//if highest scoring is moving, track
			for(OurObservation ob: o)
			{
				snorkMessage = MessageTranslator.getMessage(ob.o.getName());
				if(MessageTranslator.hm.get(snorkMessage).getSpeed() > 0)
				{
				//	logger.debug("tracking: " +snorkMessage);
					curTracking = true;
					toTrack = MessageTranslator.hm.get(snorkMessage);
					trackLocal = ob.o.getLocation();
					return snorkMessage;
				}
			}
			//else return highest static creature
		}
		else
		{
			//read messages
			iSnorkMessage temp = null;
			String mess = null;
			SeaLifePrototype obs = null;
			Iterator<iSnorkMessage> it = incomingMessages.iterator();
			while(it.hasNext())
			{
				temp = it.next();
				mess= temp.getMsg();
				obs = MessageTranslator.hm.get(mess);
				if(obs.getSpeed() == 0)
				{
					taskManager.addTask(obs.getName(), temp.getLocation());
				}
				else
				{
					taskManager.addTask(obs.getName(), temp.getSender());
				}
			}
			//add stuff to queue
		}
		
		System.out.println("snorkMessage: " + snorkMessage);
		
		return snorkMessage;
	}
	

		
	@Override
	public Direction getMove() {	
		// Head back to boat if we are running low on time (shortest time back, plus a buffer)
//		if (getRemainingTime() <= NavigateToBoat.getTimeToBoat(whereIAm) + boatTimeBufferAdjusted || shouldReturnToBoat) {
		if (getRemainingTime() <= 180) {
			shouldReturnToBoat = true;
			System.out.println("Returning to the boat.");
			// If not enough time, ignore all dangerous creatures and return to boat.
			if (getRemainingTime() < NavigateToBoat.getTimeToBoat(whereIAm) + 6) {
				direction = NavigateToBoat.getShortestDirectionToBoat(whereIAm);
				System.out.println("No time to avoid danger");
			}
			// If there are at least 6 spare minutes, that lets you maneuver around dangerous creatures,
			// even going diagonally away from boat once.
			else {
				System.out.println("Returning to boat but trying to avoid danger still")
;				Direction preferredDirectionToBoat = NavigateToBoat.getShortestDirectionToBoat(whereIAm);
				direction = dangerFinder.findSafestDirection(myPosition, myPreviousPosition,
						whatYouSee, preferredDirectionToBoat, true);
			}
			//logger.debug("(boat) remaining: " + getRemainingTime() + " whereIAm:"+whereIAm + " (dir "+direction+")");
			return direction;
		} else {
			//logger.trace("in getMove()");
			
			if(isTracker)
			{
				//if tracking track
				if(curTracking)
				{
					logger.debug(myPosition + " " + trackLocal);
					return Tracker.track(toTrack, myPosition, trackLocal);
				}
				//else rando-walk
				//return Tracker.track(null, myPosition, beast);
			}
			else
			{
				//if on task, go
				if(task == null)
				{
					task = taskManager.getNextTask(myPosition);
				}
				if(task!=null)
				{
					/** Elizabeth, please see this code below */
					Point2D objCoord = task.getObservation().getTheLocation().getLocation();
					
					if (objCoord!=null){
						if(myPosition.distance(task.getObservation().getTheLocation().getLocation()) < 5)
							task =null;
						else
							direction = Tracker.track(null, myPosition, task.getObservation().getTheLocation().getLocation());
					}
				}
				//else rando walk
			}
			
			Direction d = dangerFinder.findSafestDirection(myPosition, myPreviousPosition,
					whatYouSee, direction, false);
			
			if (d == null){
				
				//Why would d ever be null? There should always be a safest direction.
				d = getNewDirection();
			

				Point2D p = new Point2D.Double(whereIAm.getX() + d.dx,
						whereIAm.getY() + d.dy);
				while (Math.abs(p.getX()) > GameConfig.d
						|| Math.abs(p.getY()) > GameConfig.d) {
					d = getNewDirection();
					p = new Point2D.Double(whereIAm.getX() + d.dx,
							whereIAm.getY() + d.dy);
				}
			}
			return d;
		}
	}
	
	@Override
	public void newGame(Set<SeaLifePrototype> seaLifePossibilities, int penalty,
			int d, int r, int n) {
		this.seaLifePossibilities = seaLifePossibilities;
		this.penalty = penalty;
		this.d = d;
		this.r = r;
		this.n = n;
		this.round = 0;
		this.boatTimeBufferAdjusted = NavigateToBoat.getBoatTimeBufferAdjusted(BOAT_TIME_BUFFER, seaLifePossibilities, d);
		this.ourBoard = new OurBoard(d);
		this.dangerFinder = new DangerFinder(ourBoard, seaLifePossibilities, random);
		
		taskManager = new TaskManager(seaLifePossibilities, ourBoard);
		MessageTranslator.initializeMap(seaLifePossibilities);
		
		if(Math.random() >= 0.5)
			isTracker = true;
		else
			isSeeker = true;
	}


	private Direction getNewDirection() {
		int r = random.nextInt(100);
		if(r < 10 || direction == null)
		{
			ArrayList<Direction> directions = Direction.allBut(direction);
			direction = directions.get(random.nextInt(directions.size()));
		}
			return direction;
	}
	
	/**
	 * Get the remaining time out of 480 rounds (8 hours).
	 * The final round will return 0.
	 * GameEngine starts with round 0, and Game Over is before beginning of round 481.
	 * @return
	 */
	private int getRemainingTime() {
		return 480 - round;
	}


}
