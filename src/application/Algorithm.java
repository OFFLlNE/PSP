package application;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Algorithm implements Runnable{

	static int gateX = 0;
	static int gateY = 0;
    static int pl = 0;
    static int pk = 0; 
    static Map <String, String> beenTo = new HashMap<String, String>();
    static ArrayList<String> tasks = new ArrayList<String>();    
	static boolean stopped = false;
	
	static void kill(){
		System.out.println("killing thread");
		stopped = true;
	}
	
    public int make(){
		pl = Main.pencil_width;
		pk = Main.pencil_height;

		for(int i = 0; i<Main.level_width; i++){
			for(int j = 0; j<Main.level_height; j++){
				if(Main.getType(i, j) == 4 && gateX == 0 && gateY == 0){
					gateX = i;
					gateY = j;
				}
			}
		}
		
		fillSpotParking(gateX, gateY);
		System.out.println("Done filling spots!");
		
		fillPixelParking();
		System.out.println("Done filling pixels!");
		
		return ParkingSpotManager.parkingSpotsOrdered.size()+1;
    }
	
   
	/**
	 * The main algorithm that waits for requests to arrive and processes them on arrival
	 * @param stepX indicates which way the gateway leads
	 * @param stepY indicates which way the gateway leads
	 * @param x current coordinate
	 * @param y current coordinate
	 */
	public void run(){
		int stepX = 0;
		int stepY = 0;
		if(isMovable(gateX-1, gateY)){
			stepX = -pl;
		}
		else if(isMovable(gateX, gateY-1)){
			stepY = -pk;
		}
		else if(isMovable(gateX, gateY+pk)){
			stepY = pk;
		}
		else {
			stepX = pl;
		}
		
		Bot r1 = Main.makeBot(gateX, gateY);
		
		while(!stopped){
			if(tasks.size() != 0){
				String[] inf = tasks.get(0).split(",");
				
				ParkingSpot parkingSpot;
				if(inf.length != 1){
					if((parkingSpot = ParkingSpotManager.getEmptyCheckers()) != null){					
						deliver(r1, parkingSpot, inf[0], inf[1], new Date(Integer.parseInt(inf[2])));
						tasks.remove(0);
					}
					else if((parkingSpot = getNextRound(gateX+stepX, gateY+stepY, stepX, stepY)) != null){
						deliver(r1, parkingSpot, inf[0], inf[1], new Date(Integer.parseInt(inf[2])));
						tasks.remove(0);
					}
					else{
						System.out.println("ERROR - add puzzle delivery here");
					}
				}
				else{
					retrieve(r1, Integer.parseInt(inf[0]));
					tasks.remove(0);
				}
			}
			else{
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
    }
	
	/**
	 * Counter-clockwise algorithm to find a parking spot where to place the next car
	 * @param curX current coordinate
	 * @param curY current coordinate
	 * @param stepX which way the algorithm looks for the next step
	 * @param stepY which way the algorithm looks for the next step
	 * @return the target parking spot where to place the car
	 */
	public static ParkingSpot getNextRound(int curX, int curY, int stepX, int stepY){
		Map <String, String> beenTo2 = new HashMap<String, String>();
		beenTo2.put((curX-stepX)+","+(curY-stepY), "");
		int rightX;
		int rightY;
		int forwardX;
		int forwardY;
		int leftX;
		int leftY;
		while(true){
			beenTo2.put(curX+","+curY, "");
			if(stepX == -pl){
				rightX = 0;
				rightY = -pk;
				forwardX = -pl;
				forwardY = 0;
				leftX = 0;
				leftY = pk;
			}
			else if(stepX == pl){
				rightX = 0;
				rightY = pk;
				forwardX = pl;
				forwardY = 0;
				leftX = 0;
				leftY = -pk;
			}
			else if(stepY == -pk){
				rightX = pl;
				rightY = 0;
				forwardX = 0;
				forwardY = -pk;
				leftX = -pl;
				leftY = 0;
			}
			else{
				rightX = -pl;
				rightY = 0;
				forwardX = 0;
				forwardY = pk;
				leftX = pl;
				leftY = 0;
			}
			if(isParking(curX+rightX, curY+rightY) && !beenTo2.containsKey((curX+rightX)+","+(curY+rightY))){
				if(isVacant(curX+rightX, curY+rightY)){
					Map <String, String> map = new HashMap<String, String>();
					map.put(gateX+","+gateY, "");
					beenTo.clear();
					if((pathfind(curX+rightX, curY+rightY, map)) != null){
						if(checkSides(curX+rightX, curY+rightY)){
							return ParkingSpotManager.getParking(curX+rightX, curY+rightY);
						}
						else{
							curX = curX+rightX;curY = curY+rightY;stepX = rightX;stepY = rightY;
						}
					}
					else{System.out.println("ERROR UNKNOWN STUFF HAPPENED HERE CHECK HERE");}
				}
				else{					
					curX = curX+rightX;curY = curY+rightY;stepX = rightX;stepY = rightY;
				}
			}
			else if(isParking(curX+forwardX, curY+forwardY) && !beenTo2.containsKey((curX+forwardX)+","+(curY+forwardY))){
				if(isVacant(curX+forwardX, curY+forwardY)){
					Map <String, String> map = new HashMap<String, String>();
					map.put(gateX+","+gateY, "");
					beenTo.clear();
					if((pathfind(curX+forwardX, curY+forwardY, map)) != null){
						if(checkSides(curX+forwardX, curY+forwardY)){
							return ParkingSpotManager.getParking(curX+forwardX, curY+forwardY);
						}
						else{
							curX = curX+forwardX;curY = curY+forwardY;stepX = forwardX;stepY = forwardY;
						}
					}
					else{System.out.println("ERROR UNKNOWN STUFF HAPPENED HERE CHECK HERE");}
				}
				else{					
					curX = curX+forwardX;curY = curY+forwardY;stepX = forwardX;stepY = forwardY;
				}
			}
			else if(isParking(curX+leftX, curY+leftY) && !beenTo2.containsKey((curX+leftX)+","+(curY+leftY))){
				if(isVacant(curX+leftX, curY+leftY)){
					Map <String, String> map = new HashMap<String, String>();
					map.put(gateX+","+gateY, "");
					beenTo.clear();
					if((pathfind(curX+leftX, curY+leftY, map)) != null){
						if(checkSides(curX+leftX, curY+leftY)){
							return ParkingSpotManager.getParking(curX+leftX, curY+leftY);	
						}
						else{
							curX = curX+leftX;curY = curY+leftY;stepX = leftX;stepY = leftY;
						}
					}
					else{System.out.println("ERROR UNKNOWN STUFF HAPPENED HERE CHECK HERE");}
				}
				else{					
					curX = curX+leftX;curY = curY+leftY;stepX = leftX;stepY = leftY;
				}
			}
			else{
				return null;
			}
		}
	}
	
	/**
	 * Checks if placing the car to the center area blocks any other parking spots
	 * @param centerX The area where to place the car
	 * @param centerY The area where to place the car
	 * @return true if no blocking occurs
	 */
	private static boolean checkSides(int centerX, int centerY){
		beenTo.clear();
		beenTo.put(centerX+","+centerY, "");
		if(isParking(centerX+pl, centerY)){
			if(!isOccupied(centerX+pl, centerY)){
				Map <String, String> map = new HashMap<String, String>();
				map.put(gateX+","+gateY, "");
				if((pathfind(centerX+pl, centerY, map)) == null){
					return false;
				}
			}
		}
		beenTo.clear();
		beenTo.put(centerX+","+centerY, "");
		if(isParking(centerX-pl, centerY)){
			if(!isOccupied(centerX-pl, centerY)){
				Map <String, String> map = new HashMap<String, String>();
				map.put(gateX+","+gateY, "");
				if((pathfind(centerX-pl, centerY, map)) == null){
					return false;
				}
			}
		}
		beenTo.clear();
		beenTo.put(centerX+","+centerY, "");
		if(isParking(centerX, centerY+pk)){
			if(!isOccupied(centerX, centerY+pk)){
				Map <String, String> map = new HashMap<String, String>();
				map.put(gateX+","+gateY, "");
				if((pathfind(centerX, centerY+pk, map)) == null){
					return false;
				}
			}
		}
		beenTo.clear();
		beenTo.put(centerX+","+centerY, "");
		if(isParking(centerX, centerY-pk)){
			if(!isOccupied(centerX, centerY-pk)){
				Map <String, String> map = new HashMap<String, String>();
				map.put(gateX+","+gateY, "");
				if((pathfind(centerX, centerY-pk, map)) == null){
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Retrieves a car from the destination
	 * @param robot the working robot
	 * @param id the id of the car to retrieve
	 */
	private static void retrieve(Bot robot, int id){
		ParkingSpot parkingSpot = ParkingSpotManager.retrieveOccupied(id);
		if(parkingSpot.getX() != robot.getRobotX() || parkingSpot.getY() != robot.getRobotY()){
			goTo(robot, parkingSpot.getX(), parkingSpot.getY());
		}
		robot.toggleColor();
		colorParking(parkingSpot.getX(), parkingSpot.getY());
		goTo(robot, gateX, gateY);
		robot.toggleColor();
	}
	
	/**
	 * Delivers a car to the destination
	 * @param robot the working robot
	 * @param parkingSpot the parking spot to take to
	 * @param custodian of the car
	 * @param model of the car
	 * @param expirationDate of the car
	 */
	private static void deliver(Bot robot, ParkingSpot parkingSpot, String custodian, String model, Date expirationDate){
		if(robot.getRobotX() != gateX || robot.getRobotY() != gateY){
			goTo(robot, gateX, gateY);
		}
		robot.toggleColor();
		goTo(robot, parkingSpot.getX(), parkingSpot.getY());
		robot.toggleColor();
		parkingSpot.occupy(custodian, model, expirationDate);
		colorOccupied(parkingSpot.getX(), parkingSpot.getY());
	}
	
	/**
	 * Moves the robot to the target location
	 * @param b robot
	 * @param targetX coordinate
	 * @param targetY coordinate
	 */
	private static void goTo(Bot b, int targetX, int targetY){
		System.out.println("Pathfinding from "+b.getRobotX()+" "+b.getRobotY()+" to "+targetX+" "+targetY);
		beenTo.clear();
		Map <String, String> map = new HashMap<String, String>();
		map.put(b.getRobotX()+","+b.getRobotY(), "");
		String path = optimize("", pathfind(targetX, targetY, map).replace(",,,"+b.getRobotX()+","+b.getRobotY(), ""),  b.getRobotX()+"", b.getRobotY()+"", false, false);
		
		String[] waypoints = path.split(",,,");	
		for (int i = 1; i < waypoints.length; i++) {
			int sX = b.getRobotX();
			int sY = b.getRobotY();
			int tX = Integer.parseInt(waypoints[i].split(",")[0]);
			int tY = Integer.parseInt(waypoints[i].split(",")[1]);
			
			if(tX < sX){
				b.moveLeft(sX-tX);
			}
			else if(tX > sX){
				b.moveRight(tX-sX);
			}
			else if(tY < sY){
				b.moveUp(sY-tY);
			}
			else {
				b.moveDown(tY-sY);
			}
			while(b.isBusy){
				try {
					Thread.sleep(100);
				} 
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
    
	/**
	 * Pathfinds to the target location. It is highly recommended to run the output of this method through the optimizer.
	 * @param tX target X coordinate
	 * @param tY target Y coordinate
	 * @param curMap a map with one entry of which the key is the starting location as a String "24,57" and the value is an empty String "".
	 * @return null if the path can not be found
	 */
	private static String pathfind(int tX, int tY, Map <String, String> curMap){
		Map <String, String> newMap = new HashMap<String, String>();
		for (Map.Entry<String, String> entry : curMap.entrySet()){
			int sX = Integer.parseInt(entry.getKey().split(",")[0]);
			int sY = Integer.parseInt(entry.getKey().split(",")[1]);
			
			if(sX-pl == tX && sY == tY){
				return entry.getValue()+",,,"+entry.getKey()+",,,"+(sX-pl)+","+sY;
			}
			if(sX+pl == tX && sY == tY){
				return entry.getValue()+",,,"+entry.getKey()+",,,"+(sX+pl)+","+sY;
			}
			if(sX == tX && sY-pk == tY){
				return entry.getValue()+",,,"+entry.getKey()+",,,"+sX+","+(sY-pk);
			}
			if(sX == tX && sY+pk == tY){
				return entry.getValue()+",,,"+entry.getKey()+",,,"+sX+","+(sY+pk);
			}
			
			if(isMovable(sX-pl, sY) && !beenTo.containsKey((sX-pl)+","+sY)){
				beenTo.put((sX-pl)+","+sY,"");
				newMap.put((sX-pl)+","+sY, entry.getValue()+",,,"+entry.getKey());
			}			
			if(isMovable(sX+pl, sY) && !beenTo.containsKey((sX+pl)+","+sY)){
				beenTo.put((sX+pl)+","+sY,"");
				newMap.put((sX+pl)+","+sY, entry.getValue()+",,,"+entry.getKey());
			}
			if(isMovable(sX, sY-pk) && !beenTo.containsKey(sX+","+(sY-pk))){
				beenTo.put(sX+","+(sY-pk),"");				
				newMap.put(sX+","+(sY-pk), entry.getValue()+",,,"+entry.getKey());
			}
			if(isMovable(sX, sY+pk)  && !beenTo.containsKey(sX+","+(sY+pk))){
				beenTo.put(sX+","+(sY+pk), "");				
				newMap.put(sX+","+(sY+pk), entry.getValue()+",,,"+entry.getKey());
			}
		}
		if(newMap.size() == 0){
			return null;
		}
		else{
			return pathfind(tX, tY, newMap);
		}		
	}
	
    /**
     * Adds a parking spot. Includes coloring the canvas and creating an empty parking object.
     * @param x where to create the given parking spot.
     * @param y where to create the given parking spot.
     * @param lvl for internal use. ~priidrik
     */
    private static void createParkingSpot(int x, int y){
    	ParkingSpotManager.add(new ParkingSpot(x, y, null, null, null, null));
    	colorParking(x, y);
    }
    
    /**
     * Colors the spot as a possible parking spot
     * @param x coordinate
     * @param y coordinate
     */
    public static void colorParking(int x, int y){
        Main.colorParking(x, y, Main.level2);
    }
    
    /**
     * Colors the spot as occupied
     * @param x coordinate
     * @param y coordinate
     */
    public static void colorOccupied(int x, int y){
        Main.colorOccupied(x, y, Main.level2);
    }

    /**
     * Checks if the area is completely empty
     * @param x coordinate
     * @param y coordinate
     * @return true if the area is empty
     */
    public static boolean isInvisible(int x, int y){
        return Block.isInvisible(x, y);
    }
    
    /**
     * Checks if a robot can move on this terrain
     * @param x coordinate
     * @param y coordinate
     * @return true if the robot can move over this patch of land
     */
    public static boolean isMovable(int x, int y){
        return Block.isMovable(x, y);
    }
    
    /**
     * Checks if the area is a parking lot
     * @param x coordinate
     * @param y coordinate
     * @return true if the area is a parking lot
     */
    public static boolean isParking(int x, int y){
        return ParkingSpotManager.isParking(x, y);
    }
    
    /**
     * Checks if the area is an occupied parking lot
     * @param x coordinate
     * @param y coordinate
     * @return true if the area is an occupied parking lot
     */
    public static boolean isOccupied(int x, int y){
        return ParkingSpotManager.isOccupied(x, y);
    }
    
    /**
     * Checks if the area is an unoccupied parking lot
     * @param x coordinate
     * @param y coordinate
     * @return true if the area is an unoccupied parking lot
     */
    public static boolean isVacant(int x, int y){
        return ParkingSpotManager.isVacant(x, y);
    }
    
    /**
     * Fills the parking lot with maximum amount of parking spots in regards to the gate.
     * @param gateX the coordinate of the main gate
     * @param gateY the coordinate of the main gate
     * @return 
     */
    private static boolean fillSpotParking(int gateX, int gateY){
    	for(int i = gateX; i<Main.level_width; i=i+pl){
    		for(int j = gateY; j<Main.level_height; j=j+pk){		
        		if(isInvisible(i, j)){
        			createParkingSpot(i, j);
        		}       		
        	}
    	}
    	for(int i = gateX; i>0; i=i-pl){
    		for(int j = gateY; j<Main.level_height; j=j+pk){		
        		if(isInvisible(i, j)){
        			createParkingSpot(i, j);
        		}       		
        	}
    	}
    	for(int i = gateX; i>0; i=i-pl){
    		for(int j = gateY; j>0; j=j-pk){		
        		if(isInvisible(i, j)){
        			createParkingSpot(i, j);
        		}       		
        	}
    	}
    	for(int i = gateX; i<Main.level_width; i=i+pl){
    		for(int j = gateY; j>0; j=j-pk){		
        		if(isInvisible(i, j)){
        			createParkingSpot(i, j);
        		}       		
        	}
    	}
    	return true;
    }
    
    /**
     * Fills uneven spots with parking spots.
     * @return 
     */
    private static boolean fillPixelParking(){
    	for(int i = 0; i<Main.level_width; i++){
    		for(int j = 0; j<Main.level_height; j++){    			
        		if(isInvisible(i, j)){
        			createParkingSpot(i, j);
        		}       		
        	}
    	}
    	return true;
    }
    
    //For internal use
  	private static String optimize(String out, String path, String left, String right, boolean l, boolean r){
  		if(path == null){
  			return null;
  		}
  		String[] parts = path.split(",,,");
  		String newPath = "";
  		
  		if(parts.length == 1){
  			return out+",,,"+left+","+right;			
  		}
  		
  		String a1 = parts[1].split(",")[0];
  		String a2 = parts[1].split(",")[1];
  		for(int i = 2; i<parts.length;i++){
  			newPath = newPath+",,,"+parts[i];			
  		}
  		
  		if(left == null && right == null){
  			out = out+",,,"+a1+","+a2;
  			out = optimize(out, newPath, a1, a2, false, false);			
  		}
  		else if(r){
  			if(a2.equals(right)){
  				out = optimize(out, newPath, a1, a2, false, true);
  			}
  			else{
  				out = out+",,,"+left+","+right;
  				
  				if(left.equals(a1)){
  					out = optimize(out, newPath, a1, a2, true, false);
  				}
  				else{
  					out = out+",,,"+a1+","+a2;
  					out = optimize(out, newPath, a1, a2, false, false);
  				}
  				
  			}
  		}
  		else if(l){
  			if(a1.equals(left)){
  				out = optimize(out, newPath, a1, a2, true, false);
  			}
  			else{
  				out = out+",,,"+left+","+right;
  				
  				if(right.equals(a2)){
  					out = optimize(out, newPath, a1, a2, false, true);
  				}
  				else{	
  					out = out+",,,"+a1+","+a2;
  					out = optimize(out, newPath, a1, a2, false, false);
  				}				
  			}
  		}
  		else if(a2.equals(right)){
  			out = optimize(out, newPath, a1, a2, false, true);		
  		}
  		else if(a1.equals(left)){
  			out = optimize(out, newPath, a1, a2, true, false);
  		}		
  		return out;
  	}
}

