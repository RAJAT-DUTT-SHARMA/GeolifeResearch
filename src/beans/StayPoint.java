package beans;

import java.util.Date;

public class StayPoint {
double latitude;
double longitude;
double timeSpent;
Date timeStamp;

public Date getTimeStamp() {
	return timeStamp;
}
public void setTimeStamp(Date timeStamp) {
	this.timeStamp = timeStamp;
}
public double getLatitude() {
	return latitude;
}
public void setLatitude(double latitude) {
	this.latitude = latitude;
}
public double getLongitude() {
	return longitude;
}
public void setLongitude(double longitude) {
	this.longitude = longitude;
}
public double getTimeSpent() {
	return timeSpent;
}
public void setTimeSpent(double timeSpent) {
	this.timeSpent = timeSpent;
}
public StayPoint(double latitude, double longitude, double timeSpent) {
	super();
	this.latitude = latitude;
	this.longitude = longitude;
	this.timeSpent = timeSpent;
}
public StayPoint(double latitude, double longitude, double timeSpent, Date timeStamp) {
	super();
	this.latitude = latitude;
	this.longitude = longitude;
	this.timeSpent = timeSpent;
	this.timeStamp = timeStamp;
}

@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "lat : "+latitude+"  long: "+longitude+"  timespent : "+timeSpent+"  timestamp : "+timeStamp;
	}
	
}
