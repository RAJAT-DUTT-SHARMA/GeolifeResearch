package FeatureStudyExtractedData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import DataExtractionForStudy.DataExtractor;
import beans.PointHybrid;
import pocessing.PointFunctions;
import pocessing.ProcessingUtilities;

public class Plotter {

	static double thresholdVr = 0.26;
	static double thresholdVs = 3.4;
	static double thresholdHc = 19;// degrees

	static int rangesHCRCountBus[] = new int[17], rangesVCRCountBus[] = new int[17], rangesSRCountBus[] = new int[17];
	static int rangesHCRCountBike[] = new int[17], rangesVCRCountBike[] = new int[17],
			rangesSRCountBike[] = new int[17];
	static int rangesHCRCountCar[] = new int[17], rangesVCRCountCar[] = new int[17], rangesSRCountCar[] = new int[17];
	static int rangesHCRCountTrain[] = new int[17], rangesVCRCountTrain[] = new int[17],
			rangesSRCountTrain[] = new int[17];
	static int rangesHCRCountSubway[] = new int[17], rangesVCRCountSubway[] = new int[17],
			rangesSRCountSubway[] = new int[17];
	static int rangesHCRCountAirplane[] = new int[17], rangesVCRCountAirplane[] = new int[17],
			rangesSRCountAirplane[] = new int[17];

	static double rangesHCRPBus[] = new double[17], rangesVCRPBus[] = new double[17], rangesSRPBus[] = new double[17];
	static double rangesHCRPBike[] = new double[17], rangesVCRPBike[] = new double[17],
			rangesSRPBike[] = new double[17];
	static double rangesHCRPCar[] = new double[17], rangesVCRPCar[] = new double[17], rangesSRPCar[] = new double[17];
	static double rangesHCRPTrain[] = new double[17], rangesVCRPTrain[] = new double[17],
			rangesSRPTrain[] = new double[17];
	static double rangesHCRPSubway[] = new double[17], rangesVCRPSubway[] = new double[17],
			rangesSRPSubway[] = new double[17];
	static double rangesHCRPAirplane[] = new double[17], rangesVCRPAirplane[] = new double[17],
			rangesSRPAirplane[] = new double[17];

	public static void main(String[] args) {
		// TODO define features

		List<String> list = new ArrayList<>();
		// get list of all users with labelled data
		File dir = new File(DataExtractor.dirName + DataExtractor.dataDirName);
		File dirs[] = dir.listFiles();
		for (File user : dirs) {
			if (user.listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File arg0, String arg1) {
					if (arg1.equals("labels.txt")) {
						return true;
					}
					return false;
				}
			}).length != 0) {
				list.add(user.getName());
			}
		}

		String users[] = list.toArray(new String[list.size()]);
		String transportationModes[] = { "airplane"};// , "train", "subway", "car", "bike", "airplane" };
		try {
			
			  XYSeriesCollection collectionHCR=new XYSeriesCollection(); 
			  XYSeriesCollection collectionVCR=new XYSeriesCollection(); 
			  XYSeriesCollection collectionSR=new XYSeriesCollection();
			 
			XYSeriesCollection miscCollection = new XYSeriesCollection();

			for (String mode : transportationModes) {
				// 1. plot the velocity for the transportation modes
				// 1.1 read the data
				XYSeries seriesHCR = new XYSeries(mode + "HCR");
				XYSeries seriesVCR = new XYSeries(mode + "VCR");
				XYSeries seriesSR = new XYSeries(mode + "SR");

				dir = new File(DataExtractor.dirName + DataExtractor.extactedDataDirName + mode + "/");
				File files[] = dir.listFiles();
				int m = 0;
				List<List<PointHybrid>> segments = new ArrayList<>();
				for (File file : files) {
					BufferedReader reader = new BufferedReader(new FileReader(file));
					String line = reader.readLine();
					Gson gson = new Gson();

					List<List<PointHybrid>> lists = gson.fromJson(line, new TypeToken<List<List<PointHybrid>>>() {
					}.getType());
					segments.addAll(lists);
				}/*
				plotAverageVelocity(segments,mode);
				plotMinVelocity(segments,mode);
				plotMaxVelocity(segments,mode);*/
				plotVelocityGraph(segments,mode);
				/*
				 * int i = 0;
				for (List<PointHybrid> segment : segments) {
					double HCR = getSegmentHCR(segment);
					double VCR = getSegmentVCR(segment);
					double SR = getSegmentSR(segment);
					switch (mode) {
					case "bus":
						rangesHCRCountBus[(int) (HCR * 10)]++;
						rangesVCRCountBus[(int) (VCR * 10)]++;
						rangesSRCountBus[(int) (SR * 10)]++;
						break;
					case "train":
						rangesHCRCountTrain[(int) (HCR * 10)]++;
						rangesVCRCountTrain[(int) (VCR * 10)]++;
						rangesSRCountTrain[(int) (SR * 10)]++;
						break;
					case "bike":
						rangesHCRCountBike[(int) (HCR * 10)]++;
						rangesVCRCountBike[(int) (VCR * 10)]++;
						rangesSRCountBike[(int) (SR * 10)]++;
						break;
					case "car":
						rangesHCRCountCar[(int) (HCR * 10)]++;
						rangesVCRCountCar[(int) (VCR * 10)]++;
						rangesSRCountCar[(int) (SR * 10)]++;
						break;
					case "subway":
						rangesHCRCountSubway[(int) (HCR * 10)]++;
						rangesVCRCountSubway[(int) (VCR * 10)]++;
						rangesSRCountSubway[(int) (SR * 10)]++;
						break;
					case "airplane":
						rangesHCRCountAirplane[(int) (HCR * 10)]++;
						rangesVCRCountAirplane[(int) (VCR * 10)]++;
						rangesSRCountAirplane[(int) (SR * 10)]++;
						break;
					}
					i++;
				}

				
				  miscCollection.addSeries(seriesSR); 
				  miscCollection.addSeries(seriesVCR);
				  
				  miscCollection.addSeries(seriesHCR);
				  
				  collectionVCR.addSeries(plotVelocityChangeRate(segments, mode));
				  
				  collectionSR.addSeries(plotStopRate(segments, mode));
				  collectionHCR.addSeries(plotHeadingChangeRate(segments, mode));
				*/ 
			}
/*
			for (String mode : transportationModes) {

				switch (mode) {
				case "bus":
					for (int i = 0; i < 17; i++) {
						rangesHCRPBus[i] = rangesHCRCountBus[i]
								/ (rangesHCRCountAirplane[i] + rangesHCRCountBike[i] + rangesHCRCountBus[i]
										+ rangesHCRCountCar[i] + rangesHCRCountTrain[i] + rangesHCRPSubway[i]);
						rangesVCRPBus[i] = rangesVCRCountBus[i]
								/ (rangesVCRCountAirplane[i] + rangesVCRCountBike[i] + rangesVCRCountBus[i]
										+ rangesVCRCountCar[i] + rangesVCRCountTrain[i] + rangesVCRPSubway[i]);
					}
					for (int i = 0; i < 17; i++) {
						rangesSRPBus[i] = rangesSRCountBus[i]
								/ (rangesSRCountAirplane[i] + rangesSRCountBike[i] + rangesSRCountBus[i]
										+ rangesSRCountCar[i] + rangesSRCountTrain[i] + rangesSRPSubway[i]);

					}
					break;
				case "train":
					for (int i = 0; i < 17; i++) {
						rangesHCRPTrain[i] = rangesHCRCountTrain[i]
								/ (rangesHCRCountAirplane[i] + rangesHCRCountBike[i] + rangesHCRCountTrain[i]
										+ rangesHCRCountCar[i] + rangesHCRCountBus[i] + rangesHCRPSubway[i]);
						
						rangesVCRPTrain[i] = rangesVCRCountTrain[i]
								/ (rangesVCRCountAirplane[i] + rangesVCRCountBike[i] + rangesVCRCountTrain[i]
										+ rangesVCRCountCar[i] + rangesVCRCountBus[i] + rangesVCRPSubway[i]);
					}
					for (int i = 0; i < 17; i++) {
						rangesSRPTrain[i] = rangesSRCountTrain[i]
								/ (rangesSRCountAirplane[i] + rangesSRCountBike[i] + rangesSRCountTrain[i]
										+ rangesSRCountCar[i] + rangesSRCountBus[i] + rangesSRPSubway[i]);

					}
					break;
				case "bike":
					for (int i = 0; i < 17; i++) {
						rangesHCRPBike[i] = rangesHCRCountBike[i]
								/ (rangesHCRCountAirplane[i] + rangesHCRCountBike[i] + rangesHCRCountBus[i]
										+ rangesHCRCountCar[i] + rangesHCRCountTrain[i] + rangesHCRPSubway[i]);
						rangesVCRPBike[i] = rangesVCRCountBike[i]
								/ (rangesVCRCountAirplane[i] + rangesVCRCountBike[i] + rangesVCRCountBus[i]
										+ rangesVCRCountCar[i] + rangesVCRCountTrain[i] + rangesVCRPSubway[i]);
					}
					for (int i = 0; i < 17; i++) {
						rangesSRPBike[i] = rangesSRCountBike[i]
								/ (rangesSRCountAirplane[i] + rangesSRCountBike[i] + rangesSRCountBus[i]
										+ rangesSRCountCar[i] + rangesSRCountTrain[i] + rangesSRPSubway[i]);
						
					}
					break;
				case "car":
					for (int i = 0; i < 17; i++) {
						rangesHCRPCar[i] = rangesHCRCountCar[i]
								/ (rangesHCRCountAirplane[i] + rangesHCRCountBike[i] + rangesHCRCountBus[i]
										+ rangesHCRCountCar[i] + rangesHCRCountTrain[i] + rangesHCRPSubway[i]);
						rangesVCRPCar[i] = rangesVCRCountCar[i]
								/ (rangesVCRCountAirplane[i] + rangesVCRCountBike[i] + rangesVCRCountBus[i]
										+ rangesVCRCountCar[i] + rangesVCRCountTrain[i] + rangesVCRPSubway[i]);
					}
					for (int i = 0; i < 17; i++) {
						rangesSRPCar[i] = rangesSRCountCar[i]
								/ (rangesSRCountAirplane[i] + rangesSRCountBike[i] + rangesSRCountBus[i]
										+ rangesSRCountCar[i] + rangesSRCountTrain[i] + rangesSRPSubway[i]);

					}
					break;
				case "subway":
					for (int i = 0; i < 17; i++) {
						rangesHCRPSubway[i] = rangesHCRCountSubway[i]
								/ (rangesHCRCountAirplane[i] + rangesHCRCountBike[i] + rangesHCRCountBus[i]
										+ rangesHCRCountCar[i] + rangesHCRCountTrain[i] + rangesHCRPSubway[i]);
						rangesVCRPSubway[i] = rangesVCRCountSubway[i]
								/ (rangesVCRCountAirplane[i] + rangesVCRCountBike[i] + rangesVCRCountBus[i]
										+ rangesVCRCountCar[i] + rangesVCRCountTrain[i] + rangesVCRPSubway[i]);
					}
					for (int i = 0; i < 17; i++) {
						rangesSRPSubway[i] = rangesSRCountSubway[i]
								/ (rangesSRCountAirplane[i] + rangesSRCountBike[i] + rangesSRCountBus[i]
										+ rangesSRCountCar[i] + rangesSRCountTrain[i] + rangesSRPSubway[i]);
						
					}
					break;
				case "airplane":
					for (int i = 0; i < 17; i++) {
						rangesHCRPAirplane[i] = rangesHCRCountAirplane[i]
								/ (rangesHCRCountAirplane[i] + rangesHCRCountBike[i] + rangesHCRCountBus[i]
										+ rangesHCRCountCar[i] + rangesHCRCountTrain[i] + rangesHCRPSubway[i]);
						rangesVCRPAirplane[i] = rangesVCRCountAirplane[i]
								/ (rangesVCRCountAirplane[i] + rangesVCRCountBike[i] + rangesVCRCountBus[i]
										+ rangesVCRCountCar[i] + rangesVCRCountTrain[i] + rangesVCRPSubway[i]);
					}
					for (int i = 0; i < 17; i++) {
						rangesSRPAirplane[i] = rangesSRCountAirplane[i]
								/ (rangesSRCountAirplane[i] + rangesSRCountBike[i] + rangesSRCountBus[i]
										+ rangesSRCountCar[i] + rangesSRCountTrain[i] + rangesSRPSubway[i]);
					}
					break;
				}

			}
			
			for(String mode:transportationModes) {
				for(int i=0;i<17;i++) {
					switch (mode) {
					case "bus":
						if(Double.isNaN(rangesHCRPBus[i])||Double.isInfinite(rangesHCRPBus[i])) {
							rangesHCRPBus[i]=0;
						}
						if(Double.isNaN(rangesVCRPBus[i])||Double.isInfinite(rangesVCRPBus[i])) {
							rangesVCRPBus[i]=0;
						}
						if(Double.isNaN(rangesSRPBus[i])||Double.isInfinite(rangesSRPBus[i])) {
							rangesSRPBus[i]=0;
						}
						break;
					case "train":
						if(Double.isNaN(rangesHCRPTrain[i])||Double.isInfinite(rangesHCRPTrain[i])) {
							rangesHCRPTrain[i]=0;
						}
						if(Double.isNaN(rangesVCRPTrain[i])||Double.isInfinite(rangesVCRPTrain[i])) {
							rangesVCRPTrain[i]=0;
						}
						if(Double.isNaN(rangesSRPTrain[i])||Double.isInfinite(rangesSRPTrain[i])) {
							rangesSRPTrain[i]=0;
						}
						break;
					case "bike":
						if(Double.isNaN(rangesHCRPBike[i])||Double.isInfinite(rangesHCRPBike[i])) {
							rangesHCRPBike[i]=0;
						}
						if(Double.isNaN(rangesVCRPBike[i])||Double.isInfinite(rangesVCRPBike[i])) {
							rangesVCRPBike[i]=0;
						}
						if(Double.isNaN(rangesSRPBike[i])||Double.isInfinite(rangesSRPBike[i])) {
							rangesSRPBike[i]=0;
						}
						break;
					case "car":
						if(Double.isNaN(rangesHCRPCar[i])||Double.isInfinite(rangesHCRPCar[i])) {
							rangesHCRPCar[i]=0;
						}
						if(Double.isNaN(rangesVCRPCar[i])||Double.isInfinite(rangesVCRPCar[i])) {
							rangesVCRPCar[i]=0;
						}
						if(Double.isNaN(rangesSRPCar[i])||Double.isInfinite(rangesSRPCar[i])) {
							rangesSRPCar[i]=0;
						}
						break;
					case "subway":
						if(Double.isNaN(rangesHCRPSubway[i])||Double.isInfinite(rangesHCRPSubway[i])) {
							rangesHCRPSubway[i]=0;
						}
						if(Double.isNaN(rangesVCRPSubway[i])||Double.isInfinite(rangesVCRPSubway[i])) {
							rangesVCRPSubway[i]=0;
						}
						if(Double.isNaN(rangesSRPSubway[i])||Double.isInfinite(rangesSRPSubway[i])) {
							rangesSRPSubway[i]=0;
						}
						break;
					case "airplane":
						if(Double.isNaN(rangesHCRPAirplane[i])||Double.isInfinite(rangesHCRPAirplane[i])) {
							rangesHCRPAirplane[i]=0;
						}
						if(Double.isNaN(rangesVCRPAirplane[i])||Double.isInfinite(rangesVCRPAirplane[i])) {
							rangesVCRPAirplane[i]=0;
						}
						if(Double.isNaN(rangesSRPAirplane[i])||Double.isInfinite(rangesSRPAirplane[i])) {
							rangesSRPAirplane[i]=0;
						}
						break;
					}
				}
			}
			
			
	/*		
			Gson gson = new Gson();
			List<String> stringList = new ArrayList<>();
			stringList.add(gson.toJson(rangesHCRPBus));
			stringList.add(gson.toJson(rangesHCRPBike));
			stringList.add(gson.toJson(rangesHCRPTrain));
			stringList.add(gson.toJson(rangesHCRPSubway));
			stringList.add(gson.toJson(rangesHCRPCar));
			stringList.add(gson.toJson(rangesHCRPAirplane));
			
			stringList.add(gson.toJson(rangesVCRPBus));
			stringList.add(gson.toJson(rangesVCRPBike));
			stringList.add(gson.toJson(rangesVCRPTrain));
			stringList.add(gson.toJson(rangesVCRPSubway));
			stringList.add(gson.toJson(rangesVCRPCar));
			stringList.add(gson.toJson(rangesVCRPAirplane));
			
			stringList.add(gson.toJson(rangesSRPBus));
			stringList.add(gson.toJson(rangesSRPBike));
			stringList.add(gson.toJson(rangesSRPTrain));
			stringList.add(gson.toJson(rangesSRPSubway));
			stringList.add(gson.toJson(rangesSRPCar));
			stringList.add(gson.toJson(rangesSRPAirplane));
			
			String data=gson.toJson(stringList);
			BufferedWriter writer=new BufferedWriter(new FileWriter(new File(DataExtractor.dirName+"impData.txt")));
			writer.write(data);
			writer.flush();
			writer.close();
	
			for(int i=0;i<17;i++) {
				System.out.println(rangesHCRCountBus[i]);
			}
			*/
			/*
			  GraphPlotter.plotGraphs("VCR", collectionVCR); 
			  GraphPlotter.plotGraphs("SR",collectionSR); 
			  GraphPlotter.plotGraphs("HCR", collectionHCR);
			  
			  GraphPlotter.plotGraphs("Features", miscCollection);
			 */
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
	private static void plotVelocityGraph(List<List<PointHybrid>> segments, String mode) {
		// TODO Auto-generated method stub
		int j=0;
		
		segments.sort(new Comparator<List<PointHybrid>>() {
			public int compare(List<PointHybrid> arg0, List<PointHybrid> arg1) {
				if(arg0.size()>arg1.size()) {
					return -1;
				}else if(arg0.size()==arg1.size()) {
					return 0;
				}
				return 1;
			};
		});
		
		
		for(List<PointHybrid> segment:segments.subList(0, 5)) {
			XYSeriesCollection collection=new XYSeriesCollection();	
			XYSeries series=new XYSeries(mode+j);
			PointHybrid p1=segment.get(0),p2=null;
			Date startTime=p1.getTimestamp();
			for(int i=1;i<segment.size();i++) {
				p2=segment.get(i);
				double timeDiff=(p2.getTimestamp().getTime()-startTime.getTime())/1000;
				if(timeDiff<0) {
					continue;
				}
				series.add(timeDiff,PointFunctions.velocityPoint(p2, p1));
				p1=p2;
			}
			collection.addSeries(series);
			j++;
			GraphPlotter.plotGraphs("Velocity "+mode, collection);
		}
	}



	private static void plotMaxVelocity(List<List<PointHybrid>> segments, String mode) {
		// TODO Auto-generated method stub
		XYSeriesCollection collection=new XYSeriesCollection();
		int j=0;
		XYSeries series=new XYSeries(mode);
		for(List<PointHybrid> segment:segments) {
			int i=0;
			PointHybrid p1=segment.get(0),p2=null;
			double maxVelocity=0;
			for(i=1;i<segment.size();i++) {
				p2=segment.get(i);
				double tempV=PointFunctions.velocityPoint(p2, p1);
				if(tempV>maxVelocity) {
					maxVelocity=tempV;
				}
				p1=p2;
			}
			series.add(j,maxVelocity);
			j++;
		}
		collection.addSeries(series);
		GraphPlotter.plotGraphs(mode + "Max Velocity ", collection);
	
	}

	private static void plotMinVelocity(List<List<PointHybrid>> segments, String mode) {
		// TODO Auto-generated method stub
		XYSeriesCollection collection=new XYSeriesCollection();
		XYSeries series=new XYSeries(mode);
		int j=0;
		for(List<PointHybrid> segment:segments) {
			int i=0;
			PointHybrid p1=segment.get(0),p2=null;
			double minVelocity=0;
			for(i=1;i<segment.size();i++) {
				p2=segment.get(i);
				double tempV=PointFunctions.velocityPoint(p2, p1);
				if(tempV<minVelocity) {
					minVelocity=tempV;
				}
				p1=p2;
			}
			series.add(j,minVelocity);
			j++;
		}
		collection.addSeries(series);
		GraphPlotter.plotGraphs(mode + "Min Velocity ", collection);
		
	}

	private static void plotAverageVelocity(List<List<PointHybrid>> segments, String mode) {
		// TODO Auto-generated method stub
		XYSeriesCollection collection=new XYSeriesCollection();
		int j=0;
		XYSeries series=new XYSeries(mode);
		for(List<PointHybrid> segment:segments) {
			series.add(j,ProcessingUtilities.averageSpeedSegment(segment));
			j++;
		}
		collection.addSeries(series);
		GraphPlotter.plotGraphs(mode + "Average Velocity ", collection);
	
	}

	private static XYSeries plotVelocityChangeRate(List<List<PointHybrid>> segments, String mode) {
		// TODO Auto-generated method stub
		XYSeries series = new XYSeries(mode);

		HashMap<Double, Integer> map = new HashMap<>();

		for (int k = 0; k < segments.size(); k++) {
			List<PointHybrid> list = segments.get(k);
			if (list.size() < 20) {
				continue;
			}
			double VCR = getSegmentVCR(list);
			// VCR=Math.round(VCR*1000)/1000.0d;

			if (map.containsKey(VCR)) {
				map.put(VCR, map.get(VCR) + 1);
			} else {
				map.put(VCR, 1);
			}
			// series.add(k,VCR);
			k++;
		}

		for (double VCR : map.keySet()) {
			double probability = ((double) map.get(VCR)) / (double) segments.size();
			// System.out.println(map.get(VCR)+" " + segments.size()+" "+ probability);
			series.add(VCR, probability);
		}
		// collection.addSeries(series);
		// GraphPlotter.plotGraphs(mode+" VCR ", collection);
		return series;
	}

	public static double getSegmentVCR(List<PointHybrid> list) {

		Date startTime = list.get(0).getTimestamp();
		PointHybrid p2, p1 = list.get(0);
		int count = 0;
		double v2 = 0, v1;
		boolean v2Set = false;
		double vRate = 0;
		for (int l = 1; l < list.size(); l++) {
			p2 = list.get(l);
			double t = (p2.getTimestamp().getTime() - startTime.getTime()) / 1000;
			double v = PointFunctions.velocityPoint(p2, p1);

			if (t <= 0) {
				continue;
			}
			if (!v2Set) {
				v2 = v;
				v2Set = true;
			} else {
				v1 = v2;
				v2 = v;
				vRate = ((v2 - v1) / v1);
				if (vRate > thresholdVr) {
					count++;
				}
			}

			p1 = p2;
		}
		return count / ProcessingUtilities.distanceSegment(list);
	}

	static XYSeries plotStopRate(List<List<PointHybrid>> lists, String mode) {
		// XYSeriesCollection collection=new XYSeriesCollection();
		XYSeries series = new XYSeries(mode);
		HashMap<Double, Integer> map = new HashMap<>();

		for (int k = 0; k < lists.size(); k++) {
			List<PointHybrid> list = lists.get(k);
			if (list.size() < 20) {
				continue;
			}

			double v0Rate = getSegmentSR(list);

			// v0Rate=Math.round(v0Rate*1000)/1000.0d;

			if (map.containsKey(v0Rate)) {
				map.put(v0Rate, map.get(v0Rate) + 1);
			} else {
				map.put(v0Rate, 1);
			}
			// series.add(k,v0Rate);
			// System.out.println(mode+" "+k+" v0Rate : "+v0Rate);
			k++;
		}
		// collection.addSeries(series);
		// GraphPlotter.plotGraphs(mode+" STOP RATE ", collection);
		for (double v0Rate : map.keySet()) {
			double probability = (double) map.get(v0Rate) / (double) lists.size();
			series.add(v0Rate, probability);
		}

		return series;
	}

	public static double getSegmentSR(List<PointHybrid> list) {
		Date startTime = list.get(0).getTimestamp();
		PointHybrid p2, p1 = list.get(0);
		int count = 0;
		for (int l = 1; l < list.size(); l++) {
			p2 = list.get(l);
			double t = (p2.getTimestamp().getTime() - startTime.getTime()) / 1000;
			double v = PointFunctions.velocityPoint(p2, p1);
			if (t <= 0) {
				continue;
			}
			if (v < thresholdVs) {
				count++;
			}
			p1 = p2;
		}
		return count / ProcessingUtilities.distanceSegment(list);

	}

	private static XYSeries plotHeadingChangeRate(List<List<PointHybrid>> segments, String mode) {
		// TODO Auto-generated method stub
		// XYSeriesCollection collection=new XYSeriesCollection();
		XYSeries series = new XYSeries(mode);
		HashMap<Double, Integer> map = new HashMap<>();
		for (int k = 0; k < segments.size(); k++) {
			List<PointHybrid> list = segments.get(k);

			if (list.size() < 20) {
				continue;
			}
			double HCR = getSegmentHCR(list);
			// HCR=Math.round(HCR*1000)/1000.0d;
			if (map.containsKey(HCR)) {
				map.put(HCR, map.get(HCR) + 1);
			} else {
				map.put(HCR, 1);
			}
			// series.add(k,HCR);
			// System.out.println(mode+" "+k+" v0Rate : "+v0Rate);
			k++;
		}
		// collection.addSeries(series);
		// GraphPlotter.plotGraphs(mode+" HCR ", collection);
		for (double HCR : map.keySet()) {
			double probability = (double) map.get(HCR) / (double) segments.size();
			series.add(HCR, probability);
		}
		return series;
	}

	public static double getSegmentHCR(List<PointHybrid> list) {

		Date startTime = list.get(0).getTimestamp();
		PointHybrid p2, p1 = list.get(0);
		int count = 0;
		double v2 = 0, v1;
		boolean v2Set = false;
		double vRate = 0;
		for (int l = 1; l < list.size(); l++) {
			p2 = list.get(l);
			double t = (p2.getTimestamp().getTime() - startTime.getTime()) / 1000;
			double angle = PointFunctions.getAngle(p2.getLatitude(), p1.getLatitude(), p2.getLongitude(),
					p1.getLongitude());

			if (t <= 0) {
				continue;
			}
			if (angle > thresholdHc) {
				count++;
			}
			p1 = p2;
		}
		double HCR = count / ProcessingUtilities.distanceSegment(list);
		return HCR;

	}
}
