package isnork.g7;

import java.awt.geom.Point2D;
import isnork.sim.GameObject.Direction;

public class OurBoard {
	
	
	/*Returns the direction between to points*/
	public Direction getDirectionTowards(Point2D from, Point2D to){
		
		if (from.getX() < to.getX() && from.getY() > to.getY()){
			return Direction.NE;
		}
		else if(from.getX() < to.getX() && from.getY() == to.getY()){
			return Direction.E;
		}
		else if(from.getX() < to.getX() && from.getY() < to.getY()){
			return Direction.SE;
		}
		else if(from.getX() == to.getX() && from.getY() < to.getY()){
			return Direction.S;
		}
		else if(from.getX() > to.getX() && from.getY() < to.getY()){
			return Direction.SW;
		}
		else if(from.getX() > to.getX() && from.getY() == to.getY()){
			return Direction.W;
		}
		else if(from.getX() > to.getX() && from.getY() > to.getY()){
			return Direction.NW;
		}
		else if(from.getX() == to.getX() && from.getY() > to.getY()){
			return Direction.N;
		}
		
		return null;
	}
	
	public Direction getOppositeDirection(Direction d){
		if (d == Direction.NE)
			return Direction.SW;
		else if (d == Direction.E)
			return Direction.W;
		else if (d == Direction.SE)
			return Direction.NW;
		else if (d == Direction.S)
			return Direction.N;
		else if (d == Direction.SW)
			return Direction.NE;
		else if (d == Direction.W)
			return Direction.E;
		else if (d == Direction.NW)
			return Direction.SE;
		else if (d == Direction.N)
			return Direction.S;
		else 
			return null;
	}

}
