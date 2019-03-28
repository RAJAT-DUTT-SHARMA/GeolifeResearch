package DataExtractionForStudy;

import java.util.List;

import beans.PointHybrid;

public class Testing {

	static double pTrain=0.00355;
	static double pSubway=0.0016;
	static double pCar=0.0012;
	static double pBike=0.00061;
	static double pBus=0.00054;
	static double pAirplane=0.0625;
	
	static double []rangeHCR= {0.05385,0.2062,0.27668,0.2911,0.441,0.51012};
	static String []HCR= {"A","S","Bi","T","Bu","C"};
	
	static double []rangeVCR= {0.007623,0.1168,0.2340,0.2460,0.4725,0.51};
	static String []VCR= {"A","T","C","Bi","Bu","S"};
	
	static double []rangeSR= {0.0075,0.2915,0.58695,0.7770,1.2023,1.330};
	static String []SR= {"A","T","C","Bi","Bu","S"};
	
	String testSegment(List<PointHybrid> segment) {
		String transMode=null;
		
		return transMode;
	}
	
	
	
	
	
	
	
	
	
}
