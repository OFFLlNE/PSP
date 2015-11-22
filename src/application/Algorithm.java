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
    static String beenTo = "";
    
	public static boolean stopped = false;
	public static void kill(){
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
     * TODO
     */
	public void run(){		

		System.out.println(gateX+" "+gateY);
		if(isMovable(gateX-1, gateY)){
			go(-pl, 0, gateX, gateY);
		}
		else if(isMovable(gateX, gateY-1)){
			go(0, -pk, gateX, gateY);
		}
		else if(isMovable(gateX, gateY+pk)){
			go(0, pk, gateX, gateY);
		}
		else {
			go(pl, 0, gateX, gateY);
		}
	}
   
	/**
	 * A method that does something by knowing which way something has to go to get into the whole parking lot. TODO
	 * @param stepX indicates which way the gateway leads
	 * @param stepY indicates which way the gateway leads
	 * @param x current coordinate
	 * @param y current coordinate
	 */
	private static void go(int stepX, int stepY, int x, int y){
		Bot r1 = Main.makeBot(x,y);
		
		ArrayList<String> c = new ArrayList<String>();
		c.add("a1,Audi R1,42424356");
		c.add("a2,Audi R2,456356");
		c.add("a3,Audi R3,45654356");
		c.add("a4,Audi R3,45654356");
		c.add("a4,Audi R4,644356");
		c.add("a5,Audi R5,424356");
		c.add("a6,Audi R6,932356");
		c.add("a7,Audi R7,4424356");
		
		while(!stopped && c.size() != 0){ //from here
			String[] inf = c.get(0).split(",");
			String custodian = inf[0];
			String model = inf[1];
			Date expirationDate = new Date(Integer.parseInt(inf[2]));
			
			ParkingSpot parkingSpot;
			if((parkingSpot = ParkingSpotManager.getEmptyCheckers()) != null){				
				r1.toggleColor();
					
				System.out.println("Pathfinding from "+r1.getRobotX()+" "+r1.getRobotY()+" to "+parkingSpot.getX()+" "+parkingSpot.getY());
				beenTo = "";
				Map <String, String> map = new HashMap<String, String>();
				map.put(r1.getRobotX()+","+r1.getRobotY(), "");
				String path = optimize("", pathfind(parkingSpot.getX(), parkingSpot.getY(), map).replace(",,,"+r1.getRobotX()+","+r1.getRobotY(), ""),  r1.getRobotX()+"", r1.getRobotY()+"", false, false);
				
				int backX = r1.getRobotX();
				int backY = r1.getRobotY();
				String[] waypoints = path.split(",,,");	
				for (int i = 1; i < waypoints.length; i++) {
					int sX = r1.getRobotX();
					int sY = r1.getRobotY();
					int tX = Integer.parseInt(waypoints[i].split(",")[0]);
					int tY = Integer.parseInt(waypoints[i].split(",")[1]);
					
					if(tX < sX){
						r1.moveLeft(sX-tX);
					}
					else if(tX > sX){
						r1.moveRight(tX-sX);
					}
					else if(tY < sY){
						r1.moveUp(sY-tY);
					}
					else {
						r1.moveDown(tY-sY);
					}
					while(r1.isBusy){
						try {
							Thread.sleep(100);
						} 
						catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				parkingSpot.occupy(custodian, model, expirationDate);
				colorOccupied(parkingSpot.getX(), parkingSpot.getY());
				r1.toggleColor();
				
				//go back
				System.out.println("Pathfinding from "+r1.getRobotX()+" "+r1.getRobotY()+" to "+backX+" "+backY);
				beenTo = "";
				map = new HashMap<String, String>();
				map.put(r1.getRobotX()+","+r1.getRobotY(), "");
				path = optimize("", pathfind(backX, backY, map).replace(",,,"+r1.getRobotX()+","+r1.getRobotY(), ""),  r1.getRobotX()+"", r1.getRobotY()+"", false, false);
				
				waypoints = path.split(",,,");
				for (int i = 1; i < waypoints.length; i++) {
					int sX = r1.getRobotX();
					int sY = r1.getRobotY();
					int tX = Integer.parseInt(waypoints[i].split(",")[0]);
					int tY = Integer.parseInt(waypoints[i].split(",")[1]);
					
					if(tX < sX){
						r1.moveLeft(sX-tX);
					}
					else if(tX > sX){
						r1.moveRight(tX-sX);
					}
					else if(tY < sY){
						r1.moveUp(sY-tY);
					}
					else {
						r1.moveDown(tY-sY);
					}
					while(r1.isBusy){
						try {
							Thread.sleep(100);
						} 
						catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
			else{
				System.out.println("ERROR - use non checkers here");
			}			
			c.remove(0);
		}
    }
    
	private static String pathfind(int tX, int tY, Map <String, String> curMap){
		Map <String, String> newMap = new HashMap<String, String>();
		
		for (Map.Entry<String, String> entry : curMap.entrySet()){
			int sX = Integer.parseInt(entry.getKey().split(",")[0]);
			int sY = Integer.parseInt(entry.getKey().split(",")[1]);
			
			if(isMovable(sX-pl, sY) && !beenTo.contains((sX-pl)+","+sY)){
				beenTo = beenTo+",,,"+(sX-pl)+","+sY;
				if(sX-pl == tX && sY == tY){
					return entry.getValue()+",,,"+entry.getKey()+",,,"+(sX-pl)+","+sY;
				}
				newMap.put((sX-pl)+","+sY, entry.getValue()+",,,"+entry.getKey());
			}
			if(isMovable(sX+pl, sY) && !beenTo.contains((sX+pl)+","+sY)){
				beenTo = beenTo+",,,"+(sX+pl)+","+sY;
				if(sX+pl == tX && sY == tY){
					return entry.getValue()+",,,"+entry.getKey()+",,,"+(sX+pl)+","+sY;
				}
				newMap.put((sX+pl)+","+sY, entry.getValue()+",,,"+entry.getKey());
			}
			if(isMovable(sX, sY-pk) && !beenTo.contains(sX+","+(sY-pk))){
				beenTo = beenTo+",,,"+sX+","+(sY-pk);
				if(sX == tX && sY-pk == tY){
					return entry.getValue()+",,,"+entry.getKey()+",,,"+sX+","+(sY-pk);
				}
				newMap.put(sX+","+(sY-pk), entry.getValue()+",,,"+entry.getKey());
			}
			if(isMovable(sX, sY+pk)  && !beenTo.contains(sX+","+(sY+pk))){
				beenTo = beenTo+",,,"+sX+","+(sY+pk);
				if(sX == tX && sY+pk == tY){
					return entry.getValue()+",,,"+entry.getKey()+",,,"+sX+","+(sY+pk);
				}
				newMap.put(sX+","+(sY+pk), entry.getValue()+",,,"+entry.getKey());
			}
		}
		if(newMap.size() == 0){
			System.out.println("ERROR - implement moving puzzle pathfind here");
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
     * Checks if the given area contains something.
     * @param x coordinate of the area
     * @param y coordinate of the area
     * @param the type to look for
     * @return true, if this area contains at least one type of a given object.
     */
    private boolean contains(int x, int y, int type){
        for(int i = 0; i<pl; i++){
                for(int j = 0; j<pk; j++){
                        if(Main.getType(x+i, y+j) == type){
                                return true;
                        }
                }
        }
        return false;
    }
    
    /**
     * Checks if the given area is filled with only one type of objects.
     * @param x coordinate of the area
     * @param y coordinate of the area
     * @param the type to look for
     * @return true if the whole area is filled with given type
     */
    public static boolean containsAll(int x, int y, int type){
        for(int i = 0; i<pl; i++){
            for(int j = 0; j<pk; j++){
        		if(x+i < 0 || x+i > Main.level_width || y+j > Main.level_height || y+j < 0){
        			return false;
        		}
                if(Main.getType(x+i, y+j) != type){
                        return false;
                }
            }
        }
        return true;
    }
    
    /**
     * 
     * @param x coordinate
     * @param y coordinate
     * @return true if the robot can move over this patch of land
     */
    public static boolean isMovable(int x, int y){
        for(int i = 0; i<pl; i++){
            for(int j = 0; j<pk; j++){
        		if(x+i < 0 || x+i > Main.level_width || y+j > Main.level_height || y+j < 0){
        			return false;
        		}
                if(!Block.isMoveable( Main.getType(x+i, y+j) )){
                    return false;
                }
            }
        }
        return true;
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
        		if(containsAll(i, j, 2) == true){
        			createParkingSpot(i, j);
        		}       		
        	}
    	}
    	for(int i = gateX; i>0; i=i-pl){
    		for(int j = gateY; j<Main.level_height; j=j+pk){		
        		if(containsAll(i, j, 2) == true){
        			createParkingSpot(i, j);
        		}       		
        	}
    	}
    	for(int i = gateX; i>0; i=i-pl){
    		for(int j = gateY; j>0; j=j-pk){		
        		if(containsAll(i, j, 2) == true){
        			createParkingSpot(i, j);
        		}       		
        	}
    	}
    	for(int i = gateX; i<Main.level_width; i=i+pl){
    		for(int j = gateY; j>0; j=j-pk){		
        		if(containsAll(i, j, 2) == true){
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
        		if(containsAll(i, j, 2) == true){
        			createParkingSpot(i, j);
        		}       		
        	}
    	}
    	return true;
    }
    
    //For internal use
  	private static String optimize(String out, String path, String left, String right, boolean l, boolean r){
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

