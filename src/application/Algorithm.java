package application;
import java.sql.Date;

import javafx.scene.layout.GridPane;

public class Algorithm{

	static int gateX = 0;
	static int gateY = 0;
    static int pl = 0;
    static int pk = 0;
    
    /**
     * Gets the type of the land.
     * @param x coordinate
     * @param y coordinate
     * @return type of the land. Ranges from 1-13
     */
    public static int getType(int x, int y){
    	return ((Plokk) Main.getNodeFromGridPane(Main.level2, x, y)).nr;
    }
    
    /**
     * TODO
     */
	public static void run(){
		
		pl = Main.pencil_width;
		pk = Main.pencil_height;		

		for(int i = 0; i<Main.level_width; i++){
			for(int j = 0; j<Main.level_height; j++){
				// milline on vana plokk kohas
				int uusP = ((Plokk) Main.getNodeFromGridPane(Main.level, i, j)).nr;

				if(uusP == 4 && gateX == 0 && gateY == 0){
					gateX = i;
					gateY = j;
				}
				
				// priit lisas; ignoreerib vanu parkimiskohti
				if (uusP == 1 | uusP ==  7 | uusP ==  8 | uusP ==  9 | uusP ==  10 | uusP ==  11 | uusP ==  12 | uusP ==  13){
					uusP = 2;
				}
			}
		}
		
		if(getType(gateX-1, gateY) == 2){
			go(-pl, 0, gateX-pl, gateY);
		}
		else if(getType(gateX, gateY-1) == 2){
			go(0, -pk, gateX, gateY-pk);
		}
		else if(getType(gateX, gateY+pk) == 2){
			go(0, pk, gateX, gateY+pk);
		}
		else if(getType(gateX+pl, gateY) == 2){
			go(pl, 0, gateX+pl, gateY);
		}
		
		fillSpotParking(gateX, gateY);
		fillPixelParking();
	}
   
	/**
	 * A method that does something by knowing which way something has to go to get into the whole parking lot. TODO
	 * @param stepX indicates which way the gateway leads
	 * @param stepY indicates which way the gateway leads
	 * @param x current coordinate
	 * @param y current coordinate
	 */
    public static void go(int stepX, int stepY, int x, int y){
		Bot temp = Main.makeBot(x-stepX,y-stepY);
		temp.moveRight( 6);
		temp.moveDown( 10);
		temp.moveLeft( 2);
		temp.toggleColor();
		temp.moveUp( 5);
		
		Bot temp2 = Main.makeBot(x-stepX,y);
		temp2.moveRight( 2);
		temp2.toggleColor();
		temp2.moveRight( 4);
		temp2.moveRight( 5);
		
		
    }
    
    /**
     * Adds a parking spot. Includes coloring the canvas and creating an empty parking object.
     * @param x where to create the given parking spot.
     * @param y where to create the given parking spot.
     * @param lvl for internal use. ~priidrik
     */
    public static void addParking(int x, int y, GridPane lvl){
    	new ParkingSpot(x, y, "", "", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()));
    	
    	boolean pTäht = false;
        for(int i = 0; i< pl; i++){
            for(int j = 0; j< pk; j++){                   
                if (i==0&&j==0){
                        Main.replace_block(7, x+i, y+j, lvl);
                }
                else if (i==0&&j==pk-1){
                        Main.replace_block(10, x+i, y+j, lvl);
                }
                else if (i==pl-1&&j==0){
                        Main.replace_block(8, x+i, y+j, lvl);
                }
                else if (i==pl-1&&j==pk-1){
                        Main.replace_block(9, x+i, y+j, lvl);
                }
               
                else if (i==0|| i==pl-1){
                        Main.replace_block(11, x+i, y+j, lvl);
                }
                else if(j==0||j==pk-1){
                        Main.replace_block(12, x+i, y+j, lvl);
                }
                else if (!pTäht){
                        pTäht=true;
                        Main.replace_block(1, x+i, y+j, lvl);
                }
                else{
                        Main.replace_block(13, x+i, y+j, lvl);
                }               
            }
        }
    }
    
    /**
     * Checks if the given area contains something.
     * @param x coordinate of the area
     * @param y coordinate of the area
     * @param the type to look for
     * @return true, if this area contains at least one type of a given object.
     */
    public static boolean contains(int x, int y, int type){
        for(int i = 0; i<pl; i++){
                for(int j = 0; j<pk; j++){
                        if(getType(x+i, y+j) == type){
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
                if(getType(x+i, y+j) != type){
                        return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Fills the parking lot with maximum amount of parking spots.
     * @param gateX the coordinate of the main gate
     * @param gateY the coordinate of the main gate
     */
    public static void fillSpotParking(int gateX, int gateY){
    	for(int i = gateX; i<Main.level_width; i=i+pl){
    		for(int j = gateY; j<Main.level_height; j=j+pk){		
        		if(containsAll(i, j, 2) == true){
        			addParking(i, j, Main.level2);
        		}       		
        	}
    	}
    	for(int i = gateX; i>0; i=i-pl){
    		for(int j = gateY; j<Main.level_height; j=j+pk){		
        		if(containsAll(i, j, 2) == true){
        			addParking(i, j, Main.level2);
        		}       		
        	}
    	}
    	for(int i = gateX; i>0; i=i-pl){
    		for(int j = gateY; j>0; j=j-pk){		
        		if(containsAll(i, j, 2) == true){
        			addParking(i, j, Main.level2);
        		}       		
        	}
    	}
    	for(int i = gateX; i<Main.level_width; i=i+pl){
    		for(int j = gateY; j>0; j=j-pk){		
        		if(containsAll(i, j, 2) == true){
        			addParking(i, j, Main.level2);
        		}       		
        	}
    	}
    }
    
    /**
     * Fills uneven spots with parking spots.
     */
    public static void fillPixelParking(){
    	for(int i = 0; i<Main.level_width; i++){
    		for(int j = 0; j<Main.level_height; j++){    			
        		if(containsAll(i, j, 2) == true){
        			addParking(i, j, Main.level2);
        		}       		
        	}
    	}
    }
}

