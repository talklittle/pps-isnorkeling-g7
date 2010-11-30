package isnork.g7;

public class Coordinate {
	private int xcor;
	private int ycor; 
	
	public Coordinate(int xcor, int ycor){
		this.xcor = xcor; 
		this.ycor = ycor;
	}
	
	public int getX(){
		return xcor;
	}
	
	public int getY(){
		return ycor;
	}
	
}
