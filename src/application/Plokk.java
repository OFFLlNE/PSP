package application;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Plokk extends Rectangle {
	
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

	
	public static final int _PARKING_P_ = 14;
	public static final int _PARKING_TOP_LEFT_ = 15;
	public static final int _PARKING_TOP_RIGHT_ = 16;
	public static final int _PARKING_BOT_RIGHT_ = 17;
	public static final int _PARKING_BOT_LEFT_ = 18;
	public static final int _PARKING_BORDER_SIDE_ = 19;
	public static final int _PARKING_BORDER_TOPBOT_ = 20;
	public static final int _PARKING_FILLED_BLUE_ = 21;
	
	public static Paint[] textures = new Paint[50];
	
	public int nr;
	public int x;
	public int y;
	static int suurus = 8;
	
	public boolean isEdible = false;
	public boolean isGravity = false;
	public boolean isNPC = false;


	public Paint see;
	
	Plokk(int pos, int x, int y){
		
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
	
	
	
	



	@Override
	public String toString() {
		return "\nPlokk [\nnr: " + nr + "\nx: " + x + "\ny: " + y + "\n]\n";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

