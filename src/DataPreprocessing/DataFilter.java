package DataPreprocessing;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import DataExtractionForStudy.DataExtractor;
import beans.PointHybrid;
import pocessing.PointFunctions;

public class DataFilter {

	
	
	
	public static void	writeJsonFiles(File file,String userName){
		try{
			BufferedReader reader=new BufferedReader(new FileReader(file));
			for(int i=0;i<6;i++) {
				reader.readLine();
			}
			List<PointHybrid> list=new ArrayList<>();
			String line=null;
			while((line=reader.readLine())!=null) {
				String attribs[]=line.split(",");
				float latitude=Float.parseFloat(attribs[0]);
				float longitude=Float.parseFloat(attribs[1]);
				Date timestamp=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(attribs[5]+" "+attribs[6]);
				
				list.add(new PointHybrid(latitude, longitude, timestamp));
				
			}
			
			list.sort(new Comparator<PointHybrid>() {
				public int compare(PointHybrid arg0, PointHybrid arg1) {
					if(arg0.getTimestamp().before(arg1.getTimestamp())) {
						return -1;
					}else if(arg0.getTimestamp().after(arg1.getTimestamp())) {
						return 1;
					}else {
						return 0;
					}
				}
			});
			
			reader.close();
		
			List<PointHybrid> list2=new ArrayList<>();
			PointHybrid prev=null;
			for(PointHybrid p:list) {
				if(prev==null) {
					list2.add(p);
				}else if(p.getTimestamp().compareTo(prev.getTimestamp())!=0) {
					list2.add(p);
				}
				prev=p;
			}
			list=null;
			Gson gson=new Gson();
			
			String jsonString=gson.toJson(list2);
			
			BufferedWriter writer=new BufferedWriter(new FileWriter(new File(DataExtractor.dirName+Controller.jsonDir+userName+"/"+file.getName())));
			writer.write(jsonString);
			writer.flush();
			writer.close();
		
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
	
	public static void filterData(File file,String userName){
		try {
			BufferedReader reader=new BufferedReader(new FileReader(file));
			String jsonString=reader.readLine();
			Gson gson=new Gson();
			List<PointHybrid> list=gson.fromJson(jsonString, new TypeToken<List<PointHybrid>>() {}.getType());
			
			List<Double> velocities=getVelocities(list);
				
			double mean=calculateMeanVelocity(velocities);
			double stdDev=calculateStdDevVelocity(velocities,mean);
			
			List<PointHybrid> filteredData=filterData(list,velocities,mean,stdDev);
		
			BufferedWriter writer=new BufferedWriter(new FileWriter(DataExtractor.dirName+Controller.jsonFilteredDataDir+userName+"/"+file.getName()));
			writer.write(gson.toJson(filteredData));
			writer.flush();
			writer.close();
			
		}catch(Exception e) {
			System.out.println(e+"here");
		}
		
	}


	private static List<PointHybrid> filterData(List<PointHybrid> list, List<Double> velocities, double mean,
			double stdDev) {
		double alpha=1.8;
		List<PointHybrid> filteredData=new ArrayList<>();
		filteredData.add(list.get(list.size()-1));
		for(int i=0;i<(list.size()-1);i++) {
			if(velocities.get(i)>(alpha*stdDev+mean)) {
				//outlier not to be added into the list
			}else {
				filteredData.add(list.get(i+1));
			}
		}
		
		return filteredData;
	}


	private static List<Double> getVelocities(List<PointHybrid> list) {
		PointHybrid prev=null;
		List<Double> velocities=new ArrayList<>();
		for(PointHybrid p:list) {
			if(prev!=null) {
				velocities.add(PointFunctions.velocityPoint(p, prev));
			}
			prev=p;
		}
		return velocities;
	}


	private static double calculateStdDevVelocity(List<Double> velocities, double mean) {
		double stdDev=0d;
		double meanSquare=0d;
		int i=0;
		for(Double velocity:velocities) {
			meanSquare=(meanSquare*i+Math.pow(velocity,2))/(i+1);
		}
		stdDev=Math.sqrt(meanSquare-mean);
		return stdDev;
	}


	private static double calculateMeanVelocity(List<Double> velocities) {
		double mean=0d;
		int i=0;
		for(Double velocity:velocities) {
			mean=(mean*i+velocity)/(i+1);
		}
		return mean;
	}
	
	
	
	
	
}
