package isnork.g7;

/*Location is determined by a coordinate pair for static objects, or by a player for dynamic creatures*/
public class Location {

	private Coordinate coordinate;
	//playerID of the player following the object, which is used to find its current location
	private int playerID = -1;
	
	public Location(Coordinate coordinate){
		this.coordinate = coordinate;
	}
	
	public Location(int playerID){
		this.playerID = playerID;
		//get syntax right
		//coordinate = new Coordinate(playerID.getLocation().getX(), playerID.getLocation().getY());
	}
	
	public Coordinate getCoordinate(){
		return coordinate;
	}
	
	/*If there is no player following the observation, the location is static*/
	public boolean isStatic(){
		if (playerID == -1){
			return false;
		}
		return true;
	}
	
}
