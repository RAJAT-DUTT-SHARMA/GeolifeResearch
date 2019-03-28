package FeatureStudyExtractedData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import com.google.gson.Gson;

import DataExtractionForStudy.DataExtractor;
import beans.PointHybrid;
import beans.StayPoint;
import pocessing.PointFunctions;
import pocessing.ProcessingUtilities;
import pocessing.StayPointDetection;

public class FeatureTest {

	
	public static void main(String args[]) {
		//GraphPlotter.plotScatterChart("Distance Distribution", createScatterPlotDataset(), "segment number", "distance (kilo metres)");
		//GraphPlotter.plotScatterChart("Average Velocity Distribution", createScatterPlotDatasetAverageVelocity(), "segment number", "avg velocity (metres/sec)");
		//String modes[]= { "bus" , "train", "subway", "car", "bike", "airplane" };
	/*	for(String mode:modes) {
			GraphPlotter.plotXYLineChart("Move Ability Distribution "+mode, createlineChartDatasetMoveAbility(mode), "time diff (secs)", "move ability");	
		}
		
/*		for(String mode:modes) {
			GraphPlotter.plotXYLineChart("Acceleration Distribution "+mode, createXYLineChartAcceleration(mode), "time diff (secs)", "acceleration(m/s*s)");	
		}*/
		//GraphPlotter.plotScatterChart("Time Distribution", createScatterPlotDatasetForTime(), "segment number", "Time (mins)");
		/*for(String mode:modes) {
			GraphPlotter.plotScatterChart("Stop Point Time Difference Distribution "+mode, createXYScatterChartDatasetForTimeBetweenStayPoints(mode), "stay point number", "stay point time diff(secs)");
		}*/
		
		//GraphPlotter.plotScatterChart("Stop Point per unit distance ", createXYScatterChartDatasetForNumberOfStayPoints(), "segment number", "stay point per unit distance");
		/*
		for(int k=2;k<11;k++) {
			GraphPlotter.plotScatterChart("K-Distance Graph bus "+k, createPlotForKNearestNeighborDistance("bus", k), "number of points", "distance (metres)");
		}
		//testStayPointDetection();
		/*List<XYSeriesCollection> collections=plotStayPointTimeSpent();
		XYSeriesCollection collection=new XYSeriesCollection();
		for(XYSeriesCollection col:collections) {
			collection.addSeries(col.getSeries(0));
		}
		GraphPlotter.plotXYLineChart("Stay Point Time spent BUS",collection , "stay point number", "time (secs)");	
		GraphPlotter.plotXYLineChart("Stop Point Time Diff (BUS)", plotStayPointTimeDiff("bus"), "Stay Point Number", "Time Diff. (secs)");	
		GraphPlotter.plotXYLineChart("Stop Point Analysis (BUS)", StayPointAnalysis("bus"), "time (secs)", "Distance. (km)");	
		*/
		//GraphPlotter.plotXYLineChart("Stop Point Dist Diff (BUS)", plotStayPointDistanceDiff("bus"), "Stay Point Number", "Dist Diff. (km)");	
		
		//plotGraphForAnalysis();
		//GraphPlotter.plotXYLineChart("Bus",test2("bus"), "point number", "distance (metres)");
		//GraphPlotter.plotXYLineChart("Car",test2("car"), "point number", "distance (metres)");
		//test1("car");
		plotGraphForAnalysis();
		//GraphPlotter.plotXYLineChart("Car",test2("car"), "point number", "distance (metres)");
		//String mode="bus";
		//plotVelocityGraphs(mode);
	}
	
	private static void plotAccelerationGraphs(String mode) {
		List<List<PointHybrid>> segments=ReadData.readData(mode);
		segments.sort(new Comparator<List<PointHybrid>>() {
			@Override
			public int compare(List<PointHybrid> arg0, List<PointHybrid> arg1) {
				return arg1.size()-arg0.size();
			}
		});
		for(int i=0;i<8;i++) {
			List<PointHybrid> segment=segments.get(i);
			GraphPlotter.plotXYLineChart("Acceleration "+ mode, getAccelerationCollection(segment), "time (secs)", "acceleration (m/s2)");
		
		}	
		
	}

	private static XYSeriesCollection getAccelerationCollection(List<PointHybrid> segment) {
		if(segment.size()<3) {
			return null;
		}
		XYSeriesCollection collection=new XYSeriesCollection();
		XYSeries series=new XYSeries("acc");
		PointHybrid p1=segment.get(0),p2=segment.get(1),p3;
		for(int i=2;i<segment.size();i++) {
			p3=segment.get(i);
			//TODO
			series.add(i,PointFunctions.accelerationPoints(p3, p2, p1));
			p1=p2;
			p2=p3;
		}
		collection.addSeries(series);
		return collection;
	}

	private static void plotVelocityGraphs(String mode) {
		List<List<PointHybrid>> segments=ReadData.readData(mode);
		System.out.println(segments.size());
		
		segments.sort(new Comparator<List<PointHybrid>>() {
			@Override
			public int compare(List<PointHybrid> arg0, List<PointHybrid> arg1) {
				return arg1.size()-arg0.size();
			}
		});
		for(int i=0;i<8;i++) {
			List<PointHybrid> segment=segments.get(i);
			System.out.println(segment.size());
			GraphPlotter.plotXYLineChart("Velocity "+ mode, getVelocityCollection(segment), "time (secs)", "velocity (metres/sec)");
		}
	}

	private static XYSeriesCollection getVelocityCollection(List<PointHybrid> segment) {
		if(segment.size()<3) {
			return null;
		}
		XYSeriesCollection collection=new XYSeriesCollection();
		XYSeries series=new XYSeries("velo");
		
		PointHybrid p1=segment.get(0);
		
		Date startTime=p1.getTimestamp();
		for(int i=1;i<segment.size();i++) {
			PointHybrid p2=segment.get(i);
			series.add((p2.getTimestamp().getTime()-startTime.getTime())/1000,PointFunctions.velocityPoint(p2, p1)*3.6);
			p1=p2;
		}
		collection.addSeries(series);
		return collection;
	}

	
	public static XYSeriesCollection test2(String mode) {
		
		List<List<PointHybrid>> segments=ReadData.readData(mode);
		segments.sort(new Comparator<List<PointHybrid>>() {
			@Override
			public int compare(List<PointHybrid> arg0, List<PointHybrid> arg1) {
				return arg1.size()-arg0.size();
			}
		});
		
		XYSeries series;
		XYSeriesCollection collection=new XYSeriesCollection();
		
		for(int j=2;j<3;j++) {
			series=new XYSeries(j);
			PointHybrid prevPoint=segments.get(j).get(0);
			Date startTime=prevPoint.getTimestamp();
			int i=0;
			for(PointHybrid curPoint:segments.get(j)) {
				series.add(i,PointFunctions.distancePoint(curPoint, prevPoint));
				prevPoint=curPoint;
				i++;
			}
			collection.addSeries(series);
		}
		return collection;
	}
	
	private static void test1(String mode) {
		// TODO Auto-generated method stub
		List<List<PointHybrid>> segments=ReadData.readData(mode);
		segments.sort(new Comparator<List<PointHybrid>>() {
			@Override
			public int compare(List<PointHybrid> arg0, List<PointHybrid> arg1) {
				// TODO Auto-generated method stub
				return arg1.size()-arg0.size();
			}
		});
		
		XYSeries series1=new XYSeries("distance");
		XYSeries series2=new XYSeries("time");
		
		PointHybrid prevPoint=segments.get(0).get(0);
		Date startTime=prevPoint.getTimestamp();
		int i=0;
		for(PointHybrid curPoint:segments.get(0)) {
			series1.add(i,PointFunctions.distancePoint(curPoint, prevPoint));
			//series2.add(i,(curPoint.getTimestamp().getTime()-prevPoint.getTimestamp().getTime())/1000);
			prevPoint=curPoint;
			i++;
		}
		XYDataset collection1=new XYSeriesCollection(series1);
		XYDataset collection2=new XYSeriesCollection(series2);
	
		final DualAxisChart demo = new DualAxisChart("Overlap charts ",collection2,collection1);
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);	
	}
	
	static void plotGraphForAnalysis(){
		String mode="bus";
		List<List<XYDataset>> setOfDatasets=createDatasetForOverlaidGraphStayPoint(mode);
		int i=0;
		for(List<XYDataset> datasets:setOfDatasets) {
			final DualAxisChart demo = new DualAxisChart("Overlap charts (BUS) "+i,datasets.get(0),datasets.get(1));
	        demo.pack();
	        RefineryUtilities.centerFrameOnScreen(demo);
	        demo.setVisible(true);	
	        i++;
	        if(i==10) {
	        	break;
	        }
		}
	}
	
	static XYSeriesCollection createScatterPlotDataset(){
		
		XYSeriesCollection collection=new XYSeriesCollection();
		String modes[]= { "bus" , "train", "subway", "car", "bike", "airplane" };
		
		for(String mode:modes) {
			System.out.println(mode);
			XYSeries series=new XYSeries(mode);
			List<List<PointHybrid>> segments=null;
			segments=ReadData.readData(mode);
			int i=0;
			for(List<PointHybrid> segment :segments) {
				double distance=ProcessingUtilities.distanceSegment(segment)/1000;
				series.add(i,distance);
				i++;
			}
			collection.addSeries(series);
		}
		
		return collection;
	}
	
	static XYSeriesCollection createScatterPlotDatasetForTime(){
		
		XYSeriesCollection collection=new XYSeriesCollection();
		String modes[]= { "bus" , "train", "subway", "car", "bike", "airplane" };
		
		for(String mode:modes) {
			System.out.println(mode);
			XYSeries series=new XYSeries(mode);
			List<List<PointHybrid>> segments=null;
			segments=ReadData.readData(mode);
			int i=0;
			for(List<PointHybrid> segment :segments) {
				series.add(i,(segment.get(segment.size()-1).getTimestamp().getTime()-segment.get(0).getTimestamp().getTime())/60000);
				i++;
			}
			collection.addSeries(series);
		}
		
		return collection;
	}

	
	static XYSeriesCollection createScatterPlotDatasetAverageVelocity(){
		
		XYSeriesCollection collection=new XYSeriesCollection();
		String modes[]= { "bus" , "train", "subway", "car", "bike", "airplane" };
		
		for(String mode:modes) {
			//System.out.println(mode);
			XYSeries series=new XYSeries(mode);
			List<List<PointHybrid>> segments=null;
			segments=ReadData.readData(mode);
			int i=0;
			for(List<PointHybrid> segment :segments) {
				double distance=ProcessingUtilities.averageSpeedSegment(segment);
				series.add(i,distance);
				i++;
			}
			collection.addSeries(series);
		}
		
		return collection;
	}
	
	static XYSeriesCollection createXYLineChartAcceleration(String mode) {
		XYSeriesCollection collection=new XYSeriesCollection();
		List<List<PointHybrid>> segments=null;
		segments=ReadData.readData(mode);
		int i=0;
		for(List<PointHybrid> segment :segments) {
			XYSeries series=accelerationSegment(segment, i);
			if(series!=null) {
				collection.addSeries(series);
				i++;
			}
			if(i==50) {
				break;
			}
		}
		return collection;
	}
	
	
	private static XYSeries accelerationSegment(List<PointHybrid> segment, int i) {
		XYSeries series=new XYSeries(i);
		if(segment.size()<50) {
			return null;
		}
		PointHybrid p3,p2=segment.get(1),p1=segment.get(0);
		Date startTime=p1.getTimestamp();
		for(int j=2;j<segment.size();j++) {
			p3=segment.get(j);
			double timeDiff=(p2.getTimestamp().getTime()-startTime.getTime())/1000;
			if(!(timeDiff<=0)) {
				series.add(timeDiff,PointFunctions.accelerationPoints(p3, p2, p1));
			}
			p1=p2;
			p2=p3;
			
		}
		return series;
	}
	static XYSeriesCollection createlineChartDatasetMoveAbility(String mode){
		int windowSize=10;
		
			XYSeriesCollection collection=new XYSeriesCollection();
			List<List<PointHybrid>> segments=null;
			segments=ReadData.readData(mode);
			int i=0;
			for(List<PointHybrid> segment :segments) {
				XYSeries series=moveAbilitySeriesSegment(segment, i);
				if(series!=null) {
					collection.addSeries(series);
					i++;
				}
				if(i==50) {
					break;
				}
			}
		return collection;
	}
	
	static XYSeries moveAbilitySeriesSegment(List<PointHybrid> segment,int i){
		int windowSize=10;
		XYSeries series=new XYSeries(i);
		if(segment.size()<10) {
			return null;
		}
		int pointer=0;
		double directDistance=-1;
		double curveDistance=0;
		double movingAbility=-1;
		for(int j=0;j<9;j++) {
			curveDistance+=PointFunctions.distancePoint(segment.get(j+1),segment.get(j));
		}
		directDistance=PointFunctions.distancePoint(segment.get(9), segment.get(0));
		movingAbility=directDistance/curveDistance;
		Date startTime=segment.get(0).getTimestamp();
		
		series.add(0d,movingAbility);
		
		for(int j=1;j<segment.size()-windowSize;j++) {
			curveDistance-=PointFunctions.distancePoint(segment.get(j), segment.get(j-1));
			curveDistance+=PointFunctions.distancePoint(segment.get(j+windowSize-1), segment.get(j+windowSize-2));
			directDistance=PointFunctions.distancePoint(segment.get(j+windowSize-1), segment.get(j));
			movingAbility=directDistance/curveDistance;
			series.add((segment.get(j).getTimestamp().getTime()-startTime.getTime())/1000,movingAbility);
		}
		return series;
	}
	static XYSeriesCollection createXYScatterChartDatasetForNumberOfStayPoints(){
		String modes[]= { "bus" , "train", "subway", "car", "bike", "airplane" };
		XYSeriesCollection collection=new XYSeriesCollection();
		
		for(String mode : modes) {
			List<List<PointHybrid>> segments=ReadData.readData(mode);
			XYSeries series=new XYSeries(mode);
			int i =0;
			for(List<PointHybrid> segment:segments) {
				series.add(i,100*StayPointDetection.detectStayPoints3(segment,50,10).size()/ProcessingUtilities.distanceSegment(segment));
				i++;
			}
			collection.addSeries(series);
		}
		
		return collection;
	}
	
	static XYSeriesCollection createXYScatterChartDatasetForTimeBetweenStayPoints(String mode){
		XYSeriesCollection collection=new XYSeriesCollection();
			List<List<PointHybrid>> segments=ReadData.readData(mode);
			int i =0;
			for(List<PointHybrid> segment:segments) {
				XYSeries series=new XYSeries(i);
				Date lastTime=segment.get(0).getTimestamp();
				List<PointHybrid> stayPoints=StayPointDetection.detectStayPoints2(segment);
				int j=0;
				for(PointHybrid p:stayPoints) {
					double timeDiff=(p.getTimestamp().getTime()-lastTime.getTime())/1000;
					if(timeDiff>=0) {
						series.add(j,timeDiff);
						lastTime=p.getTimestamp();
					}
					j++;
				}
				collection.addSeries(series);
				if(i==50) {
					break;
				}
				i++;
		}
		
		return collection;
	}
	
	private static DecimalFormat df=new DecimalFormat(".##");
	
	static XYSeriesCollection createPlotForKNearestNeighborDistance(String mode,int k){
		System.out.println("let's start");
		XYSeriesCollection collection=new XYSeriesCollection();
		List<List<PointHybrid>> segments=ReadData.readData(mode);
		segments.sort(new Comparator<List<PointHybrid>>() {

			@Override
			public int compare(List<PointHybrid> arg0, List<PointHybrid> arg1) {
				// TODO Auto-generated method stub
				return arg1.size()-arg0.size();
			}
		});
		
		System.out.println("starting heuristic");
		HashMap<Double,Integer> map=new HashMap<>();
		int i=0;
		for(List<PointHybrid> segment:segments) {
				int index=0;
				if(segment.size()<100) {
					break;
				}
				for(PointHybrid curPoint:segment) {
					double kDistance=Double.parseDouble(df.format(findKthNearestNeighboursDistance(k, segment,index)));
					if(map.containsKey(kDistance)) {
						map.put(kDistance, map.get(kDistance)+1);
					}else {
						map.put(kDistance, 1);
					}
					index++;
				}
			i++;
			System.out.println(k+" : "+i);
		}
		//create collection
		XYSeries series=new XYSeries(mode);
		for(Double kDistance:map.keySet()) {
			series.add(map.get(kDistance),kDistance);
		}
		
		collection.addSeries(series);
		
		return collection;
	}
	
	static double findKNearestNeighboursAverageDistance(int k,List<PointHybrid> points,int index){
		if(points.size()<k+1) {
			return Double.MAX_VALUE;
		}
		double avgDist=0d;
		//first determine k - nearest neighbors
		//take a window of 2k+1 around the point
		int backProp=index-1;
		double distances[]=new double[2*k];
		int arrIndex=0;
		PointHybrid corePoint=points.get(index);
		while(backProp>=0 && backProp>=(index-k)) {
			//System.out.println("back");
			distances[arrIndex]=PointFunctions.distancePoint(points.get(backProp), corePoint);
			backProp--;
			arrIndex++;
		}
		int forwProp=index+1;
		while(forwProp<points.size() && forwProp<=(index+k)) {
			//System.out.println("for");
			distances[arrIndex]=PointFunctions.distancePoint(points.get(forwProp), corePoint);
			forwProp++;
			arrIndex++;
		}
		while(arrIndex<2*k) {
			distances[arrIndex]=Double.MAX_VALUE;
			arrIndex++;
		}
		Arrays.sort(distances);
		for(arrIndex=0;arrIndex<k;arrIndex++) {
			avgDist+=distances[arrIndex];
		}
		avgDist/=k;
		
		//System.out.println("done");
		
		return avgDist;
	}
	static double findKthNearestNeighboursDistance(int k,List<PointHybrid> points,int index){
		if(points.size()<k+1) {
			return Double.MAX_VALUE;
		}
		double avgDist=0d;
		//first determine k - nearest neighbors
		//take a window of 2k+1 around the point
		int backProp=index-1;
		double distances[]=new double[2*k];
		int arrIndex=0;
		PointHybrid corePoint=points.get(index);
		while(backProp>=0 && backProp>=(index-k)) {
			//System.out.println("back");
			distances[arrIndex]=PointFunctions.distancePoint(points.get(backProp), corePoint);
			backProp--;
			arrIndex++;
		}
		int forwProp=index+1;
		while(forwProp<points.size() && forwProp<=(index+k)) {
			//System.out.println("for");
			distances[arrIndex]=PointFunctions.distancePoint(points.get(forwProp), corePoint);
			forwProp++;
			arrIndex++;
		}
		while(arrIndex<2*k) {
			distances[arrIndex]=Double.MAX_VALUE;
			arrIndex++;
		}
		Arrays.sort(distances);
		
		return distances[k-1];
	}
	static List<XYSeriesCollection> plotStayPointTimeSpent(){
		String mode="bus";
		List<List<PointHybrid>> segments=ReadData.readData(mode);
		int i=0;
		List<XYSeriesCollection> collections=new ArrayList<>();
		
		for(List<PointHybrid> segment:segments) {
		
			XYSeries series=new XYSeries(i);
			List<StayPoint> stayPoints=StayPointDetection.detectStayPoints3(segment, 50, 10);
			int j=0;
			if(stayPoints.size()==0) {
				continue;
			}
			for(StayPoint stayPoint:stayPoints) {
				series.add(j,stayPoint.getTimeSpent()/1000);
				j++;
			}
			if(i==1) {
				break;
			}
			i++;
			XYSeriesCollection collection=new XYSeriesCollection(series);
			collections.add(collection);
		}
		return collections;
	}
	
	static XYSeriesCollection plotStayPointTimeDiff(String mode) {
		List<List<PointHybrid>> segments=ReadData.readData(mode);
		XYSeriesCollection collection=new XYSeriesCollection();
		int i=0;
		for(List<PointHybrid> segment:segments) {
			List<StayPoint> stayPoints=StayPointDetection.detectStayPoints3(segment, 50, 10);
			if(stayPoints.isEmpty()) {
				continue;
			}
			Date lastTime=stayPoints.get(0).getTimeStamp();
			XYSeries series=new XYSeries(i);
			int j=0;
			for(StayPoint sp:stayPoints) {
				series.add(j,(sp.getTimeStamp().getTime()-lastTime.getTime())/1000);
				j++;
				lastTime=sp.getTimeStamp();
			}
			i++;
			collection.addSeries(series);
			
			if(i==1) {
				break;
			}
		}
		return collection;
	}
	static XYSeriesCollection plotStayPointDistanceDiff(String mode) {
		List<List<PointHybrid>> segments=ReadData.readData(mode);
		XYSeriesCollection collection=new XYSeriesCollection();
		int i=0;
		for(List<PointHybrid> segment:segments) {
			List<StayPoint> stayPoints=StayPointDetection.detectStayPoints3(segment, 50, 10);
			if(stayPoints.isEmpty()) {
				continue;
			}
			StayPoint prevPoint=stayPoints.get(0);
			XYSeries series=new XYSeries(i);
			int j=0;
			for(StayPoint sp:stayPoints) {
				series.add(j,PointFunctions.distancePoint(sp.getLatitude(), sp.getLongitude(), prevPoint.getLatitude(), prevPoint.getLongitude())/1000);
				j++;
				prevPoint=sp;
			}
			i++;
			collection.addSeries(series);
			
			if(i==1) {
				break;
			}
		}
		return collection;
	}
	
	static void testStayPointDetection(){
		String mode="car";
		List<List<PointHybrid>> segments=ReadData.readData(mode);
		int i=0;
		for(List<PointHybrid> segment:segments) {
			List<StayPoint> stayPoints=StayPointDetection.detectStayPoints3(segment, 50, 5);
			Gson gson=new Gson();
			String jsonDataStayPoints=gson.toJson(stayPoints);
			String jsonDataActualData=gson.toJson(segment);
			
			File fileSP=new File(DataExtractor.dirName+DataExtractor.extactedDataDirName+"test/car/StayPoints/StayPoint"+i+".json");
			File fileSeg=new File(DataExtractor.dirName+DataExtractor.extactedDataDirName+"test/car/Segments/Segment"+i+".json");
			try {
				BufferedWriter writer=new BufferedWriter(new FileWriter(fileSP));
				writer.write(jsonDataStayPoints);
				writer.flush();
				writer.close();
				writer=new BufferedWriter(new FileWriter(fileSeg));
				writer.write(jsonDataActualData);
				writer.flush();
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(i==1) {
				break;
			}
			i++;
		}
	}
	
	static XYSeriesCollection StayPointAnalysis(String mode) {	
		List<List<PointHybrid>> segments=ReadData.readData(mode);
		List<String> data=new ArrayList<>();
		int i=0;
		XYSeriesCollection collection=new XYSeriesCollection();
		
		for(List<PointHybrid> segment:segments) {
			List<StayPoint> stayPoints=StayPointDetection.detectStayPoints3(segment, 50, 10);
			if(stayPoints.isEmpty()) {
				continue;
			}
			data.add("Segment " + i +" :: Stay Points : " +stayPoints.size());
			collection.addSeries(plotStayPointAnalysis(stayPoints,i));
			i++;
			if(i==1) {
				break;
			}
		}
		return collection;
	}
	
	private static XYSeries plotStayPointAnalysis(List<StayPoint> stayPoints,int key) {
		// TODO Auto-generated method stub
		StayPoint prevPoint=stayPoints.get(0);
		Date startTime=prevPoint.getTimeStamp();
		XYSeries series=new XYSeries(key);
		for(StayPoint stayPoint:stayPoints) {
			series.add((stayPoint.getTimeStamp().getTime()-startTime.getTime())/1000,PointFunctions.distancePoint(stayPoint.getLatitude(), stayPoint.getLongitude(),prevPoint.getLatitude(),prevPoint.getLongitude())/1000);
			prevPoint=stayPoint;
		}
		return series;
	}
	
	private static List<List<XYDataset>> createDatasetForOverlaidGraphStayPoint(String mode){
		List<List<PointHybrid>> segments=ReadData.readData(mode);
		List<List<XYDataset>> setOfDatasets= new ArrayList<>();
		int i=0;
		segments.sort(new Comparator<List<PointHybrid>>() {

			@Override
			public int compare(List<PointHybrid> arg0, List<PointHybrid> arg1) {
				// TODO Auto-generated method stub
				return arg1.size()-arg0.size();
			}
		});
		i=0;
		for(List<PointHybrid> segment:segments) {
			List<XYDataset> datasets=new ArrayList<>();
			XYSeries seriesDistDiff=new XYSeries("Distance Diff");
			XYSeries seriesTimeDiff=new XYSeries("Time Diff");
			XYSeries seriesTimeSpent=new XYSeries("Time Spent");
			XYSeries velocity=new XYSeries("velocity");
			
			List<StayPoint> stayPoints=StayPointDetection.detectStayPoints3(segment, 50, 10);
			if(stayPoints.isEmpty()) {
				continue;
			}
			StayPoint prevSP=stayPoints.get(0);
			int j=0;
			double distance=0d;
			double time=0d;
			
			for(StayPoint sp:stayPoints) {
				time+=((sp.getTimeStamp().getTime()-prevSP.getTimeStamp().getTime())/1000);
				distance+=(PointFunctions.distancePoint(sp.getLatitude(),sp.getLongitude(),prevSP.getLatitude(),prevSP.getLongitude())/1000);
				seriesDistDiff.add(j,distance);
				seriesTimeDiff.add(j,time/60);	
				seriesTimeSpent.add(j,sp.getTimeSpent()/1000);
				velocity.add(j,PointFunctions.velocityPoint(sp, prevSP));
				prevSP=sp;
				j++;
			}
			XYSeriesCollection collection=new XYSeriesCollection();
			
			XYSeriesCollection timeCollection=new XYSeriesCollection();
			//timeCollection.addSeries(seriesTimeSpent);
			timeCollection.addSeries(seriesTimeDiff);
			datasets.add(timeCollection);
			collection.addSeries(seriesDistDiff);
			collection.addSeries(velocity);
			datasets.add(collection);
			
			setOfDatasets.add(datasets);
			if(i==10) {
				break;
			}
			i++;
		}
		return setOfDatasets;
	}
	
}
