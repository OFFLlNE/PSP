package application;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Block extends Rectangle {
	
	// constants
	public static final int _SOLID_BLACK = 0;
	public static final int _PARKING_P = 1;
	public static final int _NO_BLOCK = 2;
	public static final int _ROBOT_ROAD = 3;
	public static final int _ROBOT_GATEWAY = 4;
	public static final int _CONCRETE = 5;
	public static final int _LEVEL_WALL = 6;
	public static final int _PARKING_TOP_LEFT = 7;
	public static final int _PARKING_TOP_RIGHT = 8;
	public static final int _PARKING_BOT_RIGHT = 9;
	public static final int _PARKING_BOT_LEFT = 10;
	public static final int _PARKING_BORDER_SIDE = 11;
	public static final int _PARKING_BORDER_TOPBOT = 12;
	public static final int _PARKING_FILLED_BLUE = 13;

	
	public static final int _CAR_TOP_LEFT = 14;
	public static final int _CAR_TOP_RIGHT = 15;
	public static final int _CAR_BOT_RIGHT = 16;
	public static final int _CAR_BOT_LEFT = 17;
	public static final int _CAR_BORDER_TOP_1 = 18;
	public static final int _CAR_BORDER_TOP_2 = 19;
	public static final int _CAR_BORDER_TOP_3 = 20;

	public static final int _CAR_BORDER_LEFT = 21;
	public static final int _CAR_SOLID_BLACK = 22;
	public static final int _CAR_BORDER_RIGHT = 23;
	public static final int _CAR_BORDER_BOT_1 = 24;
	public static final int _CAR_BORDER_BOT_2 = 25;
	public static final int _CAR_BORDER_BOT_3 = 26;
	
	public static Paint[] textures = new Paint[50];
	
	public int nr;
	public int x;
	public int y;
	static int suurus = 8;
	
	public boolean isEdible = false;
	public boolean isGravity = false;
	public boolean isNPC = false;


	public Paint see;
	
	Block(int pos, int x, int y){
		
		nr = pos;
		this.setX(0);
		this.setY(0);
		this.setWidth(suurus);
		this.setHeight(suurus);
		this.x = x;
		this.y = y;
		this.setFill(textures[pos]);
		this.see = textures[pos];

		
	}
	
	
	public void muuda(int pos){
		if (this.nr != 6){
		this.nr = pos;
		
		this.setFill(textures[pos]);
		this.see = textures[pos];
		
		this.x = GridPane.getColumnIndex(this);
		this.y = GridPane.getRowIndex(this);

		}

		
	}
	
	
	public static boolean isMovable(int n){
		if (
				n ==  _PARKING_P||	
				n ==  _NO_BLOCK||	
				n ==  _ROBOT_ROAD||	
				n ==  _ROBOT_GATEWAY||	
				n ==  _PARKING_TOP_LEFT||
				n ==  _PARKING_TOP_RIGHT||
				n ==  _PARKING_BOT_RIGHT||
				n ==  _PARKING_BOT_LEFT||
				n ==  _PARKING_BORDER_SIDE||
				n ==  _PARKING_BORDER_TOPBOT||
				n ==  _PARKING_FILLED_BLUE
				){
			return true;
		}
		else{
			return false;
		}
	}
	
	
	public static boolean isMovable(int x, int y){
        for(int i = 0; i<Main.pencil_width; i++){
            for(int j = 0; j<Main.pencil_height; j++){
            	if(x+i < 0 || x+i > Main.level_width || y+j > Main.level_height || y+j < 0){
            		return false;
            	}
                if(!isMovable( Main.getType(x+i, y+j) )){
                    return false;
                }
            }
        }
        return true;
    }
	
	public static boolean isInvisible(int x, int y){
        for(int i = 0; i<Main.pencil_width; i++){
            for(int j = 0; j<Main.pencil_height; j++){
            	if(x+i < 0 || x+i > Main.level_width || y+j > Main.level_height || y+j < 0){
            		return false;
            	}
                if(!isInvisible( Main.getType(x+i, y+j) )){
                    return false;
                }
            }
        }
        return true;
    }
	

	private static boolean isInvisible(int n){
		if (
				n ==  _NO_BLOCK	
				){
			return true;
		}
		else{
			return false;
		}
	}
	
	public static boolean isParking(int n){
		//empty car slot
		if (
				n ==  _PARKING_P||	
				n ==  _PARKING_TOP_LEFT||
				n ==  _PARKING_TOP_RIGHT||
				n ==  _PARKING_BOT_RIGHT||
				n ==  _PARKING_BOT_LEFT||
				n ==  _PARKING_BORDER_SIDE||
				n ==  _PARKING_BORDER_TOPBOT||
				n ==  _PARKING_FILLED_BLUE
				
				){
			return true;
		}
		else{
			return false;
		}
	}
	


	@Override
	public String toString() {
		return "\nPlokk [\nnr: " + nr + "\nx: " + x + "\ny: " + y + "\n]\n";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

