package isnork.g7;

public class OurObservation {
	
	private int creatureID;
	private Location location;
	private boolean isValid;
	private String creatureName;
	
	public OurObservation(int creatureID, Location location){
		this.creatureID = creatureID;
		this.location = location;
		this.isValid = true;
		
		findCreatureName(creatureID);
	}

	public Location getLocation(){
		return location;
	}
	
	public boolean isValid(){
		return isValid;
	}
	
	public void setInvalid(){
		isValid = false;
	}
	
	public int getCreatureID(){
		return creatureID;
	}
	
	public int getHappiness(){
		//TO DO: Check syntax
		//return creatureID.getHappiness();
		return 0;
	}
	
	private void findCreatureName(int creatureID){
		
		
	}
	
	private String getCreatureName(){
		return creatureName;
	}

}
