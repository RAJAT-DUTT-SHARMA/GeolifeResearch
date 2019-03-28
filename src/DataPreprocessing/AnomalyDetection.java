package DataPreprocessing;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;

import beans.PointHybrid;
import pocessing.PointFunctions;

public class AnomalyDetection {

	public static void filterData(File file,String username) {
		try {
			BufferedReader reader=new BufferedReader(new FileReader(file));
			
			for(int i=0;i<6;i++) {
				reader.readLine();
			}
			String line;
			PointHybrid p3=null,p2=null,p1=null;
			List<PointHybrid> list=new ArrayList<>();
			
			while((line=reader.readLine())!=null) {
				String attribs[]=line.split(",");
				float latitude=Float.parseFloat(attribs[0]);
				float longitude=Float.parseFloat(attribs[1]);
				Date timestamp=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(attribs[5]+" "+attribs[6]);
				p3=new PointHybrid(latitude, longitude, timestamp);
				if(p1==null) {
					p1=p3;
					list.add(p1);
				}else if(p2==null){
					p2=p3;
				}else {
					//start to detect the anomaly in the data 
					double dist12=PointFunctions.distancePoint(p2, p1);
					double dist23=PointFunctions.distancePoint(p3, p2);
					double dist13=PointFunctions.distancePoint(p3, p1);
					if(dist12+dist23>=2*dist13) {
						//anomaly detected
			
					}else {
						list.add(p2);
						p1=p2;
					}
					p2=p3;
				}
			}
			reader.close();
			list.add(p3);
			Gson gson=new Gson();
			String jsonData=gson.toJson(list);
			File newfile=new File("/media/errajatds/R&D/work stuff/iot/iot data/geolife/Geolife Trajectories 1.3/FilteredData/"+username+"/"+file.getName()+".json");
			newfile.createNewFile();
			BufferedWriter writer=new BufferedWriter(new FileWriter(newfile,true));
			writer.write(jsonData);
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
	}
	
}
