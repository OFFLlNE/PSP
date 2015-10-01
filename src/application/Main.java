package application;
       
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.FileInputStream;
import java.io.File;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
 



import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
 
class BlockButton extends ToggleButton{
        public int i;
}
 
 
 
 
 
public class Main extends Application {
       
    final Stage stage2 = new Stage();
   
        //LAIMIS
        static boolean started = false;
        static Plokk ui = null;
        //LAIMIS END
       
        static int currentTool = 0;
        static GridPane level = new GridPane();
        static GridPane level2 = new GridPane();
        public static Deque<Integer> l2 = new ArrayDeque<Integer>();
        final Label silt4 = new Label();
        static Label silt5 = new Label();
        final static Label parkimiskohadSilt = new Label();
 
        static int murphyPosX;
        static int murphyPosY;
 
        static int parkimiskohad1 = 0;
        static int parkimiskohad2 = 0;
       
        static int mouseX;
        static int mouseY;
 
        static int leveli_laius = 240;
        static int leveli_kõrgus = 100;
 
        static int pintsli_laius = 4;
        static int pintsli_kõrgus = 3;
        static int pl = 0;
        static int pk = 0;
       
        static int pildiLaius = leveli_laius*Plokk.suurus;
        static int pildiKõrgus = leveli_kõrgus*Plokk.suurus;
       
        static int vanaSuurus = Plokk.suurus;
       
        static SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss dd.MM.YYYY");
 
        static Plokk[][] plokid = new Plokk[leveli_laius][leveli_kõrgus];
        static Plokk[][] plokid2 = new Plokk[leveli_laius][leveli_kõrgus];
       
        BlockButton button_tool_place;
        BlockButton button_tool_fill;
        BlockButton button_tool_laimis;
        BlockButton button_tool_custom_brush_size;
       
        static ArrayList<String> files = new ArrayList<String>();
 
       
    class Käsitleja implements EventHandler<MouseEvent> {
        Plokk a;
       
        public Käsitleja(Plokk a) {
                this.a = a;
                }
 
