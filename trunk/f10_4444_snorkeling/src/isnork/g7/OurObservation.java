package isnork.g7;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.Set;

import isnork.sim.*;

public class OurObservation extends Observation implements Comparable<Observation> {
	
	private boolean isValid;
	private String creatureName;
	private Set<SeaLifePrototype> seaLifePossibilities;
	private int playerID = -1; 
	private Set<Observation> playerLocations;
	private Point2D location;
	
	public Observation o;
	
	public OurObservation(Observation _o)
	{
		o = _o;
	}
	
	public OurObservation(String creatureName, int playerID, Set<SeaLifePrototype> seaLifePossibilities, Set<Observation> playerLocations){
		super();
		this.creatureName = creatureName;
		this.isValid = true;
		this.seaLifePossibilities = seaLifePossibilities;
		this.playerID = playerID;
	}
	
	public OurObservation(String creatureName, Set<SeaLifePrototype> seaLifePossibilities, Set<Observation> playerLocations){
		super();
		this.creatureName = creatureName;
		this.isValid = true;
		this.seaLifePossibilities = seaLifePossibilities;
		this.playerLocations = playerLocations;
	}
	
	public boolean isValid(){
		return isValid;
	}
	
	public void setInvalid(){
		isValid = false;
	}
	
	public String getCreatureName(){
		return creatureName;
	}
	
	public Point2D getLocation(){
		if (playerID != -1){
			return getPlayer(playerID).getLocation();
		}
		else{
			return super.getLocation();
		}
	}
	
	
	public Observation getPlayer(int playerID){
		Iterator<Observation> playerIterator = playerLocations.iterator();
		
		Observation nextPlayer = playerIterator.next();
		
		while(nextPlayer != null){
			if (nextPlayer.getId() == playerID)
				return nextPlayer;
			nextPlayer = playerIterator.next();
		}
		
		return nextPlayer;
	}
	
	public void setLocation(Point2D coord){
		location = coord;
	}

	@Override
	public int compareTo(Observation oo) {
		if(o.happiness()>oo.happiness())
			return -1;
		if(o.happiness()<oo.happiness())
			return 1;
		return 0;
	}
}
