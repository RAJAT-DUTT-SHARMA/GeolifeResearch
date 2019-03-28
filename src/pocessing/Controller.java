package pocessing;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import beans.PointHybrid;

public class Controller {

	public static void main(String args[]) {
		//first read data and send for labelling
		try {
			List<PointHybrid> points=Datareader.readData(new File(Datareader.directoryPath+"179/20081011100107.plt"));
			List<Boolean>labels=ProcessingUtilities.labelData(points, 9, 10);
			ProcessingUtilities.segmentation(points, labels, 9, 10);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {;
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
