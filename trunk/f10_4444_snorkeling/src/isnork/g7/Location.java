package isnork.g7;


import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.Set;


import isnork.sim.*;


public class Location {
	
	private Point2D coordinate;
	private int playerID = -1;
	private Set<Observation> playerLocations;
	
	public Location(Point2D coordinate){
		this.coordinate = coordinate;
	}
	
	public Location(int playerID){
		this.playerID = playerID;
	}
	
	public Point2D getLocation(){
		if (playerID != -1){
			System.out.println("Getting location from player " + playerID);
			//TRAVERSE CURRENT PLAYER LOCATION LIST AND RETURN THAT LOCATION
			return getPlayerLocation(playerID);
		}
		else{
			System.out.println("Returning a static coordinate");
			return coordinate;
		}
	}
	
	public void setPlayerLocations(Set<Observation> playerLocations){
		this.playerLocations = playerLocations;
	}
	
	private Point2D getPlayerLocation(int playerID){
		System.out.println("playerLocations");
		System.out.println(playerLocations);
		
		if(playerLocations!= null){
			Iterator<Observation> playerIterator = playerLocations.iterator();
			
			while(playerIterator.hasNext()){
				Observation nextPlayer = playerIterator.next();
				if(nextPlayer.getId() == playerID  && nextPlayer!=null){
					return nextPlayer.getLocation();
				}
			}
		}
		
		return null;
	}

}
