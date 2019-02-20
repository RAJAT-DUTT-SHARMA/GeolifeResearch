package pocessing;

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

	private static double toRad(double value) {
		return value * Math.PI / 180;
	}
	
	public static double velocityPoint(PointHybrid p2,PointHybrid p1) {
		double velocity;
		double time=(p2.timestamp.getTime()-p1.getTimestamp().getTime())/1000;//in seconds
		if(time!=0d) {
			velocity=distancePoint(p2, p1)/time;
			return velocity;
		}else 
			return 0d;
	}
	
	public static double accelerationPoints(PointHybrid p2,PointHybrid p1) {
		double acceleration;
		double time=(p2.getTimestamp().getTime()-p1.getTimestamp().getTime())/1000;// in seconds
		if(time==0d) {
			return 0d;
		}else {
			acceleration=velocityPoint(p2, p1)/time;
			return acceleration;
		}
	}
	
	
	
}
