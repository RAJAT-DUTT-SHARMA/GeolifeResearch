package DataExtractionForStudy;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> list=new ArrayList<>();
		//get list of all users with labelled data
		File dir=new File(DataExtractor.dirName+DataExtractor.dataDirName);
		File dirs[]=dir.listFiles();
		for(File user : dirs) {
			if(user.listFiles(new FilenameFilter() {
				
				@Override
				public boolean accept(File arg0, String arg1) {
					if(arg1.equals("labels.txt")) {
						return true;
				}
					return false;
				}
			}).length!=0) {
				list.add(user.getName());
			}
		}
		
		
		
		
		
		String users[]= list.toArray(new String[list.size()]);
		
		String transportationModes[]= {"bus","train","subway","car","taxi","bike","airplane"};
		for(String user : users) {
			for(String transportationMode:transportationModes) {
				DataExtractor.getSegments(user, transportationMode);
			}
		}
	}

}
