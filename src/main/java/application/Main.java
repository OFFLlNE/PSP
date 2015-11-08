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
	
	// default values
	static int level_width = 240;
	static int level_height = 100;

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

	static int number_of_parking_slots_1 = 0;
	static int number_of_parking_slots_2 = 0;

	static int mouseX;
	static int mouseY;

	static int pildiLaius = level_width*Block.suurus;
	static int pildiKõrgus = level_height*Block.suurus;

	static int vanaSuurus = Block.suurus;

    static Group uus = new Group();

	static Block[][] plokid = new Block[level_width][level_height];
	static Block[][] plokid2 = new Block[level_width][level_height];
	
	
	
	static ArrayList<Integer> legal_blocks = new ArrayList<Integer>();

	BlockButton button_tool_place;
	BlockButton button_tool_fill;
	BlockButton button_tool_laimis;
	BlockButton button_tool_custom_brush_size;

	static ArrayList<String> files = new ArrayList<String>();
	static Animation sim = new Animation();

	class MouseEvent_Handler implements EventHandler<MouseEvent> {
		Block a;

		public MouseEvent_Handler(Block a) {
			this.a = a;
		}

		public void handle(MouseEvent me) {
			if (me.getEventType()==MouseEvent.MOUSE_ENTERED)
			{

				if (button_tool_fill.isSelected()){
					//mark_neighbours(a, Color.AQUA, a.nr, false, 0, level);
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
							((Block)getNodeFromGridPane(level, a.x+i, a.y+j)).setFill(Color.AQUA);
						}
					}

				}      

			}

			else if (me.getEventType()==MouseEvent.MOUSE_CLICKED)
			{


				if (button_tool_fill.isSelected()){
					mark_neighbours(a, Color.AQUA, a.nr, true, currentTool, level);
				}

				//if (a.x != 0 & a.y != 0 & a.x != 59 & a.y != 23 ) {
				replace_block(currentTool, a.x, a.y, level);
				//      };


				if (button_tool_laimis.isSelected()){

					//LAIMIS               
					if (started == false){
						started = true;
						ui = a;
					}
					else{
						started = false;

						for(int i = 0; i< Math.abs(ui.x - a.x)+1; i++){
							for(int j = 0; j< Math.abs(ui.y - a.y)+1; j++){


								replace_block(currentTool, a.x+i, a.y+j, level);
								//Parkla weJustGeneratedThisRectangle = new Parkla(xalgus, xlopp, yalgus, ylopp)

							}
						}

					}
					//LAIMIS END

				}      

				if (button_tool_custom_brush_size.isSelected()){
					
					boolean pTäht = false;

					for(int i = 0; i< pencil_width; i++){
						for(int j = 0; j< pencil_height; j++){


							if (currentTool == Block._PARKING_P){
								if (i==0&&j==0){
									replace_block(Block._PARKING_TOP_LEFT, a.x+i, a.y+j, level);
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


			else {
				mouseX = a.x;
				mouseY = a.y;      

				if (button_tool_fill.isSelected() && a.nr != 6){
					mark_neighbours(a, a.see, a.nr, false, 0, level);
				}

				//märgiTeatud(a, a.see, a.nr, 4);
				if (a.nr != 6){
					a.setFill(a.see);
				}

				if (button_tool_custom_brush_size.isSelected()){


					for(int i = 0; i< pencil_width; i++){
						for(int j = 0; j< pencil_height; j++){
							((Block)getNodeFromGridPane(level, a.x+i, a.y+j)).setFill(((Block)getNodeFromGridPane(level, a.x+i, a.y+j)).see);
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
			if (me.getEventType()==MouseEvent.MOUSE_DRAGGED)
			{

				//if ((int)me.getX()/Plokk.suurus != 0 & (int)me.getY()/Plokk.suurus != 0 & (int)me.getX()/Plokk.suurus != 59 & (int)me.getY()/Plokk.suurus != 23 ) {

				//((Plokk) getNodeFromGridPane(level, (int)me.getX()/Plokk.suurus, (int)me.getY()/Plokk.suurus)  ).muuda(currentTool);
				if (!button_tool_custom_brush_size.isSelected()){
					replace_block(currentTool, (int)me.getX()/Block.suurus, (int)me.getY()/Block.suurus, level);
				}
				//};

				label_mouse_cordinates.setText("(" + (int)me.getX()/Block.suurus + ", " + (int)me.getY()/Block.suurus + ")");



			}
		}      
	}
	
	public static void colorParking(int x, int y,GridPane lvl){
		boolean pTäht = false;

		for(int i = 0; i< pencil_width; i++){
			for(int j = 0; j< pencil_height; j++){

					if (i==0&&j==0){
						replace_block(Block._PARKING_TOP_LEFT, x+i, y+j, lvl);
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
	
    public static int getType(int x, int y){
    	return ((Block) Main.getNodeFromGridPane(Main.level2, x, y)).nr;
    }
    // FILL ALGORITHM
    public static Deque<Block> to_mark = new ArrayDeque<Block>();
    public static void mark_neighbours(Block a, Paint b, int eelmine, boolean kasAsendada, int tool, GridPane lvl){
    	if (a.nr != eelmine){

    		//System.out.println("ok!");
    	}


    	Block W = neighbourW(a.x, a.y, lvl);
    	Block S = neighbourS(a.x, a.y, lvl);
    	Block E = neighbourE(a.x, a.y, lvl);
    	Block N = neighbourN(a.x, a.y, lvl);

    	if (a.nr != 6){
    		a.setFill(b);
    	}

    	if (kasAsendada){
    		//System.out.println("värvida: " + a.x + ", " + a.y);
    		replace_block(tool, a.x, a.y, lvl);
    	}

    	if (!kasAsendada){
    		if (!W.getFill().equals(b) && W.nr == eelmine && W != null) { to_mark.push(W);};
    		if (!S.getFill().equals(b) && S.nr == eelmine && S != null  ) {         to_mark.push(S);};
    		if (!E.getFill().equals(b) && E.nr == eelmine && E != null  ) {         to_mark.push(E);};
    		if (!N.getFill().equals(b) && N.nr == eelmine && N != null  ) {         to_mark.push(N);};
    	}
    	else{
    		if (W.nr != a.nr && W.nr == eelmine && W != null) { to_mark.push(W);};
    		if (S.nr != a.nr  &&  S.nr == eelmine && S != null  ) {         to_mark.push(S);};
    		if (E.nr != a.nr &&  E.nr == eelmine && E != null  ) {          to_mark.push(E);};
    		if (N.nr != a.nr && N.nr == eelmine && N != null  ) {           to_mark.push(N);};
    	}




    	try{ 
    		mark_neighbours(to_mark.pop(), b, eelmine, kasAsendada, tool, lvl);
    	}

    	catch(Exception e){
    		//new Alert(Alert.AlertType.ERROR, "Tere", ButtonType.OK);
    		//System.out.println(e);
    		to_mark.clear();
    		return;
    	}

    }

   
   
   
    public static Block neighbourS(int x, int y, GridPane lvl){
        return (Block)getNodeFromGridPane(lvl, x, y+1);
       
    }
   
    public static Block neighbourW(int x, int y, GridPane lvl){
        return (Block)getNodeFromGridPane(lvl, x+1, y);
       
    }
   
   
    public static Block neighbourE(int x, int y, GridPane lvl){
        return (Block)getNodeFromGridPane(lvl, x-1, y);
       
    }
   
    public static Block neighbourN(int x, int y, GridPane lvl){
        return (Block)getNodeFromGridPane(lvl, x, y-1);
       
    }
   
 
   
    public static Block getNodeFromGridPane(GridPane gridPane, int col, int row) {
        //long a = System.currentTimeMillis();
    	if (gridPane==level){
    		return plokid[col][row];
    	}else{
             return plokid2[col][row]; 
//        for (Node node : gridPane.getChildren()) {
//            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
//              //System.out.println(System.currentTimeMillis()-a + "\n");
//                return node;
//            }
//             
//
//        }
//        
//     
//        
//        
//        return null;
    	}
       
    }
   
 
        public static void loadLevel(int mitmes, String fail) throws  FileNotFoundException, IOException, ClassNotFoundException, WrongFileException {
                silt5.setText("Laen...");
                RandomAccessFile r = new RandomAccessFile("levels/"+fail, "r");
                mitmes--;
                r.skipBytes(mitmes*1536);
                for(int i = 1; i<=level_height; i++){
                        for(int j = 1; j<=level_width; j++){
                        		
                                int a = r.read();
                                if (legal_blocks.contains(a)){
                                //System.out.println("("+ i + ", " + j + ") - " + a);
                                	l2.add(a);
                                }
                                else{
                                	r.close();
                                	throw new WrongFileException();
                                }
                        }
                        //System.out.println();
                }
                r.close();
               
                silt5.setText("Laetud! - " + df.format(System.currentTimeMillis() )  );
        }
       
       
       
        public static void saveLevel(int mitmes, String fail) throws  FileNotFoundException, IOException, ClassNotFoundException {
                silt5.setText("Salvestan...");
                RandomAccessFile r = new RandomAccessFile("levels/"+fail, "rw");
                //mitmes--;
                System.out.println("salvestan");
                //r.skipBytes(mitmes*1536);
                for(int i = 0; i<=level_height-1; i++){
                        for(int j = 0; j<=level_width-1; j++){
                        	//System.out.println("("+ i + ", " + j + ") - " + a);
                        	//System.out.println((Plokk) getNodeFromGridPane(level, j, i));
                        	r.write(         ((Block) getNodeFromGridPane(level, j, i)).nr              );
                        }
                        //System.out.println();
                }
                r.close();
                silt5.setText("Salvestatud! - " + df.format(System.currentTimeMillis() )  );
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
                	plokid[j][i] = r;
                }
                else if(lvl == level2){
                	plokid2[j][i] = r;
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
            plokid2[j][i] = r;
           
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
                updateParkingLabels();
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
                        Block.textures[i] = new ImagePattern(new Image(new FileInputStream(new File(i + ".png"))));
                }
                Block.textures[48] = new ImagePattern(new Image(new FileInputStream(new File("r2.png")))); 
                Block.textures[49] = new ImagePattern(new Image(new FileInputStream(new File("r.png")))); 
                //System.out.println("Done!");
        }
       
       
        public static void updateParkingLabels(){

            label_newUI_parkingSlots.setText("Number of regular parking slots: " + number_of_parking_slots_1);
            label_newUI_robotSlots.setText("Number of robot parking slots: " + number_of_parking_slots_2 );
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
        
        @Override
        public void start(final Stage primaryStage) throws  FileNotFoundException, IOException, ClassNotFoundException {
               
            readTextures();
               
//            Runtime runtime = Runtime.getRuntime();
//            NumberFormat format = NumberFormat.getInstance();
//            StringBuilder sb = new StringBuilder();
//            long maxMemory = runtime.maxMemory();
//            long allocatedMemory = runtime.totalMemory();
//            long freeMemory = runtime.freeMemory();
//            sb.append("\nfree memory: " + format.format(freeMemory / 1024) + "\n");
//            sb.append("allocated memory: " + format.format(allocatedMemory / 1024) + "\n");
//            sb.append("max memory: " + format.format(maxMemory / 1024) + "\n");
//            sb.append("total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024) + "\n\n");
//            System.out.println(sb);
             
            GridPane root = new GridPane();
            final Scene scene = new Scene(root,1600,900);
            primaryStage.setTitle("PitStop Parking");
            primaryStage.setScene(scene);
            
            
            // load dialog
            GridPane ruudustik = new GridPane();
            Label silt = new Label("Level: ");
            final Button nupp = new Button("Open");
            final TextField field_level_filename = new TextField();
            
            
            // vv OLD UI
            final TextField väli_selected_block_tool = new TextField();
            
            GridPane oldUI_block_toolbar_gridpane = new GridPane();

            ToggleGroup BlockButtons = new ToggleGroup();

            // block selection toolbar for old UI
            for (int i = 0; i<14; i++){

            	if (i < 41 ){
            		final BlockButton temp = new BlockButton();
            		temp.i = i;
            		//oldUI_block_toolbar_gridpane.getChildren().add(temp);
            		//StackPane.setMargin(temp, new Insets(0, 0, 0, 0));

            		oldUI_block_toolbar_gridpane.add(temp, i, 0);

            		Rectangle temp2 = new Rectangle();

            		temp2.setX(0);
            		temp2.setY(0);
            		temp2.setWidth(16
            				//Plokk.suurus
            				);
            		temp2.setHeight(16
            				//Plokk.suurus
            				);
            		temp2.setFill(Block.textures[i]);


            		temp.setGraphic(temp2);

            		temp.setToggleGroup(BlockButtons);

            		temp.setOnAction(
            				new EventHandler<ActionEvent>() {
            					@Override public void handle(ActionEvent e) {
            						currentTool = temp.i;
            						väli_selected_block_tool.setText( temp.i + "" );
            					}      
            				}
            				);
            		if (i==0){
            			temp.fire();
            		}

            		//Label rl = new Label(i + "");
            		//rl.setMouseTransparent(true);
            		//oldUI_block_toolbar_gridpane.add(rl, i, 0);

            	}

            }
            
            // programmi toolbar
            ruudustik.add(silt, 1, 0);
            ruudustik.add(field_level_filename, 2, 0);
            //ruudustik.add(väli, 3, 0);
            ruudustik.add(nupp, 4, 0);
            // programmi toolbar
            

            
            oldUI_block_toolbar_gridpane.add(väli_selected_block_tool, 20, 0);
            
            // ^^ OLD UI
             

            Label label_block_size = new Label("Zoom: ");
            Button button_block_size_apply = new Button("Set");
            final NumberTextField field_zoom = new NumberTextField();
           
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
            final NumberTextField pildiLaiusVäli = new NumberTextField();
            Label label_bgimage_y = new Label("Height: ");
            final NumberTextField pildiKõrgusVäli = new NumberTextField();
            Label pildifailSilt = new Label("Filename: ");
            final TextField field_image_file = new TextField();
            final Button button_reset_image_size = new Button("Reset size");
            final Button button_refresh_image = new Button("Apply image");
               
            image_resize_dialog_gridpane.add(label_bgimage_x, 1, 0);
            image_resize_dialog_gridpane.add(pildiLaiusVäli, 2, 0);
            image_resize_dialog_gridpane.add(label_bgimage_y, 3, 0);
            image_resize_dialog_gridpane.add(pildiKõrgusVäli, 4, 0);
            image_resize_dialog_gridpane.add(pildifailSilt, 5, 0);
            image_resize_dialog_gridpane.add(field_image_file, 6, 0);
            image_resize_dialog_gridpane.add(button_reset_image_size, 10, 0);
            image_resize_dialog_gridpane.add(button_refresh_image, 11, 0);
           
            pildiLaiusVäli.setText(pildiLaius+"");
            pildiKõrgusVäli.setText(pildiKõrgus+"");
            

 
            final Canvas canvas = new Canvas(level_width*Block.suurus,level_height*Block.suurus);
            
            final GraphicsContext gc = canvas.getGraphicsContext2D();

            field_zoom.setText(Block.suurus + "");
 


            GridPane modes = new GridPane();
            modes.add(button_tool_place, 16, 0);
            modes.add(button_tool_fill, 17, 0);
            modes.add(button_tool_laimis, 18, 0);
            modes.add(button_tool_custom_brush_size, 19, 0);
 

            
            // new UI

            GridPane newUI = new GridPane();
            
            final Button button_newUI_choose_image = new Button("Choose image");

            
            final Button button_newUI_choose_dat = new Button("Choose .dat file");
            newUI.add(button_newUI_choose_dat,0,1);
            button_newUI_choose_dat.setOnAction(
                    new EventHandler<ActionEvent>() {
                        @Override public void handle(ActionEvent e) {
                            stage3.show();
                            stage3.setWidth(248);
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
            				if (!button_tool_place.isSelected()){
                				button_tool_place.fire();
            					
            				}
            				currentTool = 5;
            				väli_selected_block_tool.setText(5+ "" );
            			}      
            		}
            		);
            button_newUI_draw_parking.setOnAction(
            		new EventHandler<ActionEvent>() {
            			@Override public void handle(ActionEvent e) {
            				if (!button_tool_custom_brush_size.isSelected()){
                				button_tool_custom_brush_size.fire();
            					
            				}
            				currentTool = 1;
            				väli_selected_block_tool.setText(1+ "" );
            			}      
            		}
            		); 

            button_newUI_gate.setOnAction(
            		new EventHandler<ActionEvent>() {
            			@Override public void handle(ActionEvent e) {
            				if (!button_tool_place.isSelected()){
                				button_tool_place.fire();
            					
            				}
            				currentTool = 4;
            				väli_selected_block_tool.setText(4+ "" );
            			}      
            		}
            		);
            
            button_newUI_clear.setOnAction(
            		new EventHandler<ActionEvent>() {
            			@Override public void handle(ActionEvent e) {
            				if (!button_tool_place.isSelected()){
                				button_tool_place.fire();
            					
            				}
            				currentTool = 2;
            				väli_selected_block_tool.setText(2+ "" );
            			}      
            		}
            		);   
            
            
            final Button button_newUI_resize_p = new Button("Resize P");
            newUI.add(button_newUI_resize_p,1,1);
            
            final Button button_newUI_save_dat = new Button("Save dat");
            
            final Button button_newUI_run = new Button("Make robot lot");
            
            //final Label label_newUI_image = new Label("Chosen image");
            //final Label label_newUI_dat = new Label("Chosen dat");

            
            
            
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
                        	newUI_resize_image_dialog_stage.show();
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
           
            
            
            final TextField väli_pencil_width = new TextField();
            final TextField väli_pencil_height = new TextField();
            final Button nupp_pintsli_suurus_rakenda = new Button("Yes!");
            
            väli_pencil_width.setText(pencil_width + "");
            väli_pencil_height.setText(pencil_height + "");
            
            newUI_resize_p_dialog_gridpane.add(väli_pencil_width, 1, 0);
            newUI_resize_p_dialog_gridpane.add(väli_pencil_height, 2, 0);
            newUI_resize_p_dialog_gridpane.add(nupp_pintsli_suurus_rakenda, 3, 0);
            
            //newUI_resize_p_dialog_stage.show();
            button_newUI_resize_p.setOnAction(
                    new EventHandler<ActionEvent>() {
                        @Override public void handle(ActionEvent e) {
                        	newUI_resize_p_dialog_stage.show();
                        }      
                }
            );
            
            //newUI.add(label_newUI_image,1,2);
            newUI.add(modes,1,2);
            
            //newUI.add(label_newUI_dat,1,3);
            
            newUI.add(button_newUI_save_dat,2,0);
            newUI.add(button_newUI_run,2,1);
            newUI.add(label_newUI_parkingSlots,2,2);
            newUI.add(label_newUI_robotSlots,2,3);
            
            ColumnConstraints column1 = new ColumnConstraints();
            column1.setPercentWidth(33);
            newUI.getColumnConstraints().add(column1);
            newUI.getColumnConstraints().add(column1);
            newUI.getColumnConstraints().add(column1);
            
            
 
            // old UI window
            GridPane oldRoot = new GridPane();
            oldRoot.add(ruudustik, 0, 0);
            oldRoot.add(oldUI_block_toolbar_gridpane, 0, 1);
            
            // vv

            Scene scene3 = new Scene(oldRoot, 226, 26);
            stage3.setTitle("Choose file");
            stage3.setScene(scene3);
            //^^
            
            // vv
            
            root.add(newUI, 0, 3);

            root.add(gridPane_infolabels, 0, 5);
                
            GridPane scrollableContent = new GridPane();
            GridPane scrollableContent2 = new GridPane();
            scrollableContent.add(canvas, 0, 0);
            scrollableContent.add(level, 0, 0);
            //root.add(canvas, 0, 4);
            //root.add(canvas2, 1, 3);

            final ScrollPane scrollable = new ScrollPane();
            scrollable.setHbarPolicy(ScrollBarPolicy.ALWAYS);
            scrollable.setVbarPolicy(ScrollBarPolicy.ALWAYS);
            //scrollable.setContent(level);
            scrollable.setContent(scrollableContent);
            //root.add(level, 0, 4);
            root.add(scrollable, 0, 4);
            //root.add(level2, 1, 4);
            //oldUI_block_toolbar_gridpane.add(label_mouse_cordinates, 21, 0);


            gridPane_infolabels.add(label_mouse_cordinates, 0, 0);
            gridPane_infolabels.add(silt5, 1, 0);
            gridPane_infolabels.getColumnConstraints().add(column1);
            gridPane_infolabels.getColumnConstraints().add(column1);
            //gridPane_infolabels.add(number_of_parking_slots_Silt, 0, 2);
 
               

                
            // open button
            nupp.setOnAction(
            		new EventHandler<ActionEvent>() {

            			@Override public void handle(ActionEvent e) {

            				level.getChildren().removeAll(level.getChildren());
            				level2.getChildren().removeAll(level2.getChildren());
            				number_of_parking_slots_1 = 0;
            				number_of_parking_slots_2 = 0;

            				// read saved file into queue l2
            				try {
            					loadLevel( 1, field_level_filename.getText()  );
            					//button_refresh_image.fire();
            				}
            				// file not found likely, fill queue with generic level instead
            				catch (Exception erind) {

            					//System.out.println("faili pole, teen uue");

            					if (erind instanceof FileNotFoundException){

            						if (field_level_filename.getText().equals("")){
            							silt5.setText("No file. - " + df.format(System.currentTimeMillis() )  );
            							makeGenericLevel();
            						}
            						else{
            							silt5.setText("File not found. So we crated it. - " + df.format(System.currentTimeMillis() )  );
            							makeGenericLevel();
            						}
            						//System.out.println(erind);
            					}
            					else if (erind instanceof WrongFileException){
            						silt5.setText("Not correctly structured file - " + df.format(System.currentTimeMillis() )  );
            						makeGenericLevel();
            						field_level_filename.setText("");
            					}
            					else{
            						silt5.setText("While opening file: "+erind+ " - " + df.format(System.currentTimeMillis() )  );
            						makeGenericLevel();
            					}
            					
            				}
            				// draw level from queue
            				finally {
            					for(int i = 0; i<level_height; i++){
            						for(int j = 0; j<level_width; j++){
            							add_block(   l2.pop(), j, i, level  );         
            						}
            					}

                                stage3.hide();
            				}
            			}
            		}
            		);
           
           
 
            final Rectangle rectBG = new Rectangle(0,0,Block.suurus*level_width,Block.suurus*level_height);
            rectBG.setFill(Color.TRANSPARENT);
            uus.getChildren().add(rectBG);
            
            button_block_size_apply.setOnAction(
            		new EventHandler<ActionEvent>() {
            			@Override public void handle(ActionEvent e) {
            				int uusSuurus =  Integer.parseInt( field_zoom.getText() );
            				vanaSuurus = Block.suurus;
            				Block.suurus = uusSuurus;
            				canvas.setHeight(uusSuurus*level_height);
            				canvas.setWidth(uusSuurus*level_width);
            				rectBG.setX(uusSuurus*level_height);
            				rectBG.setY(uusSuurus*level_width);
            				int x =  Integer.parseInt( pildiLaiusVäli.getText() );
            				int y =  Integer.parseInt( pildiKõrgusVäli.getText() );
            				pildiLaiusVäli.setText(((x/vanaSuurus)*Block.suurus)+"");
            				pildiKõrgusVäli.setText(((y/vanaSuurus)*Block.suurus)+"");

            				button_refresh_image.fire();
            				reset();

            			}
            		}
            		);

            button_reset_image_size.setOnAction(
            		new EventHandler<ActionEvent>() {
            			@Override public void handle(ActionEvent e) {
            				pildiLaiusVäli.setText(level_width*Block.suurus+"");
            				pildiKõrgusVäli.setText(level_height*Block.suurus+"");
            				
            			}      
            		}
            		);
           

            button_newUI_save_dat.setOnAction(
            		new EventHandler<ActionEvent>() {

            			@Override public void handle(ActionEvent e) {
            				try{           

            					saveLevel( 1, field_level_filename.getText()  );

            				}
            				catch(Exception erind){
            					silt5.setText("salvestamine, tekkis: "+erind+ " - " + df.format(System.currentTimeMillis() )  );
                                stage3.show();
                                stage3.setWidth(200);
            				}

            			}
            		}

            		);

           
            button_refresh_image.setOnAction(
            		new EventHandler<ActionEvent>() {

            			@Override public void handle(ActionEvent e) {                                                          

            				try{           
            					pildiLaius = Integer.parseInt(pildiLaiusVäli.getText());
            					pildiKõrgus = Integer.parseInt(pildiKõrgusVäli.getText());
            					gc.setFill(new ImagePattern(new Image(new FileInputStream(new File(field_image_file.getText())))));
            					gc.fillRect(Block.suurus,Block.suurus,pildiLaius,pildiKõrgus);
            					//gc2.setFill(new ImagePattern(new Image(new FileInputStream(new File(field_image_file.getText())))));
            					//gc2.fillRect(Plokk.suurus,Plokk.suurus,pildiLaius,pildiKõrgus);
            				}
            				catch(Exception f){
            					silt5.setText("While opening image: "+f+ " - " + df.format(System.currentTimeMillis() )  );
            					System.out.println(f);
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

                                    	Algorithm gg = new Algorithm();
                                    	gg.run();
                                            //(new Thread(gg)).start();
                                            
                                        }
                                    catch (Exception erind) {
                                        System.out.println(erind + " - algorithm error");
                                    }
                                    stage2.show();
                                }
                        }
                    );
           
           
           
           
           
           
           
            nupp_pintsli_suurus_rakenda.setOnAction(
                        new EventHandler<ActionEvent>() {
                       
                                @Override public void handle(ActionEvent e) {
 
                                        pencil_width = Integer.parseInt(väli_pencil_width.getText());
                                        pencil_height = Integer.parseInt(väli_pencil_height.getText());
                                        button_tool_custom_brush_size.setText(pencil_width + "x" + pencil_height);    
                                        //button_newUI_resize_p.setText("Resize P" + "(" + pencil_width + "x" + pencil_height + ")"); 
                        				newUI_resize_p_dialog_stage.hide();
                                }
                        }
                    );
           
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
            
            Label simUI_field_explaination_robo_speed = new Label("\"Speed\" (lower=faster)");
            simUI.add(simUI_field_explaination_robo_speed, 2, 0);
            final NumberTextField simUI_field_robo_speed = new NumberTextField();
            simUI.add(simUI_field_robo_speed, 2, 1);

            Label simUI_field_explaination_robo_nr = new Label("No. of robots");
            simUI.add(simUI_field_explaination_robo_nr, 3, 0);
            final NumberTextField simUI_field_robo_nr = new NumberTextField();
            simUI.add(simUI_field_robo_nr, 3, 1);
            
            final Button button_simUI_apply = new Button("Apply changes");
            simUI.add(button_simUI_apply, 4, 1);
            button_simUI_apply.setOnAction(
                    new EventHandler<ActionEvent>() {
                   
                            @Override public void handle(ActionEvent e) {
                            	Animation.numberOfCarsIn = Integer.parseInt(simUI_field_entering.getText());
                            	Animation.numberOfCarsOut = Integer.parseInt(simUI_field_exiting.getText());
                            	Animation.botSpeed = Integer.parseInt(simUI_field_robo_speed.getText());
                            	Animation.numberOfbots = Integer.parseInt(simUI_field_robo_nr.getText());
                            }
                    }
                );
            ;
            final Button button_simUI_run = new Button("Run");
            simUI.add(button_simUI_run, 5, 1);
            button_simUI_run.setOnAction(
                    new EventHandler<ActionEvent>() {
                   
                            @Override public void handle(ActionEvent e) {
                            	if(button_simUI_run.getText().equals("Run")){

                            		Animation.stopped = false;
                            		button_simUI_run.setText("Play/Pause");
                                	//Simulation.run();
                            		
                                    //Platform.runLater(sim);
                            		
                                    (new Thread(sim)).start();
                                    
                            	}
                            	else{
                            		//button_simUI_run.setText("Run");
                            		Animation.pause();
                            	}
                            }
                    }
                );
            ;
            simUI_main.add(simUI, 0, 0);
            simUI_main.add(scrollable2, 0, 1);
            
            stage2.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    System.out.println("Stage2 is closing");
                    Animation.kill();
            		button_simUI_run.setText("Run");
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
            //field_image_file.setText("parkla.png");
            
            button_refresh_image.fire();
            
            nupp.fire();

            simUI_field_entering.setText(Animation.numberOfCarsIn+"");
            simUI_field_exiting.setText(Animation.numberOfCarsOut+"");
            simUI_field_robo_speed.setText(Animation.botSpeed+"");
            simUI_field_robo_nr.setText(Animation.numberOfbots+"");
            //button_refresh_image.fire();
            // show main window
            primaryStage.show();
        }
       
        public static void main(String[] args) {
               
                launch(args);
        }
}