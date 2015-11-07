package application;

import java.sql.Date;
/**
 * Parking spot class. Objects that contain all the information about each parking spot.
 * @author FrozenImpact
 *
 */
public class ParkingSpot {
	
	private int x;
	private int y;
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

	@Override
	public String toString() {
		return "ParkingSpot [x=" + x + ", y=" + y + ", distanceFromGate="
				+ distanceFromGate + ", custodian=" + custodian + ", model="
				+ model + ", ingestionDate=" + ingestionDate
				+ ", expirationDate=" + expirationDate + "]";
	}	
}
