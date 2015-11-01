package application;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.layout.GridPane;

public class Algorithm{

    static int pl = 0;
    static int pk = 0;

    static int pencil_width = Main.pencil_width;
    static int pencil_height = Main.pencil_height;
    
    static Map <Integer, String> map = new HashMap<Integer, String>();
    static int mapC = 0;
    
	public static void run(){
		int gateX = (int) Double.POSITIVE_INFINITY;
		int gateY = (int) Double.POSITIVE_INFINITY;
		pl = Main.pencil_width;
		pk = Main.pencil_height;
		pencil_width = Main.pencil_width;
		pencil_height = Main.pencil_height;
		

		for(int i = 0; i<Main.level_width; i++){
			for(int j = 0; j<Main.level_height; j++){


				int uusP = ((Plokk) Main.getNodeFromGridPane(Main.level, i, j)).nr; // milline on vana plokk kohas i, j
				//multiple entries - per 9 piece batches

				if(uusP == 4){
					if(gateX > i){
						gateX = i;
					}
					if(gateY > j){
						gateY = j;
					}
				}
				
				// priit lisas; ignoreerib vanu parkimiskohti
				if (uusP == 1 | uusP ==  7 | uusP ==  8 | uusP ==  9 | uusP ==  10 | uusP ==  11 | uusP ==  12 | uusP ==  13){
					uusP = 2;
				}
				
				//Main.add_block2( uusP  , i, j  );      // lisab uude Main.levelisse samasse kohta samasuguse ploki

			}
		}

		
		map = new HashMap<Integer, String>();
		mapC = 0;
		
		//the initial counters RNG (maybe further ones aswell)
		if(getType(gateX-1, gateY) == 2){
			go(-pl, 0, gateX, gateY, 0);
		}
		else if(getType(gateX, gateY-1) == 2){
			go(0, -pk, gateX, gateY, 0);
		}
		else if(getType(gateX+pl, gateY+pk+1) == 2){
			go(0, pk, gateX, gateY, 0);
		}
		else if(getType(gateX+pl+1, gateY+pk) == 2){
			go(pl, 0, gateX, gateY, 0);
		}
		
		
		for (Map.Entry<Integer, String> entry : map.entrySet()){
			int a = Integer.parseInt(entry.getValue().split(",")[0]);
			int b = Integer.parseInt(entry.getValue().split(",")[1]);
		    if(containsAll(a, b, 2)){
		    	addParking(a, b, Main.level2);
		    }
		}
		
		
		fillAllParking();

	}
	
    //LAIMIS START
    
    public static int getType(int x, int y){
    	return ((Plokk) Main.getNodeFromGridPane(Main.level2, x, y)).nr;
    }
   
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
   
