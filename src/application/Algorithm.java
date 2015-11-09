package application;
import java.util.ArrayList;
import java.util.Date;

import javafx.scene.layout.GridPane;

public class Algorithm implements Runnable{

	static int gateX = 0;
	static int gateY = 0;
    static int pl = 0;
    static int pk = 0; 
    
    /**
     * TODO
     */
	public void run(){		
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
		fillPixelParking();
		//clearParkings();

		if(Main.getType(gateX-1, gateY) == 2){
			go(-pl, 0, gateX-pl, gateY);
		}
		else if(Main.getType(gateX, gateY-1) == 2){
			go(0, -pk, gateX, gateY-pk);
		}
		else if(Main.getType(gateX, gateY+pk) == 2){
			go(0, pk, gateX, gateY+pk);
		}
		else if(Main.getType(gateX+pl, gateY) == 2){
			go(pl, 0, gateX+pl, gateY);
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
		
		/*
		Bot r2 = Main.makeBot(x-stepX,y-stepY);
		while(true){
		r2.moveDown(5);
		r2.moveLeft(15);
		r2.moveDown(5);

		boolean kas = true;
		while(kas){

			if (!r2.isBusy){
				colorOccupied((int)r2.get_X(), (int)r2.get_Y());
				kas=false;
			}

			try {
				Thread.currentThread().sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		r2.moveDown(1);
		
		}
		*/
		
		
		/*	
		Bot r1 = Main.makeBot(x-stepX,y-stepY);
		
		ArrayList<String> c = new ArrayList<String>();
		c.add("a1,Audi R1,42424356");
//		c.add("a2,Audi R2,456356");
//		c.add("a3,Audi R3,45654356");
//		c.add("a4,Audi R3,45654356");
//		c.add("a4,Audi R4,644356");
//		c.add("a5,Audi R5,424356");
//		c.add("a6,Audi R6,932356");
//		c.add("a7,Audi R7,4424356");
//		c.add("a8,Audi R8,434");
//		c.add("a9,Audi R9,654656");
//		c.add("a170,Audi R10,656");
//		c.add("a79,Audi R9,654656");
//		c.add("a130,Audi R10,656");
//		c.add("a45,Audi R9,654656");
		
		while(c.size() != 0){
			String[] inf = c.get(0).split(",");
			String custodian = inf[0];
			String model = inf[1];
			Date expirationDate = new Date(Integer.parseInt(inf[2]));
			
			ParkingSpot parkingSpot;
			if((parkingSpot = ParkingSpotManager.getEmptyCheckers()) != null){
				
				String waypoints = pathfind(x, y, parkingSpot.getX(), parkingSpot.getY(), "");
				System.out.println(waypoints);
//				waypoints = optimize(waypoints);
//				System.out.println(waypoints);
				
				for (String string : waypoints.split(",,,")) {
					System.out.println(string);
				}
				
				r1.moveDown(1);
				r1.moveLeft(3);
				r1.moveDown(1);
				
				while(r1.isBusy){
					Thread.sleep(200);
				}
				parkingSpot.occupy(custodian, model, expirationDate);
				colorOccupied(parkingSpot.getX(), parkingSpot.getY());
				
//				r1.moveUp(1);
//				r1.moveRight(3);
//				r1.moveUp(1);
				
				
			}
			else{
				System.out.println("no space left for checkers");
			}
			
			c.remove(0);
		}
		}
			
		*/


		
		
    }
    
	private static String pathfind(int sX, int sY, int tX, int tY, String curPath){
		curPath = curPath+",,,"+sX+","+sY;
		if(sX == tX && sY == tY){
			return curPath;
		}
		else if(containsAll(sX, sY, 2)){
			String left;
			String right;
			String down;
			String up;
			if(curPath.contains((sX-pl)+","+sY)){
				left = null;
			}
			else{
				left = pathfind(sX-pl, sY, tX, tY, curPath);
			}
			if(curPath.contains((sX+pl)+","+sY)){
				right = null;
			}
			else{
				right = pathfind(sX+pl, sY, tX, tY, curPath);
			}
			if(curPath.contains(sX+","+(sY-pk))){
				up = null;
			}
			else{
				up = pathfind(sX, sY-pk, tX, tY, curPath);
			}
			if(curPath.contains(sX+","+(sY+pk))){
				down = null;
			}
			else{
				down = pathfind(sX, sY+pk, tX, tY, curPath);
			}
			return compareArraySizes(compareArraySizes(compareArraySizes(left, right),up),down);
		}
		else{
			return null;
		}
	}
	
//	private static String optimize(String path){
//		if(){
//			
//		}
//	}
	
	private static String compareArraySizes(String left, String right){
		if(left == null && right == null){
			return null;
		}
		else if(left == null){
			return right;
		}
		else if(right == null){
			return left;
		}
		else if(right.length() < left.length()){
			return right;
		}
		else{
			return left;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
    /**
     * Adds a parking spot. Includes coloring the canvas and creating an empty parking object.
     * @param x where to create the given parking spot.
     * @param y where to create the given parking spot.
     * @param lvl for internal use. ~priidrik
     */
    private static void createParkingSpot(int x, int y){
    	//ParkingSpotManager.add(new ParkingSpot(x, y, null, null, null, null));
    	//colorOccupied(x, y);
    	colorParking(x, y);
    }
    
    
	public static void colorParking(int x, int y){
        Main.colorParking(x, y, Main.level2);
	}
    
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
//                if(!Block.isMoveable( Main.getType(x+i, y+j) )){
//                	return false;
//                }
            }
        }
        return true;
    }
    
    /**
     * Fills the parking lot with maximum amount of parking spots in regards to the gate.
     * @param gateX the coordinate of the main gate
     * @param gateY the coordinate of the main gate
     */
    private static void fillSpotParking(int gateX, int gateY){
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
    }
    
    /**
     * Fills uneven spots with parking spots.
     */
    private static void fillPixelParking(){
    	for(int i = 0; i<Main.level_width; i++){
    		for(int j = 0; j<Main.level_height; j++){    			
        		if(containsAll(i, j, 2) == true){
        			createParkingSpot(i, j);
        		}       		
        	}
    	}
    }
    
    /**
     * TODO temporary method until priit fixes up the code
     */
    private static void clearParkings(){
    	for(int i = 0; i<Main.level_width; i++){
    		for(int j = 0; j<Main.level_height; j++){
    			int type = Main.getType(i, j);
        		if(type == 14 || type == 15 || type == 16 || type == 17 || type == 18
        				|| type == 19 || type == 20 || type == 21
        				|| type == 22 || type == 23 || type == 24
        				|| type == 25 || type == 26){
        			Main.replace_block(2, i, j, Main.level2);
        		}       		
        	}
    	}
    }
}

