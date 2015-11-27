package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ParkingSpotManager {
	public static Map <String, ParkingSpot> parkingSpots = new HashMap<String, ParkingSpot>();
	public static ArrayList<ParkingSpot> parkingSpotsOrdered = new ArrayList<ParkingSpot>();
	public static ArrayList<ParkingSpot> parkingSpotsOccupied = new ArrayList<ParkingSpot>();
	public static int getNextCounter = 0;
	
	/**
	 * Adds an empty parking spot to our database
	 * @param newSpot the parking spot
	 */
	public static void add(ParkingSpot newSpot){
		parkingSpots.put(newSpot.getX()+","+newSpot.getY(), newSpot);
		ArrayList<ParkingSpot> oldOrdered = new ArrayList<ParkingSpot>(parkingSpotsOrdered);
		parkingSpotsOrdered.clear();
		boolean done = false;
		
		for (ParkingSpot spot : oldOrdered) {			
			if(newSpot.getDistance() <= spot.getDistance() && done == false){
				parkingSpotsOrdered.add(newSpot);
				done = true;
			}
			parkingSpotsOrdered.add(spot);
		}
		if(done == false){
			parkingSpotsOrdered.add(newSpot);
			done = true;
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
				if(Algorithm.isMovable(curX+pl, curY) 
						&& Algorithm.isMovable(curX, curY+pk)
						&& Algorithm.isMovable(curX-pl, curY) 
						&& Algorithm.isMovable(curX, curY-pk)
						&& Algorithm.isMovable(curX-pl, curY-pk)
						&& Algorithm.isMovable(curX+pl, curY-pk)
						&& Algorithm.isMovable(curX-pl, curY+pk)
						&& Algorithm.isMovable(curX+pl, curY+pk)){
					
					return parkingSpot;
				}				
			}
		}
		return null;
	}
	
	public static ParkingSpot retrieveOccupied(int n){
		ParkingSpot parkingSpot;
		for(int i = 0; i < parkingSpotsOccupied.size(); i++){
			parkingSpot = parkingSpotsOccupied.get(i);
			if(parkingSpot.getID() == n){
				parkingSpotsOccupied.remove(i);
				parkingSpot.vacate();
				return parkingSpot;
			}
		}
		System.out.println("ERROR - something is really wrong");
		return null;
	}
	
	public static int getHighestID(){
		ParkingSpot parkingSpot;
		int highest = 0;
		for(int i = 0; i < parkingSpotsOccupied.size(); i++){
			parkingSpot = parkingSpotsOccupied.get(i);
			if(parkingSpot.getID() > highest){
				highest = parkingSpot.getID();
			}
		}
		return highest+1;
	}
	
	public static boolean isOccupied(int x, int y){
		if(isParking(x, y)){
			if(parkingSpots.get(x+","+y).occupied()){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			return false;
		}		
	}
	
	public static boolean isVacant(int x, int y){
		if(isParking(x, y)){
			if(parkingSpots.get(x+","+y).occupied()){
				return false;
			}
			else{
				return true;
			}
		}
		else{
			return false;
		}		
	}
	
	public static boolean isParking(int x, int y){
		if(parkingSpots.containsKey(x+","+y)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public static ParkingSpot getParking(int x, int y){
		return parkingSpots.get(x+","+y);
	}
}
