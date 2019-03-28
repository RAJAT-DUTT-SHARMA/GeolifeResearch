package pocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import DataExtractionForStudy.DataExtractor;
import beans.PointHybrid;
import beans.StayPoint;

public class StayPointDetection {

	static double distanceThreshold = 200;// in metres
	static double timeThreshold = 20;// in mins

	public static List<PointHybrid> detectStopPoints(List<PointHybrid> segment) {

		List<PointHybrid> stayPoints = new ArrayList<>();

		for (int i = 0; i < segment.size(); i++) {
			PointHybrid p1 = segment.get(i);
			int j;
			for ( j = i + 1; j < segment.size(); j++) {
				PointHybrid p2 = segment.get(j);

				double distance = PointFunctions.distancePoint(p2, p1);
				if (distance > distanceThreshold) {
					// calculate time difference
					double timeDiff = (p2.getTimestamp().getTime() - p1.getTimestamp().getTime()) / 60000;
					if (timeDiff > timeThreshold) {
						// stay points
						double latNew = 0;
						double longNew = 0;
						Date timestamp=null;
						for (int k = i; k <= j; k++) {
							latNew = (latNew * (k - i) + segment.get(k).getLatitude()) / (k - i + 1);
							longNew = (longNew * (k - i) + segment.get(k).getLongitude()) / (k - i + 1);
						}
						timestamp=segment.get((i+j)/2).getTimestamp();
						stayPoints.add(new PointHybrid(latNew, longNew, timestamp));
					}
					break;	
				}
			}
			i=j;
		}

		return stayPoints;
	}
	
	
	public static List<PointHybrid> detectStayPoints2(List<PointHybrid> segment){
		List<PointHybrid> stayPoints=new ArrayList<>();
		double distThresh=100;//metres
		//if within distThresh more than 15 points than stay point
		for(int i=0;i<segment.size();i++) {
			double distance=0d;
			int j=i;
			int stayPointCount=0;
			double latitude=segment.get(i).getLatitude();
			double longitude=segment.get(i).getLongitude();
			while(distance<distThresh && j<segment.size()-1) {
				distance+=PointFunctions.distancePoint(segment.get(j+1), segment.get(j));
				stayPointCount++;
				latitude+=segment.get(j).getLatitude();
				longitude+=segment.get(j).getLongitude();
				j++;
			}
			if(stayPointCount>=15) {
				//stop point detected
				latitude=latitude/stayPointCount;
				longitude=longitude/stayPointCount;
				stayPoints.add(new PointHybrid(latitude, longitude, segment.get(i+(stayPointCount)/2).getTimestamp()));
				i=j;
			}
		}
		
		
		return stayPoints;
	}
	
	
	public static List<StayPoint> detectStayPoints3(List<PointHybrid> segment,int distThr,int minPoints){
		List<StayPoint> stayPoints=new ArrayList<>();
		int i=0;
		while(i<segment.size()-1) {
			int j=i+1;
			double distance=0d;
			int count=0;
			PointHybrid curPoint=segment.get(i);
			while(j<segment.size()) {
				distance=PointFunctions.distancePoint(segment.get(j), curPoint);
				if(distance<distThr) {
					count++;
				}
				else{
					if(count>minPoints) {
						//stay point detected
						//calculate the centroid
						double lat=0d,lon=0d;
						for(int k=i;k<=j;k++) {
							lat+=segment.get(k).getLatitude();
							lon+=segment.get(k).getLongitude();
						}
						stayPoints.add(new StayPoint(lat/(j-i+1), lon/(j-i+1),segment.get(j).getTimestamp().getTime()-curPoint.getTimestamp().getTime(),segment.get((i+j)/2).getTimestamp()));
						i=j;
					}
					break;
				}
				j++;
			}
			i++;
		}
		return stayPoints;
	}
	
	
	
	
	

	public static void main(String args[]) {
		try {
			String mode = "airplane";
			File dir = new File(DataExtractor.dirName + DataExtractor.extactedDataDirName + mode + "/");
			File files[] = dir.listFiles();
			int m = 0;
			List<List<PointHybrid>> segments = new ArrayList<>();
			for (File file : files) {
				if (file.getName().equals("010.txt")) {
					BufferedReader reader = new BufferedReader(new FileReader(file));
					String line;

					line = reader.readLine();

					Gson gson = new Gson();

					List<List<PointHybrid>> lists = gson.fromJson(line, new TypeToken<List<List<PointHybrid>>>() {
					}.getType());
					segments.addAll(lists);
				}
			}
			for(List<PointHybrid> seg:segments) {
				System.out.println(ProcessingUtilities.averageSpeedSegment(seg));
			}
			
			/*
			int k = 0;
			for (List<PointHybrid> seg : segments) {
				List<PointHybrid> stayPoints = detectStopPoints(seg);
				File file = new File(DataExtractor.dirName + "srudy/gpsCoord" + k + ".csv");
				FileWriter outputfile = new FileWriter(file);
				// create CSVWriter object filewriter object as parameter
				CSVWriter writer = new CSVWriter(outputfile);

				// adding header to csv
				String[] header = { "Number", "lat", "long" };
				writer.writeNext(header);
				int l = 0;
				for (PointHybrid p : stayPoints) {
					String data[] = new String[3];
					data[0] = "" + l;
					data[1] = "" + p.getLatitude();
					data[2] = "" + p.getLongitude();
					writer.writeNext(data);
					l++;
				}
				// closing writer connection
				writer.close();
				k++;
			}
*/
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	static void detectStayPoints2(){
		
		
	}
	
	
	public static List<StayPoint> dbScan(List<PointHybrid> segment,int k,int eps ){
		
		int clusterID=0;
		int i=0;
		for(PointHybrid point:segment) {
			point.setFlag(-1);
		}
		for(PointHybrid point:segment) {
			if(point.getFlag()==-1) {
				if(expandCluster(segment,i,clusterID,eps,k)) {
					point.setCorePoint(true);
					point.setFlag(clusterID);
					clusterID++;	
				}
			}
			i++;
		}
		
		//processing the pointIds
		List<StayPoint> stayPoints=new ArrayList<>();
		i=0;
		for(i=0;i<segment.size();i++) {
			PointHybrid point=segment.get(i);
			if(point.getFlag()!=-1) {
				Date startTime=point.getTimestamp();
				List<Integer> corePoints=new ArrayList<>();
				corePoints.add(i);
				int clusterId=point.getFlag();
				while(i<segment.size() && segment.get(i).getFlag()==clusterId) {
					if(segment.get(i).isCorePoint()) {
						corePoints.add(i);
					}
					i++;
				}
				i--;
				Date endTime=segment.get(i).getTimestamp();
				//get mean of all the core points
				double timeSpent=(endTime.getTime()-startTime.getTime());
				double lon=0d,lat=0d;
				for(Integer j:corePoints) {
					
					lat+=segment.get(j).getLatitude();
					lon+=segment.get(j).getLongitude();
				}
				lat=lat/corePoints.size();
				lon=lon/corePoints.size();
				
				stayPoints.add(new StayPoint(lat, lon, timeSpent, point.getTimestamp()));
			}
			i++;
		}
		Gson gson=new Gson();
		String json=gson.toJson(stayPoints);
		try {
			BufferedWriter writer=new BufferedWriter(new FileWriter(new File("/media/errajatds/R&D/work stuff/iot/iot data/geolife/Geolife Trajectories 1.3/TestData/StayPoints2.json")));
			writer.write(json);
			writer.flush();
			writer.close();
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		return stayPoints;
	}
	
	
	private static boolean expandCluster(List<PointHybrid> segment, int index, int clusterID, int eps, int k) {
		List<Integer>seeds=regionQuery(segment,eps,index);
		if(seeds.size()<k-1) {
			return false;
		}else {
			//all points in seeds are density reachable from point
			segment.get(index).setCorePoint(true);
			segment.get(index).setFlag(clusterID);

			setClusterId(seeds,segment,clusterID);
			
			for(int l=0;l<seeds.size();l++) {
				Integer ind=seeds.get(l);
				List<Integer> result=regionQuery(segment, eps, ind);
				if(result.size()>=k-1) {
					segment.get(ind).setCorePoint(true);
					segment.get(ind).setFlag(clusterID);
					
					for(Integer i:result) {
						PointHybrid resultP=segment.get(i);
						if(resultP.getFlag()==-1) {
							seeds.add(i);
						}
						resultP.setFlag(clusterID);
					}
				}
			}
			return true;
		}
	}


	private static void setClusterId(List<Integer> indices, List<PointHybrid> segment, int clusterID) {
		for(Integer index:indices) {
			segment.get(index).setFlag(clusterID);
		}
	}


	private static List<Integer> regionQuery(List<PointHybrid> segment, int eps, int index) {
		List<Integer>neighborsEps=new ArrayList<>();
		int j=index-1;
		while(j>=0 && PointFunctions.distancePoint(segment.get(index), segment.get(j))<=eps) {
			neighborsEps.add(j);
			j--;
		}
		j=index+1;
		while(j<segment.size() && PointFunctions.distancePoint(segment.get(j),segment.get(index))<=eps) {
			neighborsEps.add(j);
			j++;
		}
		return neighborsEps;
	}
	
	
	
	public static List<StayPoint> detectStayPoints4(List<PointHybrid> segment){
		//TODO
		List<StayPoint> stayPoints=new ArrayList<>();
		double distanceThresh=1d;//metres
		double velocityThresh=9d;
		int windowSize=10;
		if(segment.size()<windowSize) {
			return null;
		}
		int count=0;
		
		byte isSlowPoint[]=new byte[segment.size()-1];
		
		PointHybrid prevPoint=segment.get(0),curPoint=null;
		for(int i=1;i<segment.size();i++) {
			curPoint=segment.get(i);
			if(PointFunctions.distancePoint(curPoint, prevPoint)<distanceThresh) {
				isSlowPoint[i-1]=1;
			}else {
				isSlowPoint[i-1]=0;
			}
		}
		
		//now find subarrays in the isSlowPoint array where the 
		
		//TODO
		
	
		
		
		
		
		return stayPoints;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}