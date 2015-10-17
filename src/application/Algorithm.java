package application;

import javafx.scene.layout.GridPane;

public class Algorithm {

    static int pl = 0;
    static int pk = 0;

    static int pencil_width = Main.pencil_width;
    static int pencil_height = Main.pencil_height;
    
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
				
				Main.add_block2( uusP  , i, j  );      // lisab uude Main.levelisse samasse kohta samasuguse ploki

			}
		}


		//the initial counters RNG (maybe further ones aswell)
		if(getType(gateX-1, gateY) == 2){
			go(-pl, 0, gateX, gateY, 6);
		}
		else if(getType(gateX, gateY-1) == 2){
			go(0, -pk, gateX, gateY, 6);
		}
		else if(getType(gateX+pl, gateY+pk+1) == 2){
			go(0, pk, gateX, gateY, 6);
		}
		else if(getType(gateX+pl+1, gateY+pk) == 2){
			go(pl, 0, gateX, gateY, 6);
}



///////////////////////////////////////////////////////
// siia kirjutada algoritm, mis teeb vanast uue!! Praegu loeb vana lihtsalt uueks.
///////////////////////////////////////////////////////
///////////////////////////////////////////////////////
// Laimis, kirjuta siia kuskile asju juurde. See juhtub, kui käivitada algoritm
///////////////////////////////////////////////////////

	}
	
    //LAIMIS START
    
    public static int getType(int x, int y){
            return ((Plokk) Main.getNodeFromGridPane(Main.level2, x, y)).nr;
}
   
    public static boolean containsAll(int x, int y, int type){
            for(int i = 0; i<pl; i++){
                    for(int j = 0; j<pk; j++){
                            if(getType(x+i, y+j) != type){
                                    return false;
                            }
                    }
            }
            return true;
}
   
    public static void go(int x, int y, int gateX, int gateY, int counter){
            if(containsAll(gateX+x*2, gateY+y*2, 2) == true && containsAll(gateX+x*3, gateY+y*3, 2) == true
                            && containsAll(gateX+x*4, gateY+y*4, 2) == true){
                    addRoad(gateX+x, gateY+y);
                    go(x, y, gateX+x, gateY+y, counter+1); 
                    if(counter%7 == 0){
                            if(y == 0){
                                    go(0, pk, gateX+x, gateY+y, 1);
                                    go(0, -pk, gateX+x, gateY+y, 1);
                            }
                            else{
                                    go(pl, 0, gateX+x, gateY+y, 1);
                                    go(-pl, 0, gateX+x, gateY+y, 1);
                            }                              
                    }
                    else{
                            int t1 = 0; int t2 = 1;
                            if(y == 0){
                                    t1 = 1; t2 = 0;
                            }
                            for(int i = -1; i < 2; i = i+2){
                                    if(containsAll(gateX+x+(pl*i)*t2, gateY+y+(pk*i)*t1, 2) == true){
                                            addParking(gateX+x+(pl*i)*t2, gateY+y+(pk*i)*t1, Main.level2);
                                            if(containsAll(gateX+x+(pl*2*i)*t2, gateY+y+(pk*2*i)*t1, 2) == true){
                                                    addParking(gateX+x+(pl*2*i)*t2, gateY+y+(pk*2*i)*t1, Main.level2);
                                                    if(containsAll(gateX+x+(pl*3*i)*t2, gateY+y+(pk*3*i)*t1, 2) == true){
                                                            addParking(gateX+x+(pl*3*i)*t2, gateY+y+(pk*3*i)*t1, Main.level2);
                                                    }
                                            }
                                    }      
                            }                      
                    }
            }
            //PARKING SPOTS WHEN REACHING THE WALL
            else{
                    int t1 = 0; int t2 = 1;
                    if(y == 0){
                            t1 = 1; t2 = 0;
                    }
                    for(int i = -3; i < 4; i++){
                            if(containsAll(gateX+(x)+(pl*i)*t2, gateY+(y)+(pk*i)*t1, 2) == true){
                                    addParking(gateX+(x)+(pl*i)*t2, gateY+(y)+(pk*i)*t1, Main.level2);
                                    if(containsAll(gateX+(x*2)+(pl*i)*t2, gateY+(y*2)+(pk*i)*t1, 2) == true){
                                            addParking(gateX+(x*2)+(pl*i)*t2, gateY+(y*2)+(pk*i)*t1, Main.level2);
                                            if(containsAll(gateX+(x*3)+(pl*i)*t2, gateY+(y*3)+(pk*i)*t1, 2) == true){
                                                    addParking(gateX+(x*3)+(pl*i)*t2, gateY+(y*3)+(pk*i)*t1, Main.level2);
                                            }
                                    }
                            }
                    }
            }
}
   
    //LAIMIS END

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
    	addParking(x,y,Main.level2);
