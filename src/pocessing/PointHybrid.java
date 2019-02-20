package pocessing;
import java.util.Date;

public class PointHybrid{
			 float latitude,longitude;
			 Date timestamp;
			 String mode;
			public PointHybrid(float latitude, float longitude, Date timestamp) {
				super();
				this.latitude = latitude;
				this.longitude = longitude;
				this.timestamp = timestamp;
			}
			

			public String getMode() {
				return mode;
			}

			public void setMode(String mode) {
				this.mode = mode;
			}

			public float getLatitude() {
				return latitude;
			}
			public void setLatitude(float latitude) {
				this.latitude = latitude;
			}
			public float getLongitude() {
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
				return latitude+","+longitude;
			}
			
			
		 }
