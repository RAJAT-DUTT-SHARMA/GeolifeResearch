package pocessing;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class Identifier {

	public static void main(String args[]) {
		Datareader datareader=new Datareader();
		//read data one by one for every user and then call the segment function on it
		try {
			startProcessing(datareader);
		
		
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	private static void startProcessing(Datareader datareader) throws IOException, ParseException {
		//read data
		for(int i=0;i<182;i++) {
			String userName=Datareader.getFolderName(i);
			File dir=new File(Datareader.directoryPath+userName+"/Trajectory/");
			File files[]=dir.listFiles();
			for(File file:files) {
				List<PointHybrid> list=datareader.readData(file);		
			}
		}
	}
	
	
}