//            for(int i = 0; i<pl; i++){
//                    for(int j = 0; j<pk; j++){
//                            Main.replace_block(2, x+i, y+j, Main.level2);
//                    }
//            }
            
            
    }
	
}



// OLD code, copied

// from button listener:

//int gateX = (int) Double.POSITIVE_INFINITY;
//int gateY = (int) Double.POSITIVE_INFINITY;
//pl = pencil_width;
//pk = pencil_height;
//
//for(int i = 0; i<level_width; i++){
//      for(int j = 0; j<level_height; j++){
//             
//             
//              int uusP = ((Plokk) getNodeFromGridPane(level, i, j)).nr; // milline on vana plokk kohas i, j
//              //multiple entries - per 9 piece batches
//             
//              if(uusP == 4){
//                      if(gateX > i){
//                              gateX = i;
//                      }
//                      if(gateY > j){
//                              gateY = j;
//                      }
//              }
//              add_block( uusP  , i, j, level2  );      // lisab uude levelisse samasse kohta samasuguse ploki
//             
//      }
//}
//
//
////the initial counters RNG (maybe further ones aswell)
//if(getType(gateX-1, gateY) == 2){
//go(-pl, 0, gateX, gateY, 6);
//}
//else if(getType(gateX, gateY-1) == 2){
//go(0, -pk, gateX, gateY, 6);
//}
//else if(getType(gateX+pl, gateY+pk+1) == 2){
//go(0, pk, gateX, gateY, 6);
//}
//else if(getType(gateX+pl+1, gateY+pk) == 2){
//go(pl, 0, gateX, gateY, 6);
//}
//
//
//
/////////////////////////////////////////////////////////
//// siia kirjutada algoritm, mis teeb vanast uue!! Praegu loeb vana lihtsalt uueks.
/////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////
//// Laimis, kirjuta siia kuskile asju juurde. See juhtub, kui käivitada algoritm
/////////////////////////////////////////////////////////
//
//


//other methods:

//LAIMIS START

