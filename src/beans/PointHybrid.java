package beans;
import java.util.Date;

public class PointHybrid{
			 double latitude,longitude;
			 Date timestamp;
			 String mode;
			 int flag;
			 boolean corePoint;
			 
			 public int getFlag() {
				return flag;
			}


			public void setFlag(int flag) {
				this.flag = flag;
			}


			public PointHybrid(double latNew, double longNew, Date timestamp) {
				super();
				this.latitude = latNew;
				this.longitude = longNew;
				this.timestamp = timestamp;
				this.flag=-1;
				this.corePoint=false;
			}
			
			

			public boolean isCorePoint() {
				return corePoint;
			}


			public void setCorePoint(boolean corePoint) {
				this.corePoint = corePoint;
			}


			public String getMode() {
				return mode;
			}

			public void setMode(String mode) {
				this.mode = mode;
			}

			public double getLatitude() {
				return latitude;
			}
			public void setLatitude(float latitude) {
				this.latitude = latitude;
			}
			public double getLongitude() {
				return longitude;
			}
			public void setLongitude(float longitude) {
				this.longitude = longitude;
			}
			public Date getTimestamp() {
				return timestamp;
			}
			public void setTimestamp(Date timestamp) {
				this.timestamp = timestamp;
			}
			
			@Override
			public String toString() {
			// TODO Auto-generated method stub
				return latitude+","+longitude+", "+timestamp;
			}
			
			
		 }
