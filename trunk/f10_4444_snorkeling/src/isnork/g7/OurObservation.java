package isnork.g7;

public class OurObservation {
	
	private Location location;
	private boolean isValid;
	private String creatureName;
	
	public OurObservation(String creatureName, Location location){
		this.creatureName = creatureName;
		this.location = location;
		this.isValid = true;
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

	public int getHappiness(){
		//TO DO: Check syntax
		//return creatureID.getHappiness();
		return 0;
	}
	
	public String getCreatureName(){
		return creatureName;
	}

}