//public int getType(int x, int y){
//        return ((Plokk) getNodeFromGridPane(level2, x, y)).nr;
//}
//
//public boolean containsAll(int x, int y, int type){
//        for(int i = 0; i<pl; i++){
//                for(int j = 0; j<pk; j++){
//                        if(getType(x+i, y+j) != type){
//                                return false;
//                        }
//                }
//        }
//        return true;
//}
//
//public void go(int x, int y, int gateX, int gateY, int counter){
//        if(containsAll(gateX+x*2, gateY+y*2, 2) == true && containsAll(gateX+x*3, gateY+y*3, 2) == true
//                        && containsAll(gateX+x*4, gateY+y*4, 2) == true){
//                addRoad(gateX+x, gateY+y);
//                go(x, y, gateX+x, gateY+y, counter+1); 
//                if(counter%7 == 0){
//                        if(y == 0){
//                                go(0, pk, gateX+x, gateY+y, 1);
//                                go(0, -pk, gateX+x, gateY+y, 1);
//                        }
//                        else{
//                                go(pl, 0, gateX+x, gateY+y, 1);
//                                go(-pl, 0, gateX+x, gateY+y, 1);
//                        }                              
//                }
//                else{
//                        int t1 = 0; int t2 = 1;
//                        if(y == 0){
//                                t1 = 1; t2 = 0;
//                        }
//                        for(int i = -1; i < 2; i = i+2){
//                                if(containsAll(gateX+x+(pl*i)*t2, gateY+y+(pk*i)*t1, 2) == true){
//                                        addParking(gateX+x+(pl*i)*t2, gateY+y+(pk*i)*t1, level2);
//                                        if(containsAll(gateX+x+(pl*2*i)*t2, gateY+y+(pk*2*i)*t1, 2) == true){
//                                                addParking(gateX+x+(pl*2*i)*t2, gateY+y+(pk*2*i)*t1, level2);
//                                                if(containsAll(gateX+x+(pl*3*i)*t2, gateY+y+(pk*3*i)*t1, 2) == true){
//                                                        addParking(gateX+x+(pl*3*i)*t2, gateY+y+(pk*3*i)*t1, level2);
//                                                }
//                                        }
//                                }      
//                        }                      
//                }
//        }
//        //PARKING SPOTS WHEN REACHING THE WALL
//        else{
//                int t1 = 0; int t2 = 1;
//                if(y == 0){
//                        t1 = 1; t2 = 0;
//                }
//                for(int i = -3; i < 4; i++){
//                        if(containsAll(gateX+(x)+(pl*i)*t2, gateY+(y)+(pk*i)*t1, 2) == true){
//                                addParking(gateX+(x)+(pl*i)*t2, gateY+(y)+(pk*i)*t1, level2);
//                                if(containsAll(gateX+(x*2)+(pl*i)*t2, gateY+(y*2)+(pk*i)*t1, 2) == true){
//                                        addParking(gateX+(x*2)+(pl*i)*t2, gateY+(y*2)+(pk*i)*t1, level2);
//                                        if(containsAll(gateX+(x*3)+(pl*i)*t2, gateY+(y*3)+(pk*i)*t1, 2) == true){
//                                                addParking(gateX+(x*3)+(pl*i)*t2, gateY+(y*3)+(pk*i)*t1, level2);
//                                        }
//                                }
//                        }
//                }
//        }
//}
//
////LAIMIS END
//
//public void addParking(int x, int y, GridPane lvl){
//boolean pTäht = false;
//    for(int i = 0; i< pencil_width; i++){
//            for(int j = 0; j< pencil_height; j++){
//                   
//                            if (i==0&&j==0){
//                                    replace_block(7, x+i, y+j, lvl);
//                            }
//                            else if (i==0&&j==pencil_height-1){
//                                    replace_block(10, x+i, y+j, lvl);
//                            }
//                            else if (i==pencil_width-1&&j==0){
//                                    replace_block(8, x+i, y+j, lvl);
//                            }
//                            else if (i==pencil_width-1&&j==pencil_height-1){
//                                    replace_block(9, x+i, y+j, lvl);
//                            }
//                           
//                            else if (i==0|| i==pencil_width-1){
//                                    replace_block(11, x+i, y+j, lvl);
//                            }
//                            else if(j==0||j==pencil_height-1){
//                                    replace_block(12, x+i, y+j, lvl);
//                            }
//                            else if (!pTäht){
//                                    pTäht=true;
//                                    replace_block(1, x+i, y+j, lvl);
//                            }
//                            else{
//                                    replace_block(13, x+i, y+j, lvl);
//                            }
//                           
//            }
//    }
//}
//
//public void addRoad(int x, int y){
//        for(int i = 0; i<pl; i++){
//                for(int j = 0; j<pk; j++){
//                        replace_block(3, x+i, y+j, level2);
//                }
//        }
//}
//	




// ei tea, mis see on

////LAIMIS
//Class ModifiedParkla{
//oldxstart = ...
//oldxend = ...
//oldystart = ...
//oldyend = ...
//
//newxstart = ...
//       newxend = ...
//       newystart = ...
//       newyend = ...
//}
//
//updateObject(modifiedParkla)
//
//updateObject(object){
//modifiedParkla.getOldCubes.SetInvisible;
//modifiedParkla.getNewCubes.SetParkla;
//}
//LAIMIS END