    public static void go(int x, int y, int gateX, int gateY, int counter){
            if(containsAll(gateX+x*2, gateY+y*2, 2) == true && containsAll(gateX+x*3, gateY+y*3, 2) == true
                            && containsAll(gateX+x*4, gateY+y*4, 2) == true && containsAll(gateX+x, gateY+y, 2) == true){
            	
            	if(y == 0){
            		if(contains(gateX+x, gateY+pk, 3) || contains(gateX+x, gateY+pk*2, 3) || contains(gateX+x, gateY+pk*3, 3) ||
            				contains(gateX+x, gateY-pk, 3) || contains(gateX+x, gateY-pk*2, 3) || contains(gateX+x, gateY-pk*3, 3) ||
            				contains(gateX+x*2, gateY+pk, 3) || contains(gateX+x*2, gateY+pk*2, 3) || contains(gateX+x*2, gateY+pk*3, 3) ||
            				contains(gateX+x*2, gateY-pk, 3) || contains(gateX+x*2, gateY-pk*2, 3) || contains(gateX+x*2, gateY-pk*3, 3) ||
            				contains(gateX+x*3, gateY+pk, 3) || contains(gateX+x*3, gateY+pk*2, 3) || contains(gateX+x*3, gateY+pk*3, 3) ||
            				contains(gateX+x*3, gateY-pk, 3) || contains(gateX+x*3, gateY-pk*2, 3) || contains(gateX+x*3, gateY-pk*3, 3) ||
            				contains(gateX+x*4, gateY+pk, 3) || contains(gateX+x*4, gateY+pk*2, 3) || contains(gateX+x*4, gateY+pk*3, 3) ||
            				contains(gateX+x*4, gateY-pk, 3) || contains(gateX+x*4, gateY-pk*2, 3) || contains(gateX+x*4, gateY-pk*3, 3)){
            			return;
                	}
            	}
            	else{
            		if(contains(gateX+pl, gateY+y, 3) || contains(gateX+pl*2, gateY+y, 3) || contains(gateX+pl*3, gateY+y, 3) ||
            				contains(gateX-pl, gateY+y, 3) || contains(gateX-pl*2, gateY+y, 3) || contains(gateX-pl*3, gateY+y, 3) ||
            				contains(gateX+pl, gateY+y*2, 3) || contains(gateX+pl*2, gateY+y*2, 3) || contains(gateX+pl*3, gateY+y*2, 3) ||
            				contains(gateX-pl, gateY+y*2, 3) || contains(gateX-pl*2, gateY+y*2, 3) || contains(gateX-pl*3, gateY+y*2, 3) ||
            				contains(gateX+pl, gateY+y*3, 3) || contains(gateX+pl*2, gateY+y*3, 3) || contains(gateX+pl*3, gateY+y*3, 3) ||
            				contains(gateX-pl, gateY+y*3, 3) || contains(gateX-pl*2, gateY+y*3, 3) || contains(gateX-pl*3, gateY+y*3, 3) ||
            				contains(gateX+pl, gateY+y*4, 3) || contains(gateX+pl*2, gateY+y*4, 3) || contains(gateX+pl*3, gateY+y*4, 3) ||
            				contains(gateX-pl, gateY+y*4, 3) || contains(gateX-pl*2, gateY+y*4, 3) || contains(gateX-pl*3, gateY+y*4, 3)){
                		return;
                	}
            	}
            	
                addRoad(gateX+x, gateY+y);
                
                go(x, y, gateX+x, gateY+y, counter+1); 
                if(counter%1 == 0){
                        if(y == 0){
                                go(0, pk, gateX+x, gateY+y, 0);
                                go(0, -pk, gateX+x, gateY+y, 0);
                        }
                        else{
                                go(pl, 0, gateX+x, gateY+y, 0);
                                go(-pl, 0, gateX+x, gateY+y, 0);
                        }                              
                }
            }            
            //PARKING SPOTS WHEN REACHING THE WALL
            else{
            	int[] way = new int[7];
            	way[0] = 0;
            	way[1] = -1;
            	way[2] = -2;
            	way[3] = -3;
            	way[4] = 1;
            	way[5] = 2;
            	way[6] = 3;
            	
                int t1 = 0; int t2 = 1;
                if(y == 0){
                        t1 = 1; t2 = 0;
                }
                
                boolean a = false;
                boolean b = false;
                boolean c = false;
                boolean aa = false;
                boolean bb = false;
                boolean cc = false;
                for(int k = 0; k< 7; k++){
                	int i = way[k];
                	if(i == -1 || i == 1){
                		a = aa;
                		b = bb;
                		c = cc;
                	}
                	if(containsAll(gateX+(x)+(pl*i)*t2, gateY+(y)+(pk*i)*t1, 2) == true && a == false){
                		if(i == 2 || i == 3 || i == -2 || i == -3){
                			map.put(mapC, (gateX+(x)+(pl*i)*t2)+","+(gateY+(y)+(pk*i)*t1));
                			mapC += 1;
                		}
                		else{
                			addParking(gateX+(x)+(pl*i)*t2, gateY+(y)+(pk*i)*t1, Main.level2);
                		}                        
                        if(containsAll(gateX+(x*2)+(pl*i)*t2, gateY+(y*2)+(pk*i)*t1, 2) == true && b == false){
                        	if(i == 2 || i == 3 || i == -2 || i == -3){
                    			map.put(mapC, (gateX+(x*2)+(pl*i)*t2)+","+(gateY+(y*2)+(pk*i)*t1));
                    			mapC += 1;
                    		}
                    		else{
                    			addParking(gateX+(x*2)+(pl*i)*t2, gateY+(y*2)+(pk*i)*t1, Main.level2);
                    		}                            
                            if(containsAll(gateX+(x*3)+(pl*i)*t2, gateY+(y*3)+(pk*i)*t1, 2) == true && c == false){
                            	if(i == 2 || i == 3 || i == -2 || i == -3){
                        			map.put(mapC, (gateX+(x*3)+(pl*i)*t2)+","+(gateY+(y*3)+(pk*i)*t1));
                        			mapC += 1;
                        		}
                        		else{
                        			addParking(gateX+(x*3)+(pl*i)*t2, gateY+(y*3)+(pk*i)*t1, Main.level2);
                        		}                                    
                            }
                            else{
                            	c = true;
                            }
                        }
                        else{
                        	b = true;
                        }
                    }
                	else{
                		a = true;
                	}
                	if(i == 0){
                		aa = a;
                		bb = b;
                		cc = c;
                	}
				}
            }
    }

    public static void addParking(int x, int y, GridPane lvl){
    	
    	boolean pTäht = false;
        for(int i = 0; i< pencil_width; i++){
                for(int j = 0; j< pencil_height; j++){
                       
                                if (i==0&&j==0){
                                        Main.replace_block(7, x+i, y+j, lvl);
                                }
                                else if (i==0&&j==pencil_height-1){
                                        Main.replace_block(10, x+i, y+j, lvl);
                                }
                                else if (i==pencil_width-1&&j==0){
                                        Main.replace_block(8, x+i, y+j, lvl);
                                }
                                else if (i==pencil_width-1&&j==pencil_height-1){
                                        Main.replace_block(9, x+i, y+j, lvl);
                                }
                               
                                else if (i==0|| i==pencil_width-1){
                                        Main.replace_block(11, x+i, y+j, lvl);
                                }
                                else if(j==0||j==pencil_height-1){
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

    public static void addRoad(int x, int y){

    	//addParking(x,y,Main.level2);
            for(int i = 0; i<pl; i++){
                    for(int j = 0; j<pk; j++){
                            Main.replace_block(3, x+i, y+j, Main.level2);
                    }
            }
    }
    
    public static void fillAllParking(){
    	for(int i = 0; i<Main.level_width; i++){
    		for(int j = 0; j<Main.level_height; j++){    			
        		if(containsAll(i, j, 2) == true){
        			addParking(i, j, Main.level2);
        		}       		
        	}
    	}
    }
}