                public void handle(MouseEvent me) {
            if (me.getEventType()==MouseEvent.MOUSE_ENTERED)
                {
               
                if (button_tool_fill.isSelected()){
                        //märgiNaabrid(a, Color.AQUA, a.nr, false, 0, level);
                }
                if (a.nr != 6){
                        a.setFill(Color.AQUAMARINE);
                }
                //märgiTeatud(a, Color.AQUA, a.nr, 4);
 
                mouseX = a.x;
                mouseY = a.y;
               
                silt4.setText("(" + a.x + ", " + a.y + ")");
               
 
                        if (button_tool_custom_brush_size.isSelected()){
                               
 
                        for(int i = 0; i< pintsli_laius; i++){
                                for(int j = 0; j< pintsli_kõrgus; j++){
                                       
                                       
                                       
                                        ((Plokk)getNodeFromGridPane(level, a.x+i, a.y+j)).setFill(Color.AQUA);
                                }
                        }
                       
                       
                       
                }      
               
               
                }
           
           
           
           
            else if (me.getEventType()==MouseEvent.MOUSE_CLICKED)
                {
         
               
                if (button_tool_fill.isSelected()){
                        märgiNaabrid(a, Color.AQUA, a.nr, true, currentTool, level);
                }
               
                        //if (a.x != 0 & a.y != 0 & a.x != 59 & a.y != 23 ) {
                                asendaplokk(currentTool, a.x, a.y, level);
                        //      };
                               
                               
                        if (button_tool_laimis.isSelected()){
                               
                                //LAIMIS               
                        if (started == false){
                                        started = true;
                                        ui = a;
                        }
                        else{
                                int algusX = ui.x;
                                int algusY = ui.y;
                                int loppX = a.x;
                                int loppY = a.y;
                                started = false;
                               
                                for(int i = 0; i< Math.abs(ui.x - a.x)+1; i++){
                                        for(int j = 0; j< Math.abs(ui.y - a.y)+1; j++){
                                               
 
                                                        asendaplokk(currentTool, a.x+i, a.y+j, level);
//                                              Parkla weJustGeneratedThisRectangle = new Parkla(xalgus, xlopp, yalgus, ylopp)
                                               
                                }
                                }
                               
                        }
                        //LAIMIS END
                               
                        }      
                       
                        if (button_tool_custom_brush_size.isSelected()){
                                boolean pTäht = false;
 
                                for(int i = 0; i< pintsli_laius; i++){
                                        for(int j = 0; j< pintsli_kõrgus; j++){
                                               
                                               
                                                if (currentTool == 1){
                                                        if (i==0&&j==0){
                                                                asendaplokk(7, a.x+i, a.y+j, level);
                                                        }
                                                        else if (i==0&&j==pintsli_kõrgus-1){
                                                                asendaplokk(10, a.x+i, a.y+j, level);
                                                        }
                                                        else if (i==pintsli_laius-1&&j==0){
                                                                asendaplokk(8, a.x+i, a.y+j, level);
                                                        }
                                                        else if (i==pintsli_laius-1&&j==pintsli_kõrgus-1){
                                                                asendaplokk(9, a.x+i, a.y+j, level);
                                                        }
                                                       
                                                        else if (i==0|| i==pintsli_laius-1){
                                                                asendaplokk(11, a.x+i, a.y+j, level);
                                                        }
                                                        else if(j==0||j==pintsli_kõrgus-1){
                                                                asendaplokk(12, a.x+i, a.y+j, level);
                                                        }
                                                        else if (!pTäht){
                                                                pTäht=true;
                                                                asendaplokk(1, a.x+i, a.y+j, level);
                                                        }
                                                        else{
                                                                asendaplokk(13, a.x+i, a.y+j, level);
                                                        }
                                                       
                                                       
                                                }
                                                else{
                                               
                                                        asendaplokk(currentTool, a.x+i, a.y+j, level);
//                                              Parkla weJustGeneratedThisRectangle = new Parkla(xalgus, xlopp, yalgus, ylopp)
                                                }
                                }
                                }
                               
                        }      
                       
                       
                               
                }
       
             
            else {
                mouseX = a.x;
                mouseY = a.y;      
               
                if (button_tool_fill.isSelected() && a.nr != 6){
                        märgiNaabrid(a, a.see, a.nr, false, 0, level);
                }
 
                //märgiTeatud(a, a.see, a.nr, 4);
                if (a.nr != 6){
                        a.setFill(a.see);
                }
               
                        if (button_tool_custom_brush_size.isSelected()){
                               
 
                        for(int i = 0; i< pintsli_laius; i++){
                                for(int j = 0; j< pintsli_kõrgus; j++){
                                        ((Plokk)getNodeFromGridPane(level, a.x+i, a.y+j)).setFill(((Plokk)getNodeFromGridPane(level, a.x+i, a.y+j)).see);
                                }
                        }
                }
               
               
            }
        }      
    }
       
   
    class Käsitleja2 implements EventHandler<MouseEvent> {
        GridPane a;
       
        public Käsitleja2(GridPane a) {
                this.a = a;
                }
 
                public void handle(MouseEvent me) {
            if (me.getEventType()==MouseEvent.MOUSE_DRAGGED)
                {
               
                        //if ((int)me.getX()/Plokk.suurus != 0 & (int)me.getY()/Plokk.suurus != 0 & (int)me.getX()/Plokk.suurus != 59 & (int)me.getY()/Plokk.suurus != 23 ) {
 
                        //((Plokk) getNodeFromGridPane(level, (int)me.getX()/Plokk.suurus, (int)me.getY()/Plokk.suurus)  ).muuda(currentTool);
                        asendaplokk(currentTool, (int)me.getX()/Plokk.suurus, (int)me.getY()/Plokk.suurus, level);
                        //};
 
                silt4.setText("(" + (int)me.getX()/Plokk.suurus + ", " + (int)me.getY()/Plokk.suurus + ")");
 
               
               
                }
            else {
               
            }
        }      
    }
       
//    //LAIMIS
//    Class ModifiedParkla{
//      oldxstart = ...
//      oldxend = ...
//      oldystart = ...
//      oldyend = ...
//     
//      newxstart = ...
//              newxend = ...
//              newystart = ...
//              newyend = ...
//    }
//    
//    updateObject(modifiedParkla)
//    
//    updateObject(object){
//      modifiedParkla.getOldCubes.SetInvisible;
//      modifiedParkla.getNewCubes.SetParkla;
//    }
    //LAIMIS END
   
