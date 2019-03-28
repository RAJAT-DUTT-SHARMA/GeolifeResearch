package FeatureStudyExtractedData;

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

public class ReadData {

	static List<List<PointHybrid>> readData(String mode) {

		try {
			File dir = new File(DataExtractor.dirName + DataExtractor.extactedDataDirName + mode + "/");
			File files[] = dir.listFiles();
			int m = 0;
			List<List<PointHybrid>> segments = new ArrayList<>();
			for (File file : files) {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = reader.readLine();
				reader.close();
				Gson gson = new Gson();
				List<List<PointHybrid>> lists = gson.fromJson(line, new TypeToken<List<List<PointHybrid>>>() {
				}.getType());
				segments.addAll(lists);
				//System.out.println(file.getName());
//				writeDataAgain(preprocessSegment(segments),file);	
	//			segments=new ArrayList<>();
			}
			return segments;
		}catch(Exception e) {
			System.out.println(e);
			return null;
		}

	}

	private static void writeDataAgain(List<List<PointHybrid>> preprocessSegment, File file) throws IOException {
		// TODO Auto-generated method stub
		BufferedWriter writer=new BufferedWriter(new FileWriter(file,false));
		Gson gson=new Gson();
		String jsonData=gson.toJson(preprocessSegment);
		writer.write(jsonData);
		writer.flush();
		writer.close();
	}

	private static List<List<PointHybrid>> preprocessSegment(List<List<PointHybrid>> segments) {
		// just divide separate the points are very far from each other
		//if points are more than 20 min apart
		List<List<PointHybrid>> newSegmentList=new ArrayList<>();
		for(List<PointHybrid> segment:segments) {
			List<PointHybrid> newSegment=new ArrayList<>();
			
			PointHybrid prevPoint=segment.get(0);
			newSegment.add(prevPoint);
		
			for(PointHybrid curPoint:segment) {
				if(prevPoint.getTimestamp().compareTo(curPoint.getTimestamp())!=0) {
					newSegment.add(curPoint);
					prevPoint=curPoint;
				}
				
			}
			if(!newSegment.isEmpty()&& newSegment.size()>100) {
				newSegmentList.add(newSegment);
			}
		}
		return newSegmentList;
	}

	public static void main(String args[]) {
		String modes[]= {"bus","car","bike","airplane","train","subway"};
		for(String mode:modes) {
			readData(mode);
		}
	}
}

