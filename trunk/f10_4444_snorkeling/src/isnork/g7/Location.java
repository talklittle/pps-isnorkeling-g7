package isnork.g7;


import java.awt.geom.Point2D;
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
		if (playerID == -1){
			//TRAVERSE CURRENT PLAYER LOCATION LIST AND RETURN THAT LOCATION
			return getPlayerLocation(playerID);
		}
		else
			return coordinate;
	}
	
	public void setPlayerLocations(Set<Observation> playerLocations){
		this.playerLocations = playerLocations;
	}
	
	private Point2D getPlayerLocation(int playerID){
		for (Observation player : playerLocations){
			if(player.getId() == playerID){
				return player.getLocation();
			}
		}
		return null;
	}

}
