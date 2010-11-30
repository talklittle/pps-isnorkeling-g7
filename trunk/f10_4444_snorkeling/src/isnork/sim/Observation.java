package isnork.sim;

import isnork.sim.GameObject.Direction;

import java.awt.geom.Point2D;

public class Observation {
	protected Point2D location;
	int id;
	String name;
	Direction dir;
	
	public Point2D getLocation(){
		return location;
	}
	public int getId()
	{
		return id;
	}
	public String getName()
	{
		return name;
	}
	public Direction getDirection()
	{
		return dir;
	}
	public boolean isDangerous()
	{
		return danger;
	}
	public int happiness()
	{
		return happy;
	}

	int happy;
	boolean danger;
}