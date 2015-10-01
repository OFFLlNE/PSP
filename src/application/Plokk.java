package application;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Plokk extends Rectangle {
	




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

