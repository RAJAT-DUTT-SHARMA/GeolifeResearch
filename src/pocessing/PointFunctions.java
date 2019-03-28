package pocessing;

import beans.PointHybrid;
import beans.StayPoint;

public class PointFunctions {

	public static double distancePoint(PointHybrid p2 , PointHybrid p1){
		
		final int R = 6371000; // Radious of the earth in metres
        double lat1 = p1.getLatitude();
        double lon1 = p1.getLongitude();
        double lat2 = p2.getLatitude();
        double lon2 = p2.getLongitude();
        double latDistance = toRad(lat2-lat1);
        double lonDistance = toRad(lon2-lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + 
                   Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) * 
                   Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = R * c;
        return distance;
	}

	public static double distancePoint(double lat2,double lon2,double lat1,double lon1){
		
		final int R = 6371000; // Radious of the earth in metres
        double latDistance = toRad(lat2-lat1);
        double lonDistance = toRad(lon2-lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + 
                   Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) * 
                   Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = R * c;
        return distance;
	}

	
	public static double getAngle(double lat2,double lat1,double long2,double long1){
		
		double dy = lat2 - lat1;
        double dx = Math.cos(Math.PI / 180 * lat1) * (long2 - long1);
        double angle = Math.atan2(dy, dx);
        
        //to degrees
        angle=angle*(180/Math.PI);
		
        return angle;
	}
	private static double toRad(double value) {
		return value * Math.PI / 180;
	}
	
	public static double velocityPoint(PointHybrid p2,PointHybrid p1) {
		double velocity;
		double time=(p2.getTimestamp().getTime()-p1.getTimestamp().getTime())/1000;//in seconds
		if(time!=0d) {
			velocity=distancePoint(p2, p1)/time;
			return velocity;
		}else 
			return 0d;
	}
	
	public static double accelerationPoints(PointHybrid p3,PointHybrid p2,PointHybrid p1) {
		double acceleration;
		double time=(p2.getTimestamp().getTime()-p1.getTimestamp().getTime())/1000;// in seconds
		if(time==0d) {
			return 0d;
		}else {
			acceleration=(velocityPoint(p3, p2)-velocityPoint(p2, p1))/time;
			return acceleration;
		}
	}

	public static Number velocityPoint(StayPoint sp, StayPoint prevSP) {
		// TODO Auto-generated method stub
		double velocity;
		double time=(sp.getTimeStamp().getTime()-prevSP.getTimeStamp().getTime())/1000;//in secs
		if(time!=0d) {
			double distance=distancePoint(sp.getLatitude(),sp.getLongitude(), prevSP.getLatitude(),prevSP.getLongitude())/1000;//in km
			time=time/3600;//in hours
			 velocity=distance/time;//in kmph
			 return velocity;
		}else 
			return 0d;
	}
	
	
	
}
