package application;

import java.util.LinkedList;
import java.util.Queue;

import javafx.animation.PathTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Bot extends Rectangle{
    public boolean isBusy;
    private double x;
    private double y;
    
    public Queue<Integer> orders_dist = new LinkedList<Integer>();
	public Queue<String> orders_comm = new LinkedList<String>();

	public Bot(double arg0, double arg1, double arg2, double arg3) {
		super(arg0, arg1, arg2, arg3);
		this.isBusy = false;
		this.x = arg0;
		this.y = arg1;
	}
	

    public int getRobotX() {
		return (int)this.x/Block.suurus;
	}

	public int getRobotY() {
		return (int)this.y/Block.suurus;
	}
	
	public void toggleColor(){
		if (!this.isBusy){
			//System.out.println("[Understood] Respraying. I am here:  ("+this.x/Block.suurus + ", "+this.y/Block.suurus +")");
			this.isBusy=true;

			if(this.getFill()==Block.textures[49]){
				this.setFill(Block.textures[48]);
			}
			else{
				this.setFill(Block.textures[49]);
			}
			this.isBusy=false;
	        if (!this.orders_comm.isEmpty()){
				String k = this.orders_comm.poll();
				int d = this.orders_dist.poll();
				
				if (k.equals("R")){
					this.moveRight(d);
				}
				else if (k.equals("U")){
					this.moveUp(d);
				}
				else if (k.equals("L")){
					this.moveLeft(d);
				}
				else if (k.equals("D")){
					this.moveDown(d);
				}
				else if (k.equals("C")){
					this.toggleColor();
				}
	        }
			
		}
		else{
			//System.out.println("[Queing] Not respraying. I am here:  ("+this.x/Block.suurus + ", "+this.y/Block.suurus +")");
		
			this.orders_dist.add(0);
			this.orders_comm.add("C");
		}
	}
	

	
   	public void moveRight(int distance){
   		
		if (!this.isBusy){
			//System.out.println("[Understood] Moving RIGHT by "+distance+". I am here:  ("+this.x/Block.suurus + ", "+this.y/Block.suurus +")");
			this.isBusy=true;
	        Path path = new Path();
	        
	        //
	   		final double futureX = this.x+(Block.suurus*distance);
	        path.getElements().add(new MoveTo(this.x+this.getWidth()/2,   this.y+this.getHeight()/2));
	        path.getElements().add(new LineTo(futureX+this.getWidth()/2, this.y+this.getHeight()/2));
	        //
	        
	        
	        PathTransition pathTransition = new PathTransition();
	        
	        pathTransition.setDuration(Duration.millis(Animation.speedFormula(distance)));
	        pathTransition.setPath(path);
	        pathTransition.setNode(this);
	        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
	        pathTransition.setCycleCount(1);
	        pathTransition.play();

	        pathTransition.onFinishedProperty().set(new EventHandler<ActionEvent>() {
	            @Override 
	            public void handle(ActionEvent actionEvent) {

	    	        x= futureX ;
	    	        isBusy=false;
	    	        if (!orders_comm.isEmpty()){
						String k = orders_comm.poll();
						int d = orders_dist.poll();
						
						if (k.equals("R")){
							moveRight(d);
						}
						else if (k.equals("U")){
							moveUp(d);
						}
						else if (k.equals("L")){
							moveLeft(d);
						}
						else if (k.equals("D")){
							moveDown(d);
						}
						else if (k.equals("C")){
							toggleColor();
						}
	    	        }
	            }
	        });
	        
		}
		else{
			
			//System.out.println("[Queing] Not moving RIGHT by "+distance+". I am here:  ("+this.x/Block.suurus + ", "+this.y/Block.suurus +")");
			this.orders_dist.add(distance);
			this.orders_comm.add("R");
		}
	}
   	
   	public void moveLeft(int distance){
   		
		if (!this.isBusy){
			//System.out.println("[Understood] Moving LEFT by "+distance+". I am here:  ("+this.x/Block.suurus + ", "+this.y/Block.suurus +")");
			this.isBusy=true;
	        Path path = new Path();
	        //
	   		final double futureX = this.x-(Block.suurus*distance);
	        path.getElements().add(new MoveTo(this.x+this.getWidth()/2,this.y+this.getHeight()/2));
	        path.getElements().add(new LineTo(futureX+this.getWidth()/2, this.y+this.getHeight()/2));
	        //
	        
	        PathTransition pathTransition = new PathTransition();
	        pathTransition.setDuration(Duration.millis(Animation.speedFormula(distance)));
	        pathTransition.setPath(path);
	        pathTransition.setNode(this);
	        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
	        pathTransition.setCycleCount(1);
	        pathTransition.play();

	        pathTransition.onFinishedProperty().set(new EventHandler<ActionEvent>() {
	            @Override 
	            public void handle(ActionEvent actionEvent) {

	    	        x= futureX ;
	    	        isBusy=false;
	    	        if (!orders_comm.isEmpty()){
						String k = orders_comm.poll();
						int d = orders_dist.poll();
						
						if (k.equals("R")){
							moveRight(d);
						}
						else if (k.equals("U")){
							moveUp(d);
						}
						else if (k.equals("L")){
							moveLeft(d);
						}
						else if (k.equals("D")){
							moveDown(d);
						}
						else if (k.equals("C")){
							toggleColor();
						}
	    	        }
	            }
	        });
	        
		}
		else{

			//System.out.println("[Queing] Not moving LEFT by "+distance+". I am here:  ("+this.x/Block.suurus + ", "+this.y/Block.suurus +")");

			this.orders_dist.add(distance);
			this.orders_comm.add("L");
			}
	}   	
   	public void moveUp(int distance){
   		
		if (!this.isBusy){
			//System.out.println("[Understood] Moving UP by "+distance+". I am here:  ("+this.x/Block.suurus + ", "+this.y/Block.suurus +")");
			this.isBusy=true;
	        Path path = new Path();
	        //
	   		final double futureY = this.y-(Block.suurus*distance);
	        path.getElements().add(new MoveTo(this.x+this.getWidth()/2, this.y+this.getHeight()/2));
	        path.getElements().add(new LineTo(this.x+this.getWidth()/2, futureY+this.getHeight()/2));
	        //
	        
	        PathTransition pathTransition = new PathTransition();
	        
	        pathTransition.setDuration(Duration.millis(Animation.speedFormula(distance)));
	        pathTransition.setPath(path);
	        pathTransition.setNode(this);
	        pathTransition.setOrientation(PathTransition.OrientationType.NONE);
	        pathTransition.setCycleCount(1);
	        pathTransition.play();

	        pathTransition.onFinishedProperty().set(new EventHandler<ActionEvent>() {
	            @Override 
	            public void handle(ActionEvent actionEvent) {

	    	        y= futureY ;
	    	        isBusy=false;
	    	        if (!orders_comm.isEmpty()){
						String k = orders_comm.poll();
						int d = orders_dist.poll();
						
						if (k.equals("R")){
							moveRight(d);
						}
						else if (k.equals("U")){
							moveUp(d);
						}
						else if (k.equals("L")){
							moveLeft(d);
						}
						else if (k.equals("D")){
							moveDown(d);
						}
						else if (k.equals("C")){
							toggleColor();
						}
	    	        }
	            }
	        });
	        
		}
		else{

			//System.out.println("[Queing] Not moving UP by "+distance+". I am here:  ("+this.x/Block.suurus + ", "+this.y/Block.suurus +")");
			this.orders_dist.add(distance);
			this.orders_comm.add("U");
		}
	}   	
   	

	public void moveDown(int distance){
   		
		if (!this.isBusy ){
			//System.out.println("[Understood] Moving DOWN by "+distance+". I am here:  ("+this.x/Block.suurus + ", "+this.y/Block.suurus +")");
			this.isBusy=true;
	        Path path = new Path();
	        //
	   		final double futureY = this.y+(Block.suurus*distance);
	        path.getElements().add(new MoveTo(this.x+this.getWidth()/2, this.y+this.getHeight()/2));
	        path.getElements().add(new LineTo(this.x+this.getWidth()/2, futureY+this.getHeight()/2));
	        //
	        
	        PathTransition pathTransition = new PathTransition();	        
	        pathTransition.setDuration(Duration.millis(Animation.speedFormula(distance)));
	        pathTransition.setPath(path);
	        pathTransition.setNode(this);
	        pathTransition.setOrientation(PathTransition.OrientationType.NONE);
	        pathTransition.setCycleCount(1);
	        pathTransition.play();

	        pathTransition.onFinishedProperty().set(new EventHandler<ActionEvent>() {
	            @Override 
	            public void handle(ActionEvent actionEvent) {

	    	        y= futureY ;
	    	        isBusy=false;
	    	        if (!orders_comm.isEmpty()){
						String k = orders_comm.poll();
						int d = orders_dist.poll();
						
						if (k.equals("R")){
							moveRight(d);
						}
						else if (k.equals("U")){
							moveUp(d);
						}
						else if (k.equals("L")){
							moveLeft(d);
						}
						else if (k.equals("D")){
							moveDown(d);
						}
						else if (k.equals("C")){
							toggleColor();
						}
	    	        }
	            }
	        });
	        
		}
		else{

			//System.out.println("[Queing] Not moving DOWN by "+distance+". I am here:  ("+this.x/Block.suurus + ", "+this.y/Block.suurus +")");
			this.orders_dist.add(distance);
			this.orders_comm.add("D");
		}
	}
   	
	
	
	
	
	
	
}