package application;

import java.util.ArrayList;
import java.util.Date;

public class ParkingSpotManager {
	public static ArrayList<ParkingSpot> parkingSpotsOrdered = new ArrayList<ParkingSpot>();
	
	public static void add(ParkingSpot newSpot){
		ArrayList<ParkingSpot> oldOrdered = new ArrayList<ParkingSpot>(parkingSpotsOrdered);
		parkingSpotsOrdered.clear();
		boolean done = false;
		
		
		for (ParkingSpot spot : oldOrdered) {			
			if(newSpot.getDistance() <= spot.getDistance()){
				parkingSpotsOrdered.add(newSpot);
			}
			parkingSpotsOrdered.add(spot);
		}
		if(done == false){
			parkingSpotsOrdered.add(newSpot);
		}
	}
	
	public static ParkingSpot getEmptyCheckers(){
		int pl = Algorithm.pl;
		int pk = Algorithm.pk;
		for (int i = 0; i<parkingSpotsOrdered.size(); i++) {
			ParkingSpot parkingSpot = parkingSpotsOrdered.get(i);
			if(!parkingSpot.occupied()){
				int curX = parkingSpot.getX();
				int curY = parkingSpot.getY();
				if(Algorithm.containsAll(curX+pl, curY, 2) && Algorithm.containsAll(curX, curY+pk, 2)
						&& Algorithm.containsAll(curX-pl, curY, 2) && Algorithm.containsAll(curX, curY-pk, 2)
						&& Algorithm.containsAll(curX-pl, curY-pk, 2) && Algorithm.containsAll(curX+pl, curY-pk, 2)){
					
					return parkingSpot;
				}				
			}
		}
		return null;
	}
}
