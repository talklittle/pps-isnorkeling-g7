package isnork.g7;

import isnork.sim.Observation;

import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;


public class SpecialCaseAnalyzer {
	
	private static final int HIGH_DANGER_THRESHOLD = 9000;
	private static final int MED_DANGER_THRESHOLD = 2000;
	private static final int LOW_DANGER_THRESHOLD = 500;

	private static final Logger logger = Logger.getLogger(SpecialCaseAnalyzer.class);
	
	public String detectDangerousMap(Set<Observation> seaLifePossibilities){
		Iterator<Observation> seaLifePossibilitiesIt = seaLifePossibilities.iterator();
		
		int boardDangerTotal = 0;
		
		while(seaLifePossibilitiesIt.hasNext()){
			Observation nextSeaLife = seaLifePossibilitiesIt.next();
			
			if (nextSeaLife.isDangerous()){
				boardDangerTotal += (nextSeaLife.happiness()*2);
			}
		}
		
		if (boardDangerTotal > HIGH_DANGER_THRESHOLD){
			return "Extreme danger";
		}
		else if (boardDangerTotal > MED_DANGER_THRESHOLD){
			return "Medium danger";
		}
		else if (boardDangerTotal > LOW_DANGER_THRESHOLD){
			return "Low danger";
		}
		else{
			return "Very low danger";
		}
	}
	

	
}
