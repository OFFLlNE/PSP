package application;

import java.util.ArrayDeque;
import java.util.Deque;

import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Animation implements Runnable {

	protected static int numberOfCarsIn = 10;
	protected static int numberOfCarsOut = 10;
	protected static int botSpeed = 200;
	protected static int numberOfbots = 4;
	public static boolean paused = false;
	public static boolean stopped = false;
	

	// IMPORTANT DETAILS:
	// commands that are issued while a unit is excecuting other command are ignored
	// commands that would result in unit being out of bounds are also ignored. It will not move at all instead of stopping at the wall
	// example movement is choppy because the same commands are called over and over again.
	public void run(){
		while (!stopped){
			if (!paused){
				// TODO tick


			}
			// wait
    		try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	// will be changed. returns amount of time the movement is supposed to take. 
	public static int speedFormula(int distance){
		return distance*botSpeed;
	}
	
	public static void toggleColor(Bot b){
		if(b.getFill()==Color.BLUE){
			b.setFill(Color.BROWN);
		}
		else{
			b.setFill(Color.BLUE);
		}
	}
	
   	public static void moveRight(final Bot bot, int distance){
   		final double futureX = bot.x+(Plokk.suurus*distance*Main.pencil_width);
   		
		if (!bot.isBusy && futureX < Main.level_width*Plokk.suurus){
			System.out.println("[Understood] Moving RIGHT by "+distance+". I am here:  ("+bot.x/Plokk.suurus + ", "+bot.y/Plokk.suurus +")");
			bot.isBusy=true;
	        Path path = new Path();
	        path.getElements().add(new MoveTo(bot.x+bot.getWidth()/2,   bot.y+bot.getHeight()/2));
	        path.getElements().add(new LineTo(futureX+bot.getWidth()/2, bot.y+bot.getHeight()/2));
	        PathTransition pathTransition = new PathTransition();
	        
	        pathTransition.setDuration(Duration.millis(speedFormula(distance)));
	        pathTransition.setPath(path);
	        pathTransition.setNode(bot);
	        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
	        pathTransition.setCycleCount(1);
	        pathTransition.play();

	        pathTransition.onFinishedProperty().set(new EventHandler<ActionEvent>() {
	            @Override 
	            public void handle(ActionEvent actionEvent) {

	    	        bot.x= futureX ;
	    	        bot.isBusy=false;
	            }
	        });
	        
		}
		else{

			System.out.println("[Ignored] Not moving RIGHT by "+distance+". I am here:  ("+bot.x/Plokk.suurus + ", "+bot.y/Plokk.suurus +")");
		}
	}
   	public static void moveLeft(final Bot bot, int distance){
   		final double futureX = bot.x-(Plokk.suurus*distance*Main.pencil_width);
   		
		if (!bot.isBusy && futureX >0){
			System.out.println("[Understood] Moving LEFT by "+distance+". I am here:  ("+bot.x/Plokk.suurus + ", "+bot.y/Plokk.suurus +")");
			bot.isBusy=true;
	        Path path = new Path();
	        path.getElements().add(new MoveTo(bot.x+bot.getWidth()/2,bot.y+bot.getHeight()/2));
	        path.getElements().add(new LineTo(futureX+bot.getWidth()/2, bot.y+bot.getHeight()/2));
	        PathTransition pathTransition = new PathTransition();
	        
	        pathTransition.setDuration(Duration.millis(speedFormula(distance)));
	        pathTransition.setPath(path);
	        pathTransition.setNode(bot);
	        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
	        pathTransition.setCycleCount(1);
	        pathTransition.play();

	        pathTransition.onFinishedProperty().set(new EventHandler<ActionEvent>() {
	            @Override 
	            public void handle(ActionEvent actionEvent) {

	    	        bot.x= futureX ;
	    	        bot.isBusy=false;
	            }
	        });
	        
		}
		else{

			System.out.println("[Ignored] Not moving LEFT by "+distance+". I am here:  ("+bot.x/Plokk.suurus + ", "+bot.y/Plokk.suurus +")");
		}
	}   	
   	public static void moveUp(final Bot bot, int distance){
   		final double futureY = bot.y-(Plokk.suurus*distance*Main.pencil_height);
   		
		if (!bot.isBusy && futureY > 0){
			System.out.println("[Understood] Moving UP by "+distance+". I am here:  ("+bot.x/Plokk.suurus + ", "+bot.y/Plokk.suurus +")");
			bot.isBusy=true;
	        Path path = new Path();
	        path.getElements().add(new MoveTo(bot.x+bot.getWidth()/2, bot.y+bot.getHeight()/2));
	        path.getElements().add(new LineTo(bot.x+bot.getWidth()/2, futureY+bot.getHeight()/2));
	        PathTransition pathTransition = new PathTransition();
	        
	        pathTransition.setDuration(Duration.millis(speedFormula(distance)));
	        pathTransition.setPath(path);
	        pathTransition.setNode(bot);
	        pathTransition.setOrientation(PathTransition.OrientationType.NONE);
	        pathTransition.setCycleCount(1);
	        pathTransition.play();

	        pathTransition.onFinishedProperty().set(new EventHandler<ActionEvent>() {
	            @Override 
	            public void handle(ActionEvent actionEvent) {

	    	        bot.y= futureY ;
	    	        bot.isBusy=false;
	            }
	        });
	        
		}
		else{

			System.out.println("[Ignored] Not moving UP by "+distance+". I am here:  ("+bot.x/Plokk.suurus + ", "+bot.y/Plokk.suurus +")");
		}
	}   	
   	

	public static void moveDown(final Bot bot, int distance){
   		final double futureY = bot.y+(Plokk.suurus*distance*Main.pencil_height);
   		
		if (!bot.isBusy && futureY < Main.level_height*Plokk.suurus){
			System.out.println("[Understood] Moving DOWN by "+distance+". I am here:  ("+bot.x/Plokk.suurus + ", "+bot.y/Plokk.suurus +")");
			bot.isBusy=true;
	        Path path = new Path();
	        path.getElements().add(new MoveTo(bot.x+bot.getWidth()/2, bot.y+bot.getHeight()/2));
	        path.getElements().add(new LineTo(bot.x+bot.getWidth()/2, futureY+bot.getHeight()/2));
	        PathTransition pathTransition = new PathTransition();
	        
	        pathTransition.setDuration(Duration.millis(speedFormula(distance)));
	        pathTransition.setPath(path);
	        pathTransition.setNode(bot);
	        pathTransition.setOrientation(PathTransition.OrientationType.NONE);
	        pathTransition.setCycleCount(1);
	        pathTransition.play();

	        pathTransition.onFinishedProperty().set(new EventHandler<ActionEvent>() {
	            @Override 
	            public void handle(ActionEvent actionEvent) {

	    	        bot.y= futureY ;
	    	        bot.isBusy=false;
	            }
	        });
	        
		}
		else{

			System.out.println("[Ignored] Not moving DOWN by "+distance+". I am here:  ("+bot.x/Plokk.suurus + ", "+bot.y/Plokk.suurus +")");
		}
	}
   	
   	
	
	public static void pause(){
		if (paused){
			paused = false;
		}
		else{
			paused = true;
		}
	}
	public static void kill(){
		System.out.println("killing thread");
		stopped = true;
	}
}
