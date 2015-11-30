package application;

import java.util.Date;

/**
 * Parking spot class. Objects that contain all the information about each parking spot.
 * @author FrozenImpact
 *
 */
public class ParkingSpot {
	
	private int x;
	private int y;
	private long id;
	private double distanceFromGate;
	private String custodian;
	private String model;
	private Date ingestionDate;
	private Date expirationDate;
	
	public ParkingSpot(int x, int y, String custodian, String model, Date ingestionDate, Date expirationDate) {
		this.x = x;
		this.y = y;
		this.distanceFromGate = Math.sqrt((Math.pow(Math.abs(Algorithm.gateX - x),2)+      Math.pow(Math.abs(Algorithm.gateY - y),2)));
		this.custodian = custodian;
		this.model = model;
		this.ingestionDate = ingestionDate;
		this.expirationDate = expirationDate;
	}
	
	public ParkingSpot(int x, int y, String custodian, String model, Date ingestionDate, Date expirationDate, long id) {
		this.x = x;
		this.y = y;
		this.distanceFromGate = Math.sqrt((Math.pow(Math.abs(Algorithm.gateX - x),2)+      Math.pow(Math.abs(Algorithm.gateY - y),2)));
		this.custodian = custodian;
		this.model = model;
		this.id = id;
		this.ingestionDate = ingestionDate;
		this.expirationDate = expirationDate;
	}

	public boolean occupied(){
		if(this.model == null && this.custodian == null){
			return false;
		}
		return true;
	}
	
	public double getDistance(){
		return this.distanceFromGate;
	}
	public int getX(){
		return this.x;
	}
	public int getY(){
		return this.y;
	}
	public long getID(){
		return this.id;
	}	
	public String getCustodian(){
		return this.custodian;
	}
	public String getModel(){
		return this.model;
	}
	public Date getExpirationDate(){
		return this.expirationDate;
	}
	public Date getIngestionDate(){
		return this.ingestionDate;
	}
	
	public ParkingSpot occupy(String custodian, String model, Date expirationDate){
		this.custodian = custodian;
		this.model = model;
		this.ingestionDate = new Date(System.currentTimeMillis());
		this.expirationDate = expirationDate;
		this.id = ParkingSpotManager.getHighestID();
		ParkingSpotManager.parkingSpotsOccupied.add(this);
		return this;
	}
	
	public ParkingSpot occupy(String custodian, String model, Date ingestionDate, Date expirationDate, long id){
		this.custodian = custodian;
		this.model = model;
		this.ingestionDate = ingestionDate;
		this.expirationDate = expirationDate;
		this.id = id;
		ParkingSpotManager.parkingSpotsOccupied.add(this);
		return this;
	}
	
	public ParkingSpot vacate(){
		this.custodian = null;
		this.model = null;
		this.ingestionDate = null;
		this.expirationDate = null;
		this.id = 0;
		ParkingSpotManager.parkingSpotsOccupied.remove(this);
		return this;
	}
	
	@Override
	public String toString() {
		return "id: "+id;
	}	
}
