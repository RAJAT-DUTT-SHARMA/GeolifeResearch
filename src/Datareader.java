import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Datareader {
	
	static String directoryPath="/media/errajatds/Stuff/work stuff/iot/iot data/geolife/Geolife Trajectories 1.3/FilteredData/";
	
	static String getFolderName(int i){
		int a=0,b=0,c=0;
		c=i%10;
		b=(i/10)%10;
		a=i/100;
		if(a>9){
			a=0;
		}
		return a+""+b+""+c;
	}
	
	public static List<PointHybrid> readData(File file) throws IOException, ParseException {
		BufferedReader reader=new BufferedReader(new FileReader(file));
	
		for(int i=0;i<6;i++) {
			reader.readLine();
		}
		
		String line=null;
		List<PointHybrid> list=new ArrayList<>();
		
		while((line=reader.readLine())!=null) {
			String dataCols[]=line.split(",");
			float lattitude=Float.parseFloat(dataCols[0]);
			float longitude=Float.parseFloat(dataCols[1]);
			Date timeStamp=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataCols[5]+" "+dataCols[6]);
			PointHybrid p=new PointHybrid(lattitude, longitude, timeStamp);
			list.add(p);	
		}
		return list;
	}

}
