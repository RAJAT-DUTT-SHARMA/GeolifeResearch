package DataExtractionForStudy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import beans.PointHybrid;

public class DataExtractor {

	public static String dirName="/media/errajatds/R&D/work stuff/iot/iot data/geolife/Geolife Trajectories 1.3/";
	public static String extactedDataDirName="Study data for non walk transportation modes/";
	public static String dataDirName="Data/";
	static String filteredDataDirName="FilteredData/";
	
	static void getSegments(String username, String transportationMode){
		//1. read the labels.txt file

		File labels=new File(dirName+dataDirName+username+"/labels.txt");


		try {

			List<Date> startTimes=new ArrayList<>();
			List<Date> endTimes=new ArrayList<>();
			System.out.println(transportationMode);
			BufferedReader reader=new BufferedReader(new FileReader(labels));
			String line="";
			reader.readLine();
			while((line=reader.readLine())!=null){
				String strings[]=line.split("\t");
				if(strings[2].equals(transportationMode)) {
					Date startTime=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(strings[0]);
					Date endTime=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(strings[1]);
					startTimes.add(startTime);
					endTimes.add(endTime);	
				}
			}
			reader.close();	
			
			if(startTimes.isEmpty()) {
				return;
			}
			
		
			
			//2. read the trajectory files
			File dir=new File(dirName+filteredDataDirName+username+"/");
			File files[]=dir.listFiles();
			int timeStampPointer=0;
			
			List<List<PointHybrid>> segments=new ArrayList<>();
			for(File file:files) {
				//read the segments.
				reader=new BufferedReader(new FileReader(file));
				String jsonData=reader.readLine();
				Gson gson=new Gson();
				
				List<PointHybrid> tripData=gson.fromJson(jsonData, new TypeToken<List<PointHybrid>>() {}.getType());
				
				boolean flag=true;
				List<PointHybrid> segment=new ArrayList<>();
				for(PointHybrid curPoint:tripData){
					if(isTargetPoint(curPoint.getTimestamp(),startTimes,endTimes)) {
						segment.add(curPoint);
						flag=false;
					}else {
						if(!segment.isEmpty()) {
							segments.add(segment);
							segment=new ArrayList<>();
							flag=true;
						}
					}
				}
				if(flag==false) {
					segments.add(segment);
				}
			}
			reader.close();
			
			//i have segments , now write them to a file
			if(transportationMode.equals("taxi")) {
				transportationMode="car";
			}
			
			Gson gson=new Gson();
			String data=gson.toJson(segments);
			BufferedWriter writer=new BufferedWriter(new FileWriter(new File(dirName+extactedDataDirName+transportationMode+"/"+username+".txt")));
			writer.write(data);
			writer.flush();
			writer.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	private static boolean isTargetPoint(Date timeStamp, List<Date> startTimes, List<Date> endTimes) {
		// TODO Auto-generated method stub
		
		for(int i=0;i<startTimes.size();i++) {
			if(timeStamp.before(startTimes.get(i))) {
				return false;
			}
			if(timeStamp.after(startTimes.get(i))&& timeStamp.before(endTimes.get(i))) {
				return true;
			}
		}
		return false;
	}  
	
	
	
	
	
}
