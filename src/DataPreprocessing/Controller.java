package DataPreprocessing;

import java.io.File;

import DataExtractionForStudy.DataExtractor;
import pocessing.Datareader;

public class Controller {

	public static String jsonDir="jsonData/";
	public static String jsonFilteredDataDir="FilteredData/";
	
	
	public static void main(String args[]) {
		
		//filter the data
		//write json files and remove points with same timestamp and ensure timestamp order
		for(int i=0;i<182;i++) {
			String userName=Datareader.getFolderName(i);
			File dir=new File(DataExtractor.dirName+DataExtractor.dataDirName+userName+"/Trajectory/");
			File files[]=dir.listFiles();
			File newDir=new File(DataExtractor.dirName+"FilteredData/"+userName+"/");
			newDir.mkdirs();
			for(File file:files) {
				AnomalyDetection.filterData(file, userName);
			}
		}
		/*
		//3 sigma filter
		for(int i=0;i<182;i++) {
			String userName=Datareader.getFolderName(i);
			File dir=new File(DataExtractor.dirName+jsonDir+userName);
			File files[]=dir.listFiles();
			File newDir=new File(DataExtractor.dirName+jsonFilteredDataDir+userName+"/");
			newDir.mkdirs();
			for(File file:files) {
				DataFilter.filterData(file, userName);
			}
		}*/
	}
	
}