    public static Deque<Plokk> märgitavad = new ArrayDeque<Plokk>();
   
    public static void märgiNaabrid(Plokk a, Paint b, int eelmine, boolean kasAsendada, int tool, GridPane lvl){
        if (a.nr != eelmine){
 
                //System.out.println("ok!");
        }
       
        try{
        Plokk W = naaberW(a.x, a.y, lvl);
        Plokk S = naaberS(a.x, a.y, lvl);
        Plokk E = naaberE(a.x, a.y, lvl);
        Plokk N = naaberN(a.x, a.y, lvl);
 
        if (a.nr != 6){
                a.setFill(b);
        }
       
        if (kasAsendada){
                //System.out.println("värvida: " + a.x + ", " + a.y);
                        asendaplokk(tool, a.x, a.y, lvl);
        }
       
        if (!kasAsendada){
                if (!W.getFill().equals(b) && W.nr == eelmine && W != null) { märgitavad.push(W);};
                if (!S.getFill().equals(b) && S.nr == eelmine && S != null  ) {         märgitavad.push(S);};
                if (!E.getFill().equals(b) && E.nr == eelmine && E != null  ) {         märgitavad.push(E);};
                if (!N.getFill().equals(b) && N.nr == eelmine && N != null  ) {         märgitavad.push(N);};
        }
        else{
                if (W.nr != a.nr && W.nr == eelmine && W != null) { märgitavad.push(W);};
                if (S.nr != a.nr  &&  S.nr == eelmine && S != null  ) {         märgitavad.push(S);};
                if (E.nr != a.nr &&  E.nr == eelmine && E != null  ) {          märgitavad.push(E);};
                if (N.nr != a.nr && N.nr == eelmine && N != null  ) {           märgitavad.push(N);};
        }
 
 
       
                märgiNaabrid(märgitavad.pop(), b, eelmine, kasAsendada, tool, lvl);
        }
       
                catch(Exception e){
                	new Alert(Alert.AlertType.ERROR, "Tere", ButtonType.OK);
                        //System.out.println(e);
                	märgitavad.clear();
                        return;
                }
               
               
               
               
       
               
    }
   
   
   
   
    public static Plokk naaberS(int x, int y, GridPane lvl){
        return (Plokk)getNodeFromGridPane(lvl, x, y+1);
       
    }
   
    public static Plokk naaberW(int x, int y, GridPane lvl){
        return (Plokk)getNodeFromGridPane(lvl, x+1, y);
       
    }
   
   
    public static Plokk naaberE(int x, int y, GridPane lvl){
        return (Plokk)getNodeFromGridPane(lvl, x-1, y);
       
    }
   
    public static Plokk naaberN(int x, int y, GridPane lvl){
        return (Plokk)getNodeFromGridPane(lvl, x, y-1);
       
    }
   
 
   
    public static Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
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
   
 
        public static String loeLevel(int mitmes, String fail) throws  FileNotFoundException, IOException, ClassNotFoundException {
                silt5.setText("Laen...");
               
                RandomAccessFile r = new RandomAccessFile("levels/"+fail, "r");
 
 
               
               
                mitmes--;
               
 
                r.skipBytes(mitmes*1536);
 
               
               
                for(int i = 1; i<=leveli_kõrgus; i++){
                        for(int j = 1; j<=leveli_laius; j++){
                                int a = r.read();
                                //System.out.println("("+ i + ", " + j + ") - " + a);
                       
                                l2.add(a);
                       
                        }
                        //System.out.println();
                       
                }
               
                r.close();
               
                silt5.setText("Laetud! - " + df.format(System.currentTimeMillis() )  );
               
 
           
                return "";
        }
       
       
       
        public static String salveLevel(int mitmes, String fail) throws  FileNotFoundException, IOException, ClassNotFoundException {
                silt5.setText("Salvestan...");
                RandomAccessFile r = new RandomAccessFile("levels/"+fail, "rw");
                //mitmes--;
                System.out.println("salvestan");
                //r.skipBytes(mitmes*1536);
                for(int i = 0; i<=leveli_kõrgus-1; i++){
 
                        for(int j = 0; j<=leveli_laius-1; j++){
                               
                               
                               
                        //System.out.println("("+ i + ", " + j + ") - " + a);
                        //System.out.println((Plokk) getNodeFromGridPane(level, j, i));
                        r.write(         ((Plokk) getNodeFromGridPane(level, j, i)).nr              );
                       
                        }
                        //System.out.println();
                       
                }
               
               
                r.close();
                silt5.setText("Salvestatud! - " + df.format(System.currentTimeMillis() )  );
 
                return "";
        }
       
       
 
       
       
       
        public void lisaplokk(int nr, int j, int i, GridPane lvl){
                Plokk r = new Plokk(   nr, j, i  );
               
                if (lvl == level ){
                        r.addEventHandler(MouseEvent.MOUSE_ENTERED, new Käsitleja(r));
                        r.addEventHandler(MouseEvent.MOUSE_CLICKED, new Käsitleja(r));
                        r.addEventHandler(MouseEvent.MOUSE_EXITED, new Käsitleja(r));
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
                                parkimiskohad1++;
                        }else{
                                parkimiskohad2++;
                }
               
 
                        parkimiskohadSilt.setText("Parkimiskohti joonistusel: " + parkimiskohad1 + ", parkimiskohti robotparklas: " + parkimiskohad2 + "");
                }
               
        }
       
       
       
        public static void asendaplokk(int nr, int j, int i, GridPane lvl){
                Plokk muudetav = (Plokk) getNodeFromGridPane(lvl, j, i);
               
                if (nr == 1){
                        if (lvl == level){
                                parkimiskohad1++;
                        }else{
                                parkimiskohad2++;
                        }
                }
               
                if (muudetav.nr == 1){
                        if (lvl == level){
                                parkimiskohad1--;
                        }else{
                                parkimiskohad2--;
                }
                }
               
                if (muudetav.nr != 6){
                       
 
                        muudetav.muuda(nr);
               
               
               
               
                }
                parkimiskohadSilt.setText("Parkimiskohti joonistusel: " + parkimiskohad1 + ", parkimiskohti robotparklas: " + parkimiskohad2 + "");
               
        }
       
       
        public void readTextures() throws  FileNotFoundException{
                System.out.println("reading textures...");
                Plokk.textures[0] = Color.BLACK;
               
                for (int i = 1; i<14; i++){
                        Plokk.textures[i] = new ImagePattern(new Image(new FileInputStream(new File(i + ".png"))));
                }
               
 
                System.out.println("Done!");
        }
       
        @Override
        public void start(final Stage primaryStage) throws  FileNotFoundException, IOException, ClassNotFoundException {
               
                readTextures();
               
                Runtime runtime = Runtime.getRuntime();
            NumberFormat format = NumberFormat.getInstance();
            StringBuilder sb = new StringBuilder();
            long maxMemory = runtime.maxMemory();
            long allocatedMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
            sb.append("\nfree memory: " + format.format(freeMemory / 1024) + "\n");
            sb.append("allocated memory: " + format.format(allocatedMemory / 1024) + "\n");
            sb.append("max memory: " + format.format(maxMemory / 1024) + "\n");
            sb.append("total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024) + "\n\n");
            System.out.println(sb);
             
                GridPane root = new GridPane();
               
                GridPane ruudustik = new GridPane();
            final TextField väli = new TextField();
            Label silt = new Label("Level: ");
            final Button nupp = new Button("Open");
            final TextField väli_level_filename = new TextField();
           
            Label label_tool = new Label("Tool: ");
            final TextField väli_selected_block_tool = new TextField();
           
            final TextField väli_pintsli_laius = new TextField();
            final TextField väli_pintsli_kõrgus = new TextField();
            final Button nupp_pintsli_suurus_rakenda = new Button("nupp_pintsli_suurus_rakenda");
           
           
            Button button_save = new Button("Save");
           
 
                 ToggleGroup tools = new ToggleGroup();
 
                button_tool_place = new BlockButton();
                button_tool_fill = new BlockButton();
                button_tool_laimis = new BlockButton();
                button_tool_custom_brush_size = new BlockButton();
                button_tool_place.setText("Place");
                button_tool_fill.setText("Fill");
                button_tool_laimis.setText("Laimis");
                button_tool_custom_brush_size.setText(pintsli_laius + "x" + pintsli_kõrgus);
                button_tool_place.setToggleGroup(tools);
                button_tool_fill.setToggleGroup(tools);
                button_tool_laimis.setToggleGroup(tools);
                button_tool_custom_brush_size.setToggleGroup(tools);
                button_tool_place.fire();
 
            Label label_block_size = new Label("Block size: ");
            Button button_block_size_apply = new Button("Use size. You must reload level.");
            final TextField väli_block_size = new TextField();
           
 
                GridPane gridPane_infolabels = new GridPane();
                GridPane ruudustik2 = new GridPane();
                GridPane ruudustik3 = new GridPane();
               
                 ToggleGroup BlockButtons = new ToggleGroup();
 
                // plokkide nupud
                for (int i = 0; i<14; i++){
                       
                        if (i < 41 ){
                                final BlockButton temp = new BlockButton();
                                temp.i = i;
                                //ruudustik2.getChildren().add(temp);
                                //StackPane.setMargin(temp, new Insets(0, 0, 0, 0));
       
                                ruudustik2.add(temp, i, 0);
                               
                                Rectangle temp2 = new Rectangle();
                               
                                temp2.setX(0);
                                temp2.setY(0);
                                temp2.setWidth(16
                                                //Plokk.suurus
                                                );
                                temp2.setHeight(16
                                                //Plokk.suurus
                                                );
                                temp2.setFill(Plokk.textures[i]);
                               
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
                           
//                              Label rl = new Label(i + "");
//                              rl.setMouseTransparent(true);
//                              ruudustik2.add(rl, i, 0);
                   
                        }
                       
                }
           
               
 
            Label label_bgimage_x = new Label("PiltX: ");
            final TextField pildiLaiusVäli = new TextField();
            Label label_bgimage_y = new Label("PiltY: ");
            final TextField pildiKõrgusVäli = new TextField();
            Label pildifailSilt = new Label("Pilt: ");
            final TextField pildifailVäli = new TextField();
            final Button button_recalculate_image = new Button("Arvuta_pildi_kordinaadid");
            final Button button_refresh_image = new Button("Set");
            final Button button_make_robot_lot = new Button("Make robot lot");
               
            ruudustik3.add(label_bgimage_x, 1, 0);
            ruudustik3.add(pildiLaiusVäli, 2, 0);
            ruudustik3.add(label_bgimage_y, 3, 0);
            ruudustik3.add(pildiKõrgusVäli, 4, 0);
            ruudustik3.add(pildifailSilt, 5, 0);
            ruudustik3.add(pildifailVäli, 6, 0);
            ruudustik3.add(button_recalculate_image, 10, 0);
            ruudustik3.add(button_refresh_image, 11, 0);
            ruudustik3.add(button_make_robot_lot, 12, 0);
           
            pildiLaiusVäli.setText(pildiLaius+"");
            pildiKõrgusVäli.setText(pildiKõrgus+"");
            pildifailVäli.setText("parkla.png");
 
            väli_pintsli_laius.setText(pintsli_laius + "");
            väli_pintsli_kõrgus.setText(pintsli_kõrgus + "");
 
                final Canvas canvas = new Canvas(1920,1080);
                final GraphicsContext gc = canvas.getGraphicsContext2D();
               
            väli.setText("1");
            väli_level_filename.setText("levels.dat");
            väli_block_size.setText(Plokk.suurus + "");
 
            // programmi toolbar
            ruudustik.add(silt, 1, 0);
            ruudustik.add(väli_level_filename, 2, 0);
            //ruudustik.add(väli, 3, 0);
            ruudustik.add(nupp, 4, 0);
            ruudustik.add(button_save, 6, 0);
 
            ruudustik.add(label_tool, 15, 0);
            ruudustik2.add(väli_selected_block_tool, 20, 0);
 
            ruudustik.add(label_block_size, 12, 0);
            ruudustik.add(väli_block_size, 13, 0);
            ruudustik.add(button_block_size_apply, 14, 0);
           
            ruudustik.add(button_tool_place, 16, 0);
            ruudustik.add(button_tool_fill, 17, 0);
            ruudustik.add(button_tool_laimis, 18, 0);
            ruudustik.add(button_tool_custom_brush_size, 19, 0);
 
            ruudustik.add(väli_pintsli_laius, 20, 0);
            ruudustik.add(väli_pintsli_kõrgus, 21, 0);
            ruudustik.add(nupp_pintsli_suurus_rakenda, 22, 0);
           
                root.add(ruudustik, 0, 0);
                root.add(gridPane_infolabels, 0, 1);
                root.add(ruudustik2, 0, 2);
                root.add(ruudustik3, 0, 3);
                root.add(canvas, 0, 4);
                //root.add(canvas2, 1, 3);
                root.add(level, 0, 4);
                //root.add(level2, 1, 4);
                ruudustik2.add(silt4, 21, 0);
               
               
                gridPane_infolabels.add(silt5, 0, 1);
                gridPane_infolabels.add(parkimiskohadSilt, 0, 2);
 
               
 
           
            nupp.setOnAction(
                new EventHandler<ActionEvent>() {
               
                        @Override public void handle(ActionEvent e) {
                               
                                level.getChildren().removeAll(level.getChildren());
                                level2.getChildren().removeAll(level2.getChildren());
                                parkimiskohad1 = 0;
                                parkimiskohad2 = 0;
                               
                                // read saved file into queue l2
                            try {
                                        loeLevel( Integer.parseInt(väli.getText() ), väli_level_filename.getText()  );
                                        //button_refresh_image.fire();
                                }
                            // file not found likely, fill queue with generic level instead
                            catch (Exception erind) {
 
                                //System.out.println("faili pole, teen uue");
 
                                        silt5.setText(erind+ " - " + df.format(System.currentTimeMillis() )  );
                                       
                                        for(int i = 1; i<=leveli_kõrgus; i++){
                                                for(int j = 1; j<=leveli_laius; j++){
                                                        if (i == 1 || i==leveli_kõrgus || j==1 || j==leveli_laius){
                                                                l2.add(6);
                                                        }
                                                        else{
                                                                l2.add(2);
                                                        }
                                               
                                                }
                                                //System.out.println();
                                               
                                        }
 
                                //System.out.println(erind);
                            }
                            // draw level from queue
                            finally {
                                        for(int i = 0; i<leveli_kõrgus; i++){
                                                for(int j = 0; j<leveli_laius; j++){
                                                        lisaplokk(   l2.pop(), j, i, level  );         
                                                }
                                        }
                            }
                        }
                }
            );
           
           
 
           
            button_block_size_apply.setOnAction(
                        new EventHandler<ActionEvent>() {
                                @Override public void handle(ActionEvent e) {
                                        int uusSuurus =  Integer.parseInt( väli_block_size.getText() );
                                        vanaSuurus = Plokk.suurus;
                                        Plokk.suurus = uusSuurus;
                                        if (uusSuurus > vanaSuurus){
                                        canvas.setHeight(uusSuurus*leveli_kõrgus);
                                        }
                                    }      
                        }
            );
           
            button_recalculate_image.setOnAction(
                        new EventHandler<ActionEvent>() {
                                @Override public void handle(ActionEvent e) {
                                        int x =  Integer.parseInt( pildiLaiusVäli.getText() );
                                        int y =  Integer.parseInt( pildiKõrgusVäli.getText() );
                                        pildiLaiusVäli.setText(((x/vanaSuurus)*Plokk.suurus)+"");
                                        pildiKõrgusVäli.setText(((y/vanaSuurus)*Plokk.suurus)+"");
                                }      
                        }
            );
           
           
            button_save.setOnAction(
                        new EventHandler<ActionEvent>() {
                       
                                @Override public void handle(ActionEvent e) {
                                                                try{           
                                                                       
                                                                        //System.out.println( Integer.parseInt(väli.getText() ));
                                                                        //System.out.println(väli_level_filename.getText());
                                                                       
                                                                       
                                                salveLevel( Integer.parseInt(väli.getText() ), väli_level_filename.getText()  );
                                                                }
                                                                catch(Exception erind){
                                                                silt5.setText("salvestamine, tekkis: "+erind+ " - " + df.format(System.currentTimeMillis() )  );
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
                                            gc.setFill(new ImagePattern(new Image(new FileInputStream(new File(pildifailVäli.getText())))));
                                            gc.fillRect(Plokk.suurus,Plokk.suurus,pildiLaius,pildiKõrgus);
                                            //gc2.setFill(new ImagePattern(new Image(new FileInputStream(new File(pildifailVäli.getText())))));
                                            //gc2.fillRect(Plokk.suurus,Plokk.suurus,pildiLaius,pildiKõrgus);
                                }
                                catch(Exception f){
                                        System.out.println(f);
                                }
                                       
                                   
                 
                                }
                        }
       
                    );
 
           
            button_make_robot_lot.setOnAction(
                        new EventHandler<ActionEvent>() {
                       
                                @Override public void handle(ActionEvent e) {
                                       
                                        level2.getChildren().removeAll(level2.getChildren());
                                       
                                        parkimiskohad2 = 0;
                                       
                                       
                                    try {
                                        int gateX = (int) Double.POSITIVE_INFINITY;
                                                int gateY = (int) Double.POSITIVE_INFINITY;
                                                pl = pintsli_laius;
                                                pk = pintsli_kõrgus;
                                       
                                        for(int i = 0; i<leveli_laius; i++){
                                                        for(int j = 0; j<leveli_kõrgus; j++){
                                                               
                                                               
                                                                int uusP = ((Plokk) getNodeFromGridPane(level, i, j)).nr; // milline on vana plokk kohas i, j
                                                                //multiple entries - per 9 piece batches
                                                               
                                                                if(uusP == 4){
                                                                        if(gateX > i){
                                                                                gateX = i;
                                                                        }
                                                                        if(gateY > j){
                                                                                gateY = j;
                                                                        }
                                                                }
                                                                lisaplokk( uusP  , i, j, level2  );      // lisab uude levelisse samasse kohta samasuguse ploki
                                                               
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
                                    catch (Exception erind) {
                                        System.out.println(erind + " - laimis is a pleb and cant code for s**t");
                                    }
                                    stage2.show();
                                }
                        }
                    );
           
           
           
           
           
           
           
            nupp_pintsli_suurus_rakenda.setOnAction(
                        new EventHandler<ActionEvent>() {
                       
                                @Override public void handle(ActionEvent e) {
 
                                        pintsli_laius = Integer.parseInt(väli_pintsli_laius.getText());
                                        pintsli_kõrgus = Integer.parseInt(väli_pintsli_kõrgus.getText());
                                        button_tool_custom_brush_size.setText(pintsli_laius + "x" + pintsli_kõrgus);    
                                }
                        }
                    );
           
            level.addEventHandler(MouseEvent.MOUSE_DRAGGED, new Käsitleja2(level));
           
            Scene scene2 = new Scene(level2,1920,900);
                stage2.setTitle("Simulatsioon");
            stage2.setScene(scene2);
           
            nupp.fire();
            button_refresh_image.fire();
            Scene scene = new Scene(root,1920,900);
               
            primaryStage.setScene(scene);
                primaryStage.show();
        }
       
       
       
        //LAIMIS START
       
        public int getType(int x, int y){
                return ((Plokk) getNodeFromGridPane(level2, x, y)).nr;
    }
       
        public boolean containsAll(int x, int y, int type){
                for(int i = 0; i<pl; i++){
                        for(int j = 0; j<pk; j++){
                                if(getType(x+i, y+j) != type){
                                        return false;
                                }
                        }
                }
                return true;
    }
       
        public void go(int x, int y, int gateX, int gateY, int counter){
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
                                                addParking(gateX+x+(pl*i)*t2, gateY+y+(pk*i)*t1, level2);
                                                if(containsAll(gateX+x+(pl*2*i)*t2, gateY+y+(pk*2*i)*t1, 2) == true){
                                                        addParking(gateX+x+(pl*2*i)*t2, gateY+y+(pk*2*i)*t1, level2);
                                                        if(containsAll(gateX+x+(pl*3*i)*t2, gateY+y+(pk*3*i)*t1, 2) == true){
                                                                addParking(gateX+x+(pl*3*i)*t2, gateY+y+(pk*3*i)*t1, level2);
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
                                        addParking(gateX+(x)+(pl*i)*t2, gateY+(y)+(pk*i)*t1, level2);
                                        if(containsAll(gateX+(x*2)+(pl*i)*t2, gateY+(y*2)+(pk*i)*t1, 2) == true){
                                                addParking(gateX+(x*2)+(pl*i)*t2, gateY+(y*2)+(pk*i)*t1, level2);
                                                if(containsAll(gateX+(x*3)+(pl*i)*t2, gateY+(y*3)+(pk*i)*t1, 2) == true){
                                                        addParking(gateX+(x*3)+(pl*i)*t2, gateY+(y*3)+(pk*i)*t1, level2);
                                                }
                                        }
                                }
                        }
                }
    }
       
        //LAIMIS END
 
        public void addParking(int x, int y, GridPane lvl){
        boolean pTäht = false;
            for(int i = 0; i< pintsli_laius; i++){
                    for(int j = 0; j< pintsli_kõrgus; j++){
                           
                                    if (i==0&&j==0){
                                            asendaplokk(7, x+i, y+j, lvl);
                                    }
                                    else if (i==0&&j==pintsli_kõrgus-1){
                                            asendaplokk(10, x+i, y+j, lvl);
                                    }
                                    else if (i==pintsli_laius-1&&j==0){
                                            asendaplokk(8, x+i, y+j, lvl);
                                    }
                                    else if (i==pintsli_laius-1&&j==pintsli_kõrgus-1){
                                            asendaplokk(9, x+i, y+j, lvl);
                                    }
                                   
                                    else if (i==0|| i==pintsli_laius-1){
                                            asendaplokk(11, x+i, y+j, lvl);
                                    }
                                    else if(j==0||j==pintsli_kõrgus-1){
                                            asendaplokk(12, x+i, y+j, lvl);
                                    }
                                    else if (!pTäht){
                                            pTäht=true;
                                            asendaplokk(1, x+i, y+j, lvl);
                                    }
                                    else{
                                            asendaplokk(13, x+i, y+j, lvl);
                                    }
                                   
                    }
            }
        }
   
        public void addRoad(int x, int y){
                for(int i = 0; i<pl; i++){
                        for(int j = 0; j<pk; j++){
                                asendaplokk(3, x+i, y+j, level2);
                        }
                }
        }
       
   
        public static void main(String[] args) {
               
                launch(args);
        }
}