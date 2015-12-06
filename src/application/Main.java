package application;
       
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.FileInputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
 
class BlockButton extends ToggleButton{
        public int i;
}


public class Main extends Application {
	static String[] arguments;
	// default values
	static int level_width = 240;
	static int level_height = 100;
	//static int level_width = 60;
	//static int level_height = 50;

	static int pencil_width = 5;
	static int pencil_height = 3;

	static SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss dd.MM.YYYY");

	//

	final Stage stage2 = new Stage();
	final Stage stage3 = new Stage();

	//LAIMIS
	static boolean started = false;
	static Block ui = null;
	//LAIMIS END

	public static Deque<Integer> l2 = new ArrayDeque<Integer>();
	
	static int currentTool = 0;
	static GridPane level = new GridPane();
	static GridPane level2 = new GridPane();
	final Label label_mouse_cordinates = new Label();
	static Label silt5 = new Label();

	final static Label label_newUI_parkingSlots = new Label();
	final static Label label_newUI_robotSlots = new Label();
	

    final static NumberTextField field_zoom = new NumberTextField(1,50);
    final static NumberTextField field_image_width = new NumberTextField();
    final static NumberTextField field_image_height = new NumberTextField();
    final static TextField field_image_file = new TextField();
    final static NumberTextField field_pencil_width = new NumberTextField(1,10);
    final static NumberTextField field_pencil_height = new NumberTextField(1,10);
    final static Button button_refresh_image = new Button("Apply image");
    final static Button button_pencil_size_apply = new Button("Yes!");
    final static Label label_newUI_image = new Label("");
    final static Label label_newUI_dat = new Label("");
    final static Label simUI_hour_indicator = new Label("");
    
	static Algorithm algorithm_thread = new Algorithm();
    
    static String last_path = "";
    
	static int number_of_parking_slots_1 = 0;
	static int number_of_parking_slots_2 = 0;

	static int mouseX;
	static int mouseY;

	static int image_width = level_width*Block.suurus;
	static int image_height = level_height*Block.suurus;

	static int vanaSuurus = Block.suurus;

    static Group uus = new Group();

	static Block[][] blocks = new Block[level_width][level_height];
	static Block[][] blocks2 = new Block[level_width][level_height];
	
	
	
	static ArrayList<Integer> legal_blocks = new ArrayList<Integer>();

	BlockButton button_tool_place;
	BlockButton button_tool_fill;
	BlockButton button_tool_laimis;
	BlockButton button_tool_custom_brush_size;

	static ArrayList<String> files = new ArrayList<String>();
	static Simulation sim = new Simulation();

	class MouseEvent_Handler implements EventHandler<MouseEvent> {
		Block a;

		public MouseEvent_Handler(Block a) {
			this.a = a;
		}

		public void handle(MouseEvent me) {
			if (me.getEventType()==MouseEvent.MOUSE_ENTERED){
				if (button_tool_fill.isSelected() && a.nr!=6){
					try{
					mark_neighbours(a, Color.AQUA, a.nr, false, 0, level);
				}
				catch(StackOverflowError st){
					System.out.println(st + " You might want add VM argument -Xss1500m");
					to_mark.clear();
				}
				}
				if (a.nr != 6){
					a.setFill(Color.AQUAMARINE);
				}
				//märgiTeatud(a, Color.AQUA, a.nr, 4);

				mouseX = a.x;
				mouseY = a.y;

				label_mouse_cordinates.setText("(" + a.x + ", " + a.y + ")");


				if (button_tool_custom_brush_size.isSelected()){
					for(int i = 0; i< pencil_width; i++){
						for(int j = 0; j< pencil_height; j++){
							
							if( !(a.x+i >= level_width)  && !(a.y+j>= level_height) ){
								((Block)getNodeFromGridPane(level, a.x+i, a.y+j)).setFill(Color.AQUA);
							
							}
						}
					}
				}

				if (button_tool_laimis.isSelected()){
					if (started == true && a.nr != 6){
						int x_side;
						int y_side;
						if (a.x>ui.x){
							x_side = -1;
						}
						else{
							x_side=1;
						}

						if (a.y>ui.y){
							y_side = -1;
						}
						else{
							y_side=1;
						}
						for(int i = 0; i< Math.abs(ui.x - a.x)+1; i++){
							for(int j = 0; j< Math.abs(ui.y - a.y)+1; j++){
								((Block)getNodeFromGridPane(level, a.x+ (i*x_side) , a.y+  (j*y_side ) )).setFill(Color.AQUA);
							}
						}
					}
				}   
			}

			else if (me.getEventType()==MouseEvent.MOUSE_CLICKED){
				if (button_tool_fill.isSelected()){
					if(!(a.nr==currentTool)){
						try{
						mark_neighbours(a, Color.AQUA, a.nr, true, currentTool, level);
						}
						catch(StackOverflowError st){
							System.out.println(st + " You might want add VM argument -Xss1500m");
							to_mark.clear();
						}
					}
			
				}

				//if (a.x != 0 & a.y != 0 & a.x != 59 & a.y != 23 ) {
				replace_block(currentTool, a.x, a.y, level);
				//      };


				if (button_tool_laimis.isSelected()){     
					if (started == false){
						started = true;
						ui = a;
					}
					else{
						started = false;
						int x_side;
						int y_side;
						if (a.x>ui.x){
							x_side = -1;
						}
						else{
							x_side=1;
						}
						if (a.y>ui.y){
							y_side = -1;
						}
						else{
							y_side=1;
						}
						for(int i = 0; i< Math.abs(ui.x - a.x)+1; i++){
							for(int j = 0; j< Math.abs(ui.y - a.y)+1; j++){
								replace_block(currentTool, a.x+ (i*x_side)   , a.y+ (j*y_side)    , level);
							}
						}
					}

				}      

				if (button_tool_custom_brush_size.isSelected()&&a.nr != 6){

					boolean pTäht = false;

					for(int i = 0; i< pencil_width; i++){
						for(int j = 0; j< pencil_height; j++){

							if (currentTool == Block._PARKING_P){
								if (i==0&&j==0){

									if(pencil_width < 3 || pencil_height < 3){
										replace_block(Block._PARKING_P, a.x, a.y, level);
									}
									else{
										replace_block(Block._PARKING_TOP_LEFT, a.x+i, a.y+j, level);
									}
								}
								else if (i==0&&j==pencil_height-1){
									replace_block(Block._PARKING_BOT_LEFT, a.x+i, a.y+j, level);
								}
								else if (i==pencil_width-1&&j==0){
									replace_block(Block._PARKING_TOP_RIGHT, a.x+i, a.y+j, level);
								}
								else if (i==pencil_width-1&&j==pencil_height-1){
									replace_block(Block._PARKING_BOT_RIGHT, a.x+i, a.y+j, level);
								}

								else if (i==0|| i==pencil_width-1){
									replace_block(Block._PARKING_BORDER_SIDE, a.x+i, a.y+j, level);
								}
								else if(j==0||j==pencil_height-1){
									replace_block(Block._PARKING_BORDER_TOPBOT, a.x+i, a.y+j, level);
								}
								else if (!pTäht){
									pTäht=true;
									replace_block(Block._PARKING_P, a.x+i, a.y+j, level);
									
								}
								else{
									replace_block(Block._PARKING_FILLED_BLUE, a.x+i, a.y+j, level);
								}
							}
							else{
								replace_block(currentTool, a.x+i, a.y+j, level);
								//Parkla weJustGeneratedThisRectangle = new Parkla(xalgus, xlopp, yalgus, ylopp)
							}
						}
					}

				}      
			}

			//MOUSE_EXITED
			else {
				mouseX = a.x;
				mouseY = a.y;      

				if (button_tool_fill.isSelected() && a.nr != 6){
					try{
					mark_neighbours(a, a.see, a.nr, false, 0, level);
				}
				catch(StackOverflowError st){
					System.out.println(st + " You might want add VM argument -Xss1500m");
					to_mark.clear();
				}
				}

				if (a.nr != 6){
					a.setFill(a.see);
				}

				if (button_tool_custom_brush_size.isSelected()){
					for(int i = 0; i< pencil_width; i++){
						for(int j = 0; j< pencil_height; j++){
							if(!(a.x+i > level_width) || !(a.y+j> level_height)){
								((Block)getNodeFromGridPane(level, a.x+i, a.y+j)).setFill(((Block)getNodeFromGridPane(level, a.x+i, a.y+j)).see);
							}
						}
					}
				}

				if (button_tool_laimis.isSelected()){
					if (started == true && a.nr != 6){
						int x_side;
						int y_side;
						if (a.x>ui.x){
							x_side = -1;
						}
						else{
							x_side=1;
						}
						if (a.y>ui.y){
							y_side = -1;
						}
						else{
							y_side=1;
						}
						for(int i = 0; i< Math.abs(ui.x - a.x)+1; i++){
							for(int j = 0; j< Math.abs(ui.y - a.y)+1; j++){
								((Block)getNodeFromGridPane(level, a.x+ (i*x_side) , a.y+  (j*y_side ) )).setFill(((Block)getNodeFromGridPane(level, a.x+(i*x_side), a.y+(j*y_side ))).see);
							}
						}
					}
				}   
			}
		}      
	}


	class MouseEvent_Drag_Handler implements EventHandler<MouseEvent> {
		GridPane a;

		public MouseEvent_Drag_Handler(GridPane a) {
			this.a = a;
		}

		public void handle(MouseEvent me) {
			if (me.getEventType()==MouseEvent.MOUSE_DRAGGED){

				//if ((int)me.getX()/Plokk.suurus != 0 & (int)me.getY()/Plokk.suurus != 0 & (int)me.getX()/Plokk.suurus != 59 & (int)me.getY()/Plokk.suurus != 23 ) {

				//((Plokk) getNodeFromGridPane(level, (int)me.getX()/Plokk.suurus, (int)me.getY()/Plokk.suurus)  ).muuda(currentTool);
				if (button_tool_place.isSelected()){
					replace_block(currentTool, (int)me.getX()/Block.suurus, (int)me.getY()/Block.suurus, level);
				}
				//};

				label_mouse_cordinates.setText("(" + (int)me.getX()/Block.suurus + ", " + (int)me.getY()/Block.suurus + ")");
			}
		}      
	}
	


	public static void colorParking(int x, int y, GridPane lvl){
		boolean pTäht = false;

		for(int i = 0; i< pencil_width; i++){
			for(int j = 0; j< pencil_height; j++){

					if (i==0&&j==0){
						
						if(pencil_width < 3 || pencil_height < 3){
							replace_block(Block._PARKING_P, x, y, lvl);
						}
						else{
							replace_block(Block._PARKING_TOP_LEFT, x+i, y+j, lvl);
						}
						
						//replace_block(Block._PARKING_TOP_LEFT, x+i, y+j, lvl);
					}
					else if (i==0&&j==pencil_height-1){
						replace_block(Block._PARKING_BOT_LEFT, x+i, y+j, lvl);
					}
					else if (i==pencil_width-1&&j==0){
						replace_block(Block._PARKING_TOP_RIGHT, x+i, y+j, lvl);
					}
					else if (i==pencil_width-1&&j==pencil_height-1){
						replace_block(Block._PARKING_BOT_RIGHT, x+i, y+j, lvl);
					}

					else if (i==0|| i==pencil_width-1){
						replace_block(Block._PARKING_BORDER_SIDE, x+i, y+j, lvl);
					}
					else if(j==0||j==pencil_height-1){
						replace_block(Block._PARKING_BORDER_TOPBOT, x+i, y+j, lvl);
					}
					else if (!pTäht){
						pTäht=true;
						replace_block(Block._PARKING_P, x+i, y+j, lvl);
					}
					else{
						replace_block(Block._PARKING_FILLED_BLUE, x+i, y+j, lvl);
					}


			}
		}
	}
    
    public static void colorOccupied(int x, int y, GridPane lvl){
        for(int i = 0; i< pencil_width; i++){
            for(int j = 0; j< pencil_height; j++){                   
                if (i==0&&j==0){
                        replace_block(14, x+i, y+j, lvl);
                }
                else if (i==0&&j==pencil_height-1){
                        replace_block(17, x+i, y+j, lvl);
                }
                else if (i==pencil_width-1&&j==0){
                        replace_block(15, x+i, y+j, lvl);
                }
                else if (i==pencil_width-1&&j==pencil_height-1){
                        replace_block(16, x+i, y+j, lvl);
                }               
                else if (i==1 && j == 0){
                        replace_block(18, x+i, y+j, lvl);
                }
                else if (i==2 && j == 0){
                    replace_block(19, x+i, y+j, lvl);
                }
                else if (i==3 && j == 0){
                    replace_block(20, x+i, y+j, lvl);
                }
                else if (i==0 && j == 1){
                    replace_block(21, x+i, y+j, lvl);
                }
                else if (i==pencil_width-1 && j == 1){
                    replace_block(23, x+i, y+j, lvl);

                }
                else if (i==1 && j == pencil_height-1){
                    replace_block(24, x+i, y+j, lvl);
                }
                else if (i==2 && j == pencil_height-1){
                    replace_block(25, x+i, y+j, lvl);
                }
                else if (i==3 && j == pencil_height-1){
                    replace_block(26, x+i, y+j, lvl);
                }
                else{
                    replace_block(22, x+i, y+j, lvl);
                }
            }
        }
    }
    

	
    public static int getType(int x, int y){
    	return ((Block) Main.getNodeFromGridPane(Main.level2, x, y)).nr;
    }
    // FILL ALGORITHM
    
    public static Deque<Block> to_mark = new ArrayDeque<Block>();
    
    public static void mark_neighbours(Block a, Paint b, int eelmine, boolean kasAsendada, int tool, GridPane lvl) throws StackOverflowError{
    	if (a==null){
    		return;
    	}
    	
    	Block W = neighbourW(a.x, a.y, lvl);
    	Block S = neighbourS(a.x, a.y, lvl);
    	Block E = neighbourE(a.x, a.y, lvl);
    	Block N = neighbourN(a.x, a.y, lvl);

    	if (kasAsendada){
    		replace_block(tool, a.x, a.y, lvl);
    	}
    	else{
    		a.setFill(b);
    	}

    	if (!kasAsendada){
    		if (W != null && !W.getFill().equals(b) && W.nr == eelmine     ) { to_mark.push(W);};
    		if (S != null && !S.getFill().equals(b) && S.nr == eelmine    ) {         to_mark.push(S);};
    		if (E != null && !E.getFill().equals(b) && E.nr == eelmine    ) {         to_mark.push(E);};
    		if (N != null && !N.getFill().equals(b) && N.nr == eelmine   ) {         to_mark.push(N);};
    	}
    	else{
    		//asendamine
    		if (W!=null && W.nr == eelmine  ) { to_mark.push(W);};
    		if (S!=null && S.nr == eelmine   ) {         to_mark.push(S);};
    		if (E!=null && E.nr == eelmine  ) {          to_mark.push(E);};
    		if (N!=null && N.nr == eelmine    ) {           to_mark.push(N);};
    	}

    	//System.out.println(to_mark.getLast().nr);
    	try{
    		mark_neighbours(to_mark.pop(), b, eelmine, kasAsendada, tool, lvl);
    	}
    	catch(Exception e){
    		if(e instanceof java.util.NoSuchElementException){
    			return;
    		}
    		to_mark.clear();
    		return;
    	}

    }

   
   
   
    public static Block neighbourS(int x, int y, GridPane lvl){
    	if (x>1 && x<level_width && y > 0 && y<Main.level_height){
    		return (Block)getNodeFromGridPane(lvl, x, y+1);
    	}
    	else{
    		return null;
    	}
    }
   
    public static Block neighbourW(int x, int y, GridPane lvl){
    	if (x>1 && x<level_width && y > 0 && y<Main.level_height){
        return (Block)getNodeFromGridPane(lvl, x+1, y);
    	}
    	else{
    		return null;
    	}
       
    }
   
   
    public static Block neighbourE(int x, int y, GridPane lvl){
    	if (x>1 && x<level_width && y > 0 && y<Main.level_height){
        return (Block)getNodeFromGridPane(lvl, x-1, y);
    	}
    	else{
    		return null;
    	}
       
    }
   
    public static Block neighbourN(int x, int y, GridPane lvl){
    	if (x>1 && x<level_width && y > 0 && y<Main.level_height){
        return (Block)getNodeFromGridPane(lvl, x, y-1);
    	}
    	else{
    		return null;
    	}
       
    }
   
 
   
    public static Block getNodeFromGridPane(GridPane gridPane, int col, int row) {
    	if (gridPane==level){
    		return blocks[col][row];
    	}else{
    		 if(col < 0 || row < 0 || col >= level_width || row >= level_height){
    			 System.out.println("out of bounds: "+ col + ", " + row);
    			 return new Block(Block._CONCRETE,col,row);
    		 }
    		 else {
                 return blocks2[col][row]; 
    		 }
    	}
       
    }
   
 
    public static void loadLevel_IO(File fail) throws  FileNotFoundException, IOException, ClassNotFoundException, WrongFileException {
    	silt5.setText("Laen...");
    	RandomAccessFile r = new RandomAccessFile(fail, "r");



    	for(int i = 1; i<=level_height; i++){
    		for(int j = 1; j<=level_width; j++){

    			int a = r.read();
    			if (legal_blocks.contains(a)){
    				//System.out.println("("+ i + ", " + j + ") - " + a);
    				l2.add(a);
    			}
    			else{
    				//System.out.println("Encountered illegal tile value in file.");
    				r.close();
    				throw new WrongFileException();
    			}
    		}
    		//System.out.println();
    	}

    	field_zoom.setText(r.readInt() +"");
    	field_image_width.setText(r.readInt() +"");
    	field_image_height.setText(r.readInt() +"");
    	field_pencil_width.setText(r.readInt() +"");
    	field_pencil_height.setText(r.readInt() +"");
    	
    	field_image_file.setText("");
    	try{
    		while(true){
    			char imagePathPiece = r.readChar();
    			field_image_file.setText(field_image_file.getText() + imagePathPiece ); 
    		}
    	}
    	catch(Exception e){
    		if (e instanceof java.io.EOFException){
    			// This is normal. This is needed for program to stop trying to read file after it has ended.
    		}
    		else{
    			System.out.println(e+" while determining image path");
    		}
    	}
    	r.close();

    	silt5.setText("Laetud! - " + df.format(System.currentTimeMillis() )  );
    }

       
        public void loadLevel(File fail){
			level.getChildren().removeAll(level.getChildren());
			level2.getChildren().removeAll(level2.getChildren());
			number_of_parking_slots_1 = 0;
			number_of_parking_slots_2 = 0;

			// read saved file into queue l2
			try {
				loadLevel_IO( fail  );
				//button_refresh_image.fire();
			}
			// file not found likely, fill queue with generic level instead
			catch (Exception erind) {
				
				//System.out.println("faili pole, teen uue");

				if (erind instanceof FileNotFoundException){
					System.out.println(erind + " While loading level. Making blank unsaved file.");
					silt5.setText("Blank unsaved file. - " + df.format(System.currentTimeMillis() )  );
					makeGenericLevel();
					
					//System.out.println(erind);
				}
				else if (erind instanceof WrongFileException){
					System.out.println(erind + " While loading level.");
					silt5.setText("Not correctly structured file - " + df.format(System.currentTimeMillis() )  );
					makeGenericLevel();
				}
				else if (erind instanceof java.io.EOFException){
					System.out.println(erind + " File ends after level info.");
					silt5.setText("Incomplete file. Likely made with older version. - " + df.format(System.currentTimeMillis() )  );
				}
				else{
					System.out.println(erind + " While loading level.");
					silt5.setText("While opening file: "+erind+ " - " + df.format(System.currentTimeMillis() )  );
					//makeGenericLevel();
				}
				
			}
			// draw level from queue
			finally {
				for(int i = 0; i<level_height; i++){
					for(int j = 0; j<level_width; j++){
						add_block(   l2.pop(), j, i, level  );         
					}
				}

				last_path = fail.toString();
				button_refresh_image.fire();
				button_pencil_size_apply.fire();

			    label_newUI_dat.setText(last_path);

			}
        }
        
       
        public static void saveLevel(File fail) throws  FileNotFoundException, IOException, ClassNotFoundException {
                silt5.setText("Salvestan...");
                RandomAccessFile r = new RandomAccessFile(fail, "rw");
                
                System.out.println("salvestan");
                
                for(int i = 0; i<=level_height-1; i++){
                        for(int j = 0; j<=level_width-1; j++){
                        	//System.out.println("("+ i + ", " + j + ") - " + a);
                        	//System.out.println((Plokk) getNodeFromGridPane(level, j, i));
                        	r.write(         ((Block) getNodeFromGridPane(level, j, i)).nr              );
                        }
                        //System.out.println();
                }
                

				validate_input();
                r.writeInt(Integer.parseInt(field_zoom.getText()));
                
                r.writeInt(Integer.parseInt(field_image_width.getText()));
                r.writeInt(Integer.parseInt(field_image_height.getText()));
                
                r.writeInt(Integer.parseInt(field_pencil_width.getText()));
                r.writeInt(Integer.parseInt(field_pencil_height.getText()));
                
                r.writeChars(field_image_file.getText());
                
                r.setLength(r.getFilePointer());
                
                r.close();
                
                silt5.setText("Salvestatud! - " + df.format(System.currentTimeMillis() )  );
        }
       

        // to avoid making another listener. Called every time info is used.
        public static void validate_input(){
        	int newPw = Integer.parseInt(field_pencil_width.getText());
        	
			if(newPw > field_pencil_width.range_max){
				field_pencil_width.setText(field_pencil_width.range_max+"");
			}
			else if(newPw < field_pencil_width.range_min){
				field_pencil_width.setText(field_pencil_width.range_min+"");            					
			}        	
			
			int newPy = Integer.parseInt(field_pencil_height.getText());
        	
			if(newPy > field_pencil_height.range_max){
				field_pencil_height.setText(field_pencil_height.range_max+"");
			}
			else if(newPy < field_pencil_height.range_min){
				field_pencil_height.setText(field_pencil_height.range_min+"");            					
			}
			
        	
        	int newZ = Integer.parseInt(field_zoom.getText());
        	
			if(newZ > field_zoom.range_max){
				field_zoom.setText(field_zoom.range_max+"");
			}
			else if(newZ < field_zoom.range_min){
				field_zoom.setText(field_zoom.range_min+"");            					
			}
			
        }
       
        public void add_block(int nr, int j, int i, GridPane lvl){
                Block r = new Block(   nr, j, i  );
               
                if (lvl == level ){
                        r.addEventHandler(MouseEvent.MOUSE_ENTERED, new MouseEvent_Handler(r));
                        r.addEventHandler(MouseEvent.MOUSE_CLICKED, new MouseEvent_Handler(r));
                        r.addEventHandler(MouseEvent.MOUSE_EXITED, new MouseEvent_Handler(r));
                }
                //if (j == 0|| i == 0 || j == 59 || i == 23 ) {r.mouseTransparentProperty().set(true);;};
 
                lvl.add(r, j, i);
                if (lvl == level){
                	blocks[j][i] = r;
                }
                else if(lvl == level2){
                	blocks2[j][i] = r;
                }
//              Label rl = new Label(r.nr + "");
//              rl.setMouseTransparent(true);
//              level.add(rl, j, i);
               
                if (nr == 1){
                    if (lvl == level){
                        number_of_parking_slots_1++;
                    }else{
                        number_of_parking_slots_2++;
                    }
                    updateParkingLabels();
                }
               
        }
       
        public static void add_block2(int nr, int j, int i){
            Block r = new Block(   nr, j, i  );
           
            level2.add(r, j, i);
            blocks2[j][i] = r;
           
            if (nr == 1){
                number_of_parking_slots_2++;
                updateParkingLabels();
            }
           
    }       
        
      
        public static void replace_block(int nr, int j, int i, GridPane lvl){
                Block muudetav = (Block) getNodeFromGridPane(lvl, j, i);
                
                
                if (nr == 1){
                        if (lvl == level){
                                number_of_parking_slots_1++;
                        }else{
                                number_of_parking_slots_2++;
                        }
                }
               
                if (muudetav.nr == 1){
                        if (lvl == level){
                                number_of_parking_slots_1--;
                        }else{
                                number_of_parking_slots_2--;
                }
                }
               
                if (muudetav.nr != 6){
                       
                        muudetav.muuda(nr);
               
                }
                if(lvl==level){
                	updateParkingLabels();
                }
                //number_of_parking_slots_Silt.setText("Parkimiskohti joonistusel: " + number_of_parking_slots_1 + ", parkimiskohti robotparklas: " + number_of_parking_slots_2 + "");
               
        }
       
       public static Bot makeBot(int x, int y){
           final Bot rect1 = new Bot(x*Block.suurus,y*Block.suurus,Block.suurus*Main.pencil_width,Block.suurus*Main.pencil_height);

           rect1.setFill(Block.textures[49]);

           Platform.runLater(new Runnable() {                          
               @Override
               public void run() {
                       Main.uus.getChildren().add(rect1);
               }
           });
           return rect1;

       }

        public void readTextures() throws  FileNotFoundException{
                //System.out.println("reading textures...");
                Block.textures[0] = Color.BLACK;
               
                for (int i = 1; i<27; i++){
                		legal_blocks.add(i);
                        Block.textures[i] = new ImagePattern(new Image(new FileInputStream(new File("textures/" + i + ".png"))));
                }
                Block.textures[48] = new ImagePattern(new Image(new FileInputStream(new File("textures/" + "r2.png")))); 
                Block.textures[49] = new ImagePattern(new Image(new FileInputStream(new File("textures/" + "r.png")))); 
                //System.out.println("Done!");
        }
       
       
        public static void updateParkingLabels(){

            label_newUI_parkingSlots.setText("Number of regular parking slots: " + number_of_parking_slots_1);
            label_newUI_robotSlots.setText("Number of robot parking slots: " + number_of_parking_slots_2 );
        }
        public static void updateHourLabel(){
        	Platform.runLater(new Runnable() {
        		  @Override public void run() {
        	        simUI_hour_indicator.setText((Integer.parseInt((simUI_hour_indicator.getText().split(":"))[0])+1)+":"+0);
        		  }
        		});
			
        }
        public static void updateMinuteLabel(final int minutes){
        	Platform.runLater(new Runnable() {
        		  @Override public void run() {
        	        simUI_hour_indicator.setText((simUI_hour_indicator.getText().split(":"))[0]+":"+(Integer.parseInt((simUI_hour_indicator.getText().split(":"))[1])+minutes));
        		  }
        		});
			
        }       
        public static void makeGenericLevel(){       
        	for(int i = 1; i<=level_height; i++){
        		for(int j = 1; j<=level_width; j++){
        			if (i == 1 || i==level_height || j==1 || j==level_width){
        				l2.add(6);
        			}
        			else{
        				l2.add(2);
        			}

        		}
        		//System.out.println();

        	}
        }
        
        // delete all objects. redraw them
        public void reset(){

			number_of_parking_slots_1 = 0;
			number_of_parking_slots_2 = 0;
			for(int i = 0; i<=level_height-1; i++){
				for(int j = 0; j<=level_width-1; j++){
					l2.add(((Block) getNodeFromGridPane(level, j, i)).nr);
				}
			} 
			level.getChildren().removeAll(level.getChildren());
			for(int i = 0; i<level_height; i++){
				for(int j = 0; j<level_width; j++){
					add_block(   l2.pop(), j, i, level  );         
				}
			}
			
			if(!level2.getChildren().isEmpty()){
				for(int i = 0; i<=level_height-1; i++){
					for(int j = 0; j<=level_width-1; j++){
						l2.add(((Block) getNodeFromGridPane(level2, j, i)).nr);
					}
				} 
				level2.getChildren().removeAll(level2.getChildren());
				for(int i = 0; i<level_height; i++){
					for(int j = 0; j<level_width; j++){
						add_block(   l2.pop(), j, i, level2  );         
					}
				}
			}
        }
        
        @Override
        public void start(final Stage primaryStage) throws  FileNotFoundException, IOException, ClassNotFoundException {
               
            readTextures();
             
            GridPane root = new GridPane();
            final Scene scene = new Scene(root,1600,900);
            primaryStage.setTitle("PitStop Parking");
            primaryStage.setScene(scene);
            
//            // vv OLD UI
//            final TextField väli_selected_block_tool = new TextField();
//            
//            GridPane oldUI_block_toolbar_gridpane = new GridPane();
//
//            ToggleGroup BlockButtons = new ToggleGroup();
//
//            // block selection toolbar for old UI
//            for (int i = 0; i<14; i++){
//            	if (i < 41 ){
//            		final BlockButton temp = new BlockButton();
//            		temp.i = i;
//            		//oldUI_block_toolbar_gridpane.getChildren().add(temp);
//            		//StackPane.setMargin(temp, new Insets(0, 0, 0, 0));
//            		oldUI_block_toolbar_gridpane.add(temp, i, 0);
//            		Rectangle temp2 = new Rectangle();
//
//            		temp2.setX(0);
//            		temp2.setY(0);
//            		temp2.setWidth(16 );
//            		temp2.setHeight(16           				);
//            		temp2.setFill(Block.textures[i]);
//            		temp.setGraphic(temp2);
//            		temp.setToggleGroup(BlockButtons);
//            		temp.setOnAction(
//            				new EventHandler<ActionEvent>() {
//            					@Override public void handle(ActionEvent e) {
//            						currentTool = temp.i;
//            						väli_selected_block_tool.setText( temp.i + "" );
//            					}      
//            				}
//            				);
//            		if (i==0){
//            			temp.fire();
//            		}
//            		//Label rl = new Label(i + "");
//            		//rl.setMouseTransparent(true);
//            		//oldUI_block_toolbar_gridpane.add(rl, i, 0);
//
//            	}
//
//            }
//            
//            
//            oldUI_block_toolbar_gridpane.add(väli_selected_block_tool, 20, 0);
            
            // ^^ OLD UI
             

            Label label_block_size = new Label("Zoom: ");
            Button button_block_size_apply = new Button("Set");
           
            GridPane gridPane_infolabels = new GridPane();
            
            ToggleGroup tools = new ToggleGroup();

            button_tool_place = new BlockButton();
            button_tool_fill = new BlockButton();
            button_tool_laimis = new BlockButton();
            button_tool_custom_brush_size = new BlockButton();
            button_tool_place.setText("Place");
            button_tool_fill.setText("Fill");
            button_tool_laimis.setText("Square");
            button_tool_custom_brush_size.setText(pencil_width + "x" + pencil_height);
            button_tool_place.setToggleGroup(tools);
            button_tool_fill.setToggleGroup(tools);
            button_tool_laimis.setToggleGroup(tools);
            button_tool_custom_brush_size.setToggleGroup(tools);
            button_tool_place.fire();
            
            GridPane image_resize_dialog_gridpane = new GridPane();

            Label label_bgimage_x = new Label("Width: ");
            Label label_bgimage_y = new Label("Height: ");
            Label pildifailSilt = new Label("Filename: ");
            final Button button_reset_image_size = new Button("Reset size");
               
            image_resize_dialog_gridpane.add(label_bgimage_x, 1, 0);
            image_resize_dialog_gridpane.add(field_image_width, 2, 0);
            image_resize_dialog_gridpane.add(label_bgimage_y, 3, 0);
            image_resize_dialog_gridpane.add(field_image_height, 4, 0);
            image_resize_dialog_gridpane.add(pildifailSilt, 5, 0);
            image_resize_dialog_gridpane.add(field_image_file, 6, 0);
            image_resize_dialog_gridpane.add(button_reset_image_size, 10, 0);
            image_resize_dialog_gridpane.add(button_refresh_image, 11, 0);
           
            field_image_width.setText(image_width+"");
            field_image_height.setText(image_height+"");
            

 
            final Canvas canvas = new Canvas(level_width*Block.suurus,level_height*Block.suurus);
            
            final GraphicsContext gc = canvas.getGraphicsContext2D();

            field_zoom.setText(Block.suurus + "");
 


            GridPane modes = new GridPane();
            modes.add(button_tool_place, 16, 0);
            modes.add(button_tool_fill, 17, 0);
            modes.add(button_tool_laimis, 18, 0);
            modes.add(button_tool_custom_brush_size, 19, 0);
 
            // file operation dialogs
            final FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("DAT", "*.dat")
                );
            fileChooser.setTitle("Point to .dat file");
            fileChooser.setInitialDirectory(new File("levels/"));

            final FileChooser imageChooser = new FileChooser();
            imageChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All Images", "*.*"),
                    new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                    new FileChooser.ExtensionFilter("PNG", "*.png")
                );
            imageChooser.setTitle("Point to image");
            
            
            // new UI

            GridPane newUI = new GridPane();
            
            final Button button_newUI_choose_image = new Button("Choose image");

            
            final Button button_newUI_choose_dat = new Button("Choose .dat file");
            newUI.add(button_newUI_choose_dat,0,1);
            button_newUI_choose_dat.setOnAction(
                    new EventHandler<ActionEvent>() {
                        @Override public void handle(ActionEvent e) {
                        	

                            File dat_file = fileChooser.showOpenDialog(primaryStage);
                        	if (dat_file!=null){
                        		loadLevel( dat_file  );
                        	}
            				
                        }      
                }
            );
            final Button button_newUI_resize_image = new Button("Resize image");

            newUI.add(button_newUI_resize_image,0,2);
            
            GridPane newUI_zoom_gridPane = new GridPane();
            newUI.add(newUI_zoom_gridPane,0,3);
            
            newUI_zoom_gridPane.add(label_block_size, 0,0);
            newUI_zoom_gridPane.add(field_zoom, 1,0);
            field_zoom.setMaxWidth(40);
            field_zoom.setMinWidth(40);
            newUI_zoom_gridPane.add(button_block_size_apply, 2,0);
            
            GridPane newUI_toolbuttons = new GridPane();
            newUI.add(newUI_toolbuttons,1,0);
            
            final BlockButton button_newUI_draw_wall = new BlockButton();
            final BlockButton button_newUI_draw_parking = new BlockButton();
            final BlockButton button_newUI_gate = new BlockButton();
            final BlockButton button_newUI_clear = new BlockButton();
            ToggleGroup toolsNew = new ToggleGroup();
            button_newUI_draw_wall.setToggleGroup(toolsNew);
            button_newUI_draw_parking.setToggleGroup(toolsNew);
            button_newUI_gate.setToggleGroup(toolsNew);
            button_newUI_clear.setToggleGroup(toolsNew);
            
            button_newUI_draw_wall.setGraphic(new Rectangle(16,16,Block.textures[5]));
            button_newUI_draw_parking.setGraphic(new Rectangle(16,16,Block.textures[1]));
            button_newUI_gate.setGraphic(new Rectangle(16,16,Block.textures[4]));
            button_newUI_clear.setGraphic(new Rectangle(16,16,Block.textures[2]));

            newUI_toolbuttons.add(button_newUI_draw_wall,1,0);
            newUI_toolbuttons.add(button_newUI_draw_parking,2,0);
            newUI_toolbuttons.add(button_newUI_gate,3,0);
            newUI_toolbuttons.add(button_newUI_clear,4,0);
            
            
            button_newUI_draw_wall.setOnAction(
            		new EventHandler<ActionEvent>() {
            			@Override public void handle(ActionEvent e) {
            	        	button_tool_place.setDisable(false);
            	        	button_tool_fill.setDisable(false);
            	        	button_tool_laimis.setDisable(false);
            	        	button_tool_custom_brush_size.setDisable(false);
            				if (currentTool == 1){
                				button_tool_place.fire();
            					
            				}
            				currentTool = 5;
            				//väli_selected_block_tool.setText(5+ "" );
            			}      
            		}
            		);
            button_newUI_draw_parking.setOnAction(
            		new EventHandler<ActionEvent>() {
            			@Override public void handle(ActionEvent e) {
            				if (!button_tool_custom_brush_size.isSelected()){
                				button_tool_custom_brush_size.fire();
            					
            				}
//            	        	button_tool_place.setDisable(true);
//            	        	button_tool_fill.setDisable(true);
//            	        	button_tool_laimis.setDisable(true);
//            	        	button_tool_custom_brush_size.setDisable(true);
            				currentTool = 1;
            				//väli_selected_block_tool.setText(1+ "" );
            			}      
            		}
            		); 

        	
            button_newUI_gate.setOnAction(
            		new EventHandler<ActionEvent>() {
            			@Override public void handle(ActionEvent e) {
            	        	button_tool_place.setDisable(false);
            	        	button_tool_fill.setDisable(false);
            	        	button_tool_laimis.setDisable(false);
            	        	button_tool_custom_brush_size.setDisable(false);
            				if (currentTool == 1){
                				button_tool_place.fire();
            					
            				}
            				currentTool = 4;
            				//väli_selected_block_tool.setText(4+ "" );
            			}      
            		}
            		);
            
            button_newUI_clear.setOnAction(
            		new EventHandler<ActionEvent>() {
            			@Override public void handle(ActionEvent e) {
            	        	button_tool_place.setDisable(false);
            	        	button_tool_fill.setDisable(false);
            	        	button_tool_laimis.setDisable(false);
            	        	button_tool_custom_brush_size.setDisable(false);
            				if (currentTool == 1){
                				button_tool_place.fire();
            					
            				}
            				currentTool = 2;
            				//väli_selected_block_tool.setText(2+ "" );
            			}      
            		}
            		);   
            
            
            final Button button_newUI_resize_p = new Button("Resize P");
            newUI.add(button_newUI_resize_p,1,1);
            
            
            final Button button_newUI_run = new Button("Make robot lot");
            


            
            
            
            // vv dialog
            final Stage newUI_resize_image_dialog_stage = new Stage();
            Scene newUI_resize_image_dialog_scene = new Scene(image_resize_dialog_gridpane);
            newUI_resize_image_dialog_stage.setTitle("Resize Image");
            newUI_resize_image_dialog_stage.setScene(newUI_resize_image_dialog_scene);
            
            //^^ dialog 
            
            newUI.add(button_newUI_choose_image,0,0);            
            button_newUI_choose_image.setOnAction(
                    new EventHandler<ActionEvent>() {
                        @Override public void handle(ActionEvent e) {
                        	//newUI_resize_image_dialog_stage.show();
                        	

                            File image_file = imageChooser.showOpenDialog(primaryStage);
                        	if (image_file!=null){

                        	    field_image_file.setText(image_file.toString());
                        	    button_refresh_image.fire();
                        	}
                        	
                        }      
                }
            );
            

            
            button_newUI_resize_image.setOnAction(
                    new EventHandler<ActionEvent>() {
                        @Override public void handle(ActionEvent e) {
                        	newUI_resize_image_dialog_stage.show();
                        }      
                }
            );
            

            
            
            // vv dialog
            final Stage newUI_resize_p_dialog_stage = new Stage();
            
            GridPane newUI_resize_p_dialog_gridpane = new GridPane();
            Scene newUI_resize_p_dialog = new Scene(newUI_resize_p_dialog_gridpane);
            newUI_resize_p_dialog_stage.setTitle("Resize P");
            newUI_resize_p_dialog_stage.setScene(newUI_resize_p_dialog);
            
            //^^ dialog
           
            
            
            
            field_pencil_width.setText(pencil_width + "");
            field_pencil_height.setText(pencil_height + "");
            
            newUI_resize_p_dialog_gridpane.add(field_pencil_width, 1, 0);
            newUI_resize_p_dialog_gridpane.add(field_pencil_height, 2, 0);
            newUI_resize_p_dialog_gridpane.add(button_pencil_size_apply, 3, 0);
            
            //newUI_resize_p_dialog_stage.show();
            button_newUI_resize_p.setOnAction(
                    new EventHandler<ActionEvent>() {
                        @Override public void handle(ActionEvent e) {
                        	newUI_resize_p_dialog_stage.show();
                        }      
                }
            );

            newUI.add(modes,1,2);
            
            GridPane filelabels = new GridPane();

            newUI.add(filelabels,1,3);
            
            filelabels.add(label_newUI_image,1,0);
            
            filelabels.add(label_newUI_dat,0,0);
            filelabels.setHgap(10);
            filelabels.setVgap(4);
            

            final Button button_newUI_save_dat = new Button("Save dat");
            newUI.add(button_newUI_save_dat,2,0);
            newUI.add(button_newUI_run,2,1);
            newUI.add(label_newUI_parkingSlots,2,2);
            newUI.add(label_newUI_robotSlots,2,3);
            
            ColumnConstraints column1 = new ColumnConstraints();
            column1.setPercentWidth(33);
            newUI.getColumnConstraints().add(column1);
            newUI.getColumnConstraints().add(column1);
            newUI.getColumnConstraints().add(column1);
            
            
 
//            // old UI window
//            GridPane oldRoot = new GridPane();
//            //oldRoot.add(ruudustik, 0, 0);
//            oldRoot.add(oldUI_block_toolbar_gridpane, 0, 1);
//            
//            // vv
//
//            Scene scene3 = new Scene(oldRoot);
//            stage3.setTitle("Old UI");
//            stage3.setScene(scene3);
//            //^^
//            
//            // vv
            
            root.add(newUI, 0, 3);

            root.add(gridPane_infolabels, 0, 5);
                
            GridPane scrollableContent = new GridPane();
            GridPane scrollableContent2 = new GridPane();
            scrollableContent.add(canvas, 0, 0);
            scrollableContent.add(level, 0, 0);

            final ScrollPane scrollable = new ScrollPane();
            scrollable.setHbarPolicy(ScrollBarPolicy.ALWAYS);
            scrollable.setVbarPolicy(ScrollBarPolicy.ALWAYS);
            //scrollable.setContent(level);
            scrollable.setContent(scrollableContent);
            root.add(scrollable, 0, 4);


            gridPane_infolabels.add(label_mouse_cordinates, 0, 0);
            gridPane_infolabels.add(silt5, 1, 0);
            gridPane_infolabels.getColumnConstraints().add(column1);
            gridPane_infolabels.getColumnConstraints().add(column1);
            //gridPane_infolabels.add(number_of_parking_slots_Silt, 0, 2);
 
               
  
 
            final Rectangle rectBG = new Rectangle(0,0,Block.suurus*level_width,Block.suurus*level_height);
            rectBG.setFill(Color.TRANSPARENT);
            uus.getChildren().add(rectBG);
            
            button_block_size_apply.setOnAction(
            		new EventHandler<ActionEvent>() {
            			@Override public void handle(ActionEvent e) {
            				validate_input();
            				int uusSuurus =  Integer.parseInt( field_zoom.getText() );

            				
            				vanaSuurus = Block.suurus;
            				Block.suurus = uusSuurus;
            				canvas.setHeight(uusSuurus*level_height);
            				canvas.setWidth(uusSuurus*level_width);
            				
            				rectBG.setWidth(uusSuurus*level_width);
            				rectBG.setHeight(uusSuurus*level_height);
//            				rectBG.setX(uusSuurus*level_width);
//            				rectBG.setY(uusSuurus*level_height);
            				
            				int x =  Integer.parseInt( field_image_width.getText() );
            				int y =  Integer.parseInt( field_image_height.getText() );
            				field_image_width.setText(((x/vanaSuurus)*Block.suurus)+"");
            				field_image_height.setText(((y/vanaSuurus)*Block.suurus)+"");
            				Block.suurus = vanaSuurus;
            				button_refresh_image.fire();
            				Block.suurus = uusSuurus;
            				
            				reset();

            			}
            		}
            		);

            button_reset_image_size.setOnAction(
            		new EventHandler<ActionEvent>() {
            			@Override public void handle(ActionEvent e) {
            				field_image_width.setText(level_width*Block.suurus+"");
            				field_image_height.setText(level_height*Block.suurus+"");
            				
            			}      
            		}
            		);
           

            button_newUI_save_dat.setOnAction(
            		new EventHandler<ActionEvent>() {

            			@Override public void handle(ActionEvent e) {
            				try{           
            					
            					if (last_path.equals("") ){
                					File dat_file = fileChooser.showSaveDialog(primaryStage);
                					saveLevel( dat_file  );
                					last_path = dat_file.toString();

                				    label_newUI_dat.setText(last_path);
            					}
            					else{
            						saveLevel( new File(last_path)  );
            					}

            				}
            				catch(Exception erind){
            					silt5.setText("salvestamine, tekkis: "+erind+ " - " + df.format(System.currentTimeMillis() )  );

            				}

            			}
            		}

            		);

            button_pencil_size_apply.setOnAction(
                    new EventHandler<ActionEvent>() {
                   
                            @Override public void handle(ActionEvent e) {

                				validate_input();
                                    pencil_width = Integer.parseInt(field_pencil_width.getText());
                                    pencil_height = Integer.parseInt(field_pencil_height.getText());
                                    button_tool_custom_brush_size.setText(pencil_width + "x" + pencil_height);    
                                    //button_newUI_resize_p.setText("Resize P" + "(" + pencil_width + "x" + pencil_height + ")"); 
                    				newUI_resize_p_dialog_stage.hide();
                            }
                    }
                );
            
            button_refresh_image.setOnAction(
            		new EventHandler<ActionEvent>() {

            			@Override public void handle(ActionEvent e) {                                                          

            				try{
            					
            					gc.clearRect(Block.suurus,Block.suurus,image_width,image_height);
            					gc.setFill(new ImagePattern(new Image(new FileInputStream(new File(field_image_file.getText())))));
            					image_width = Integer.parseInt(field_image_width.getText());
            					image_height = Integer.parseInt(field_image_height.getText());
            					gc.fillRect(Block.suurus,Block.suurus,image_width,image_height);
            				}
            				catch(Exception f){
            					if (f instanceof java.io.FileNotFoundException){
            						System.out.println(f+ " While opening image. This is intended, when user wants a blank background or program is just now opened.");
            					}
            					else if (f instanceof java.lang.IllegalArgumentException){
                					silt5.setText("Image not recognised - " + df.format(System.currentTimeMillis() )  );
            						System.out.println(f+ " While opening image. Not an actual image file.");
            					}
            					else{
            						//silt5.setText("While opening image: "+f+ " - " + df.format(System.currentTimeMillis() )  );
            						System.out.println(f+ " While opening image. Unexpected exception.");
            					}
            				}
            				finally{
            					newUI_resize_image_dialog_stage.hide();
            				}



            			}
            		}

            		);
 
           // Run algorithm
            button_newUI_run.setOnAction(
            		new EventHandler<ActionEvent>() {
            			@Override public void handle(ActionEvent e) {
            				level2.getChildren().removeAll(level2.getChildren());
            				number_of_parking_slots_2 = 0;

            				try {		
            					for(int i = 0; i<Main.level_width; i++){
            						for(int j = 0; j<Main.level_height; j++){


            							int uusP = ((Block) Main.getNodeFromGridPane(Main.level, i, j)).nr; // milline on vana plokk kohas i, j
            							//multiple entries - per 9 piece batches


            							// priit lisas; ignoreerib vanu parkimiskohti
            							if (uusP == 1 | uusP ==  7 | uusP ==  8 | uusP ==  9 | uusP ==  10 | uusP ==  11 | uusP ==  12 | uusP ==  13){
            								uusP = 2;
            							}

            							Main.add_block2( uusP  , i, j  );      // lisab uude Main.levelisse samasse kohta samasuguse ploki

            						}
            					}
            					// this is where laimis's code used to be that is now in class Algorithm

            					//Algorithm.run();   

            					//gg.run();
            					int roboSpaces = algorithm_thread.make();
            					label_newUI_robotSlots.setText("Number of robot parking slots: " + roboSpaces);
            					//(new Thread(gg)).start();

            					button_newUI_run.setDisable(true);
            					try{
            						// inform user of test results
            						int actual = Integer.parseInt(label_newUI_robotSlots.getText().split(" ")[label_newUI_robotSlots.getText().split(" ").length-1]);
            						System.out.println(
            								"\n\nGenerated parking spots\n"
            										+ "Expected: " + arguments[1] + 
            										", Actual: "+ actual);
            						if (actual != Integer.parseInt(arguments[1])){
            							System.out.println("\n!!! Expected does not match actual.\n");
            						}

            					}catch (Exception erind_minor){
            					}

            				}
            				catch (Exception erind) {
            					System.out.println(erind + " - While running algorithm.");
            				}
            				stage2.show();
            			}
            		}
            		);
           
           
           
           
           
           
           

           
            
//            scene.getAccelerators().put(
//            		new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_ANY), 
//            		  new Runnable() {
//            		@Override public void run() {
//            			Integer[] comm = pastReplaces.pop();
//            			replace_block(comm[0],comm[2],comm[1], level);
//
//            		}
//            		  }
//            		);
            
           // second window
            final ScrollPane scrollable2 = new ScrollPane();
            scrollable2.setHbarPolicy(ScrollBarPolicy.ALWAYS);
            scrollable2.setVbarPolicy(ScrollBarPolicy.ALWAYS);


            
            scrollableContent2.add(level2, 0, 0);
            scrollableContent2.add(uus, 0, 0);
     
            scrollable2.setContent(scrollableContent2);
            
            //scrollable2.setContent(level2);
            //scrollable2.setContent(canvas2);
            
            
            final GridPane simUI_main = new GridPane();
            GridPane simUI = new GridPane();
            Label simUI_field_explaination_entering = new Label("Entering cars (per hour): ");
            simUI.add(simUI_field_explaination_entering, 0, 0);
            final NumberTextField simUI_field_entering = new NumberTextField();
            simUI.add(simUI_field_entering, 0, 1);

            Label simUI_field_explaination_exiting = new Label("Exiting cars (per hour)");
            simUI.add(simUI_field_explaination_exiting, 1, 0);
            final NumberTextField simUI_field_exiting = new NumberTextField();
            simUI.add(simUI_field_exiting, 1, 1);
            
            Label simUI_field_explaination_robo_speed = new Label("Speed (tiles per second)");
            simUI.add(simUI_field_explaination_robo_speed, 2, 0);
            final TextField simUI_field_robo_speed = new TextField();
            simUI.add(simUI_field_robo_speed, 2, 1);

            Label simUI_field_explaination_robo_nr = new Label("No. of robots");
            simUI.add(simUI_field_explaination_robo_nr, 3, 0);
            final NumberTextField simUI_field_robo_nr = new NumberTextField();
            simUI.add(simUI_field_robo_nr, 3, 1);
            simUI_field_robo_nr.setDisable(true);
            
            Label simUI_field_explaination_hour_length = new Label("One hour in ms");
            simUI.add(simUI_field_explaination_hour_length, 4, 0);
            final NumberTextField simUI_field_hour_length = new NumberTextField();
            simUI.add(simUI_field_hour_length, 4, 1);
            
            //simUI_field_hour_length.setDisable(true);
            
            final Button button_simUI_apply = new Button("Apply changes");
            simUI.add(button_simUI_apply, 5, 1);
            button_simUI_apply.setOnAction(
                    new EventHandler<ActionEvent>() {
                   
                            @Override public void handle(ActionEvent e) {
                            	Simulation.oneHourInMs = Integer.parseInt(simUI_field_hour_length.getText());
                            	
                            	Simulation.numberOfCarsIn = Integer.parseInt(simUI_field_entering.getText());
                            	Simulation.numberOfCarsOut = Integer.parseInt(simUI_field_exiting.getText());
                            	double speed = Double.parseDouble(simUI_field_robo_speed.getText());
                            	
                            	if(Simulation.validateSpeed(speed)){
                            		Simulation.botSpeed = speed;
                            	}
                            	else{
                            		Simulation.botSpeed = Simulation.maxSpeed();
                            		simUI_field_robo_speed.setText(Simulation.maxSpeed()+"");
                            	}
                            	
                            	Simulation.numberOfbots = Integer.parseInt(simUI_field_robo_nr.getText());
                            	
//                            	Simulation.oneHourInMsChanged = true;
//                            	Simulation.oneHourInMsNew = Integer.parseInt(simUI_field_hour_length.getText());
                            }
                    }
                );
            ;
            final Button button_simUI_run = new Button("Run");
            simUI.add(button_simUI_run, 6, 1);
            button_simUI_run.setOnAction(
                    new EventHandler<ActionEvent>() {
                   
                            @Override public void handle(ActionEvent e) {
                            	if(button_simUI_run.getText().equals("Run")){

                            	    field_zoom.setDisable(true);
                            	    simUI_field_hour_length.setDisable(true);
                            		//Animation.stopped = false;
                            		button_simUI_run.setText("Running");
                                	//Simulation.run();
                            		
                                    //Platform.runLater(sim);
                            		
                                    //(new Thread(sim)).start();

                					(new Thread(sim)).start();
                					
//                					try {
//                						Thread.sleep(1000);
//	                				} 
//	                				catch (InterruptedException ee) {
//	                					System.out.println(ee);
//	                				}
                					
                					
                					(new Thread(algorithm_thread)).start();
                				
                                    
                            	}
                            	else{
                            		//button_simUI_run.setText("Run");
                            		//Animation.pause();
                            	}
                            }
                    }
                );
            ;
            simUI_main.add(simUI, 0, 0);
            simUI_main.add(scrollable2, 0, 1);
            
            simUI.add(simUI_hour_indicator, 7, 1);
            simUI_hour_indicator.setText("0:00");
            
            stage2.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    System.out.println("Stage2 is closing. Thread should be killed soon.");
                    //Animation.kill();
                    Algorithm.kill();
            		button_simUI_run.setText("Run");
            		//button_simUI_run.setText("Run");
                }
            }); 
            
            
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    System.out.println("Primary stage is closing. Thread should be killed soon.");
                    //Animation.kill();
                    Algorithm.kill();
                    System.exit(0);
            		//button_simUI_run.setText("Run");
                }
            }); 
            
            
            final Scene scene2 = new Scene(simUI_main,1600,900);
            stage2.setTitle("Robot simulation");
            stage2.setScene(scene2);
           
            scrollable.setMaxWidth(scene.getWidth());
            scrollable.setMinWidth(scene.getWidth());
            scrollable.setMaxHeight(scene.getHeight()-120);
            scrollable.setMinHeight(scene.getHeight()-120);
            
            scrollable2.setMaxWidth(scene.getWidth());
            scrollable2.setMinWidth(scene.getWidth());
            scrollable2.setMaxHeight(scene.getHeight()-40);
            scrollable2.setMinHeight(scene.getHeight()-40);
            
            



            
            scene.widthProperty().addListener(new ChangeListener<Number>() {

                @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {

                    //System.out.println("Width: " + newSceneWidth);
                    scrollable.setMaxWidth(scene.getWidth());
                    scrollable.setMinWidth(scene.getWidth());
                    //canvas.setWidth(scene.getWidth());
                }

            });

            scene.heightProperty().addListener(new ChangeListener<Number>() {

                @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {

                    //System.out.println("Height: " + newSceneHeight);
                    scrollable.setMaxHeight(scene.getHeight()-120);
                    scrollable.setMinHeight(scene.getHeight()-120);
                    //canvas.setHeight(scene.getHeight()-120);

                }

            });
            
            scene2.widthProperty().addListener(new ChangeListener<Number>() {

                @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {

                    //System.out.println("Width: " + newSceneWidth);
                    scrollable2.setMaxWidth(scene2.getWidth());
                    scrollable2.setMinWidth(scene2.getWidth());
                    //canvas.setWidth(scene.getWidth());
                }

            });

            scene2.heightProperty().addListener(new ChangeListener<Number>() {

                @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {

                    //System.out.println("Height: " + newSceneHeight);
                    scrollable2.setMaxHeight(scene2.getHeight()-40);
                    scrollable2.setMinHeight(scene2.getHeight()-40);
                    //canvas.setHeight(scene.getHeight()-120);

                }

            });


            
            level.addEventHandler(MouseEvent.MOUSE_DRAGGED, new MouseEvent_Drag_Handler(level));
            updateParkingLabels();

            button_newUI_draw_wall.fire();
            
            //field_level_filename.setText("levels.dat");
            try{
            	loadLevel(new File(arguments[0]));
            }
            catch (Exception e){
            	loadLevel(new File(""));
            	System.out.println("You have not specified filename launch argument.");
            }
            

            //field_image_file.setText("parkla.png");
            
            //button_refresh_image.fire();
            
            //nupp.fire();
            
//            button_newUI_run.fire();
//            primaryStage.setIconified(true);
            
            simUI_field_entering.setText(Simulation.numberOfCarsIn+"");
            simUI_field_exiting.setText(Simulation.numberOfCarsOut+"");
            simUI_field_robo_speed.setText(Simulation.botSpeed+"");
            simUI_field_robo_nr.setText(Simulation.numberOfbots+"");
            simUI_field_hour_length.setText(Simulation.oneHourInMs+"");
            //button_refresh_image.fire();
            // show main window
            //stage3.show();
            primaryStage.show();
            try{
            	System.out.println("Expecting "+arguments[1]+" parking spots.");
            	button_newUI_run.fire();
            	primaryStage.close();
            	stage2.close();
            }
            catch (Exception e){
            	System.out.println("You have not specified expected number parking spot value launch argument.");
            }
            
        }
       
        public static void main(String[] args) {
               arguments=args;
                launch(args);
        }
}