package FeatureStudyExtractedData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import DataExtractionForStudy.DataExtractor;
import beans.PointHybrid;

public class Testing {

	static double[] rangeHCR = { 0.05385, 0.2062, 0.27668, 0.2911, 0.441, 0.51012 };
	static String[] HCRSt = { "A", "S", "Bi", "T", "Bu", "C" };

	static double[] rangeVCR = { 0.007623, 0.1168, 0.2340, 0.2460, 0.4725, 0.51 };
	static String[] VCRSt = { "A", "T", "C", "Bi", "Bu", "S" };

	static double[] rangeSR = { 0.0075, 0.2915, 0.58695, 0.7770, 1.2023, 1.330 };
	static String[] SRSt = { "A", "T", "C", "Bi", "Bu", "S" };

	static HashMap<String, Integer> map;
	static {
		map = new HashMap<>();
		map.put("A", 0);
		map.put("T", 1);
		map.put("C", 2);
		map.put("Bi", 3);
		map.put("Bu", 4);
		map.put("S", 5);
	}

	static double pTransModes[] = { 0.0625, 0.00355, 0.0012, 0.00061, 0.00054, 0.0016 };

	public static void main(String args[]) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(DataExtractor.dirName + "impData.txt")));
			String data = reader.readLine();
			reader.close();
			Gson gson = new Gson();
			List<String> pData = gson.fromJson(data, new TypeToken<List<String>>() {
			}.getType());
			List<Double[]> dataP = new ArrayList<>();
			for (String dataString : pData) {
				dataP.add(gson.fromJson(dataString, new TypeToken<Double[]>() {
				}.getType()));
			}

			String mode = "train";
			File dir = new File(DataExtractor.dirName + DataExtractor.extactedDataDirName + mode + "/");
			File files[] = dir.listFiles();
			int m = 0;
			List<List<PointHybrid>> segments = new ArrayList<>();

			for (File file : files) {
				reader = new BufferedReader(new FileReader(file));

				String line = reader.readLine();

				List<List<PointHybrid>> lists = gson.fromJson(line, new TypeToken<List<List<PointHybrid>>>() {
				}.getType());
				segments.addAll(lists);
			}

			int count = 0;
			for (List<PointHybrid> segment : segments) {
				String transMode[]=testSegment(segment, dataP);
				if (transMode[0].equals(mode)||transMode[1].equals(mode)) {
					count++;
				}
			}
			System.out.println((double)count/(double)segments.size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static String[] testSegment(List<PointHybrid> segment, List<Double[]> dataP) {
		double HCR = Plotter.getSegmentHCR(segment);
		double VCR = Plotter.getSegmentVCR(segment);
		double SR = Plotter.getSegmentSR(segment);

		double probTransModeArr[] = { 0d, 0d, 0d, 0d, 0d, 0d };

		// HCR Contribution
		probTransModeArr[0] += dataP.get(5)[(int) (HCR * 10)];
		probTransModeArr[1] += dataP.get(2)[(int) (HCR * 10)];
		probTransModeArr[2] += dataP.get(4)[(int) (HCR * 10)];
		probTransModeArr[3] += dataP.get(1)[(int) (HCR * 10)];
		probTransModeArr[4] += dataP.get(0)[(int) (HCR * 10)];
		probTransModeArr[5] += dataP.get(3)[(int) (HCR * 10)];

		// VCR Contribution
		probTransModeArr[0] += dataP.get(11)[(int) (VCR * 10)];
		probTransModeArr[1] += dataP.get(8)[(int) (VCR * 10)];
		probTransModeArr[2] += dataP.get(10)[(int) (VCR * 10)];
		probTransModeArr[3] += dataP.get(7)[(int) (VCR * 10)];
		probTransModeArr[4] += dataP.get(6)[(int) (VCR * 10)];
		probTransModeArr[5] += dataP.get(9)[(int) (VCR * 10)];

		// SR Contribution
		probTransModeArr[0] += dataP.get(17)[(int) (SR * 10)];
		probTransModeArr[1] += dataP.get(14)[(int) (SR * 10)];
		probTransModeArr[2] += dataP.get(16)[(int) (SR * 10)];
		probTransModeArr[3] += dataP.get(13)[(int) (SR * 10)];
		probTransModeArr[4] += dataP.get(12)[(int) (SR * 10)];
		probTransModeArr[5] += dataP.get(15)[(int) (SR * 10)];

		double max=probTransModeArr[0];
		int index = 0;
		
		for(int i=1;i<6;i++) {
			if(probTransModeArr[i]>max) {
				max=probTransModeArr[i];
				index=i;
			}
		}
		int index2=(index!=0)?0:1;
		double max2=(index!=0)?probTransModeArr[0]:probTransModeArr[1];
		for(int i=0;i<6;i++) {
			if(index==i) {
				continue;
			}
			if(probTransModeArr[i]>max2) {
				max2=probTransModeArr[i];
				index2=i;
			}
		}
		
		String transMode[]=new String[2];
		transMode[0]=getTransportMode(index);
		transMode[1]=getTransportMode(index2);
		
		return transMode;
	}

	private static String getTransportMode(int index) {
		String transMode1=null;
		switch (index) {
		case 0:
			transMode1 = "airplane";
			break;
		case 1:
			transMode1 = "train";
			break;
		case 2:
			transMode1 = "car";
			break;
		case 3:
			transMode1 = "bike";
			break;
		case 4:
			transMode1 = "bus";
			break;
		case 5:
			transMode1 = "subway";
			break;

		}
		return transMode1;
	}

}
