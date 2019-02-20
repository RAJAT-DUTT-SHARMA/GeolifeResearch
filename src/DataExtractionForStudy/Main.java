package DataExtractionForStudy;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String users[]= {"010","020","056","052","056","062"};
		String transportationModes[]= {"bus","train","subway","car","taxi","bike","airplane"};
		for(String user : users) {
			for(String transportationMode:transportationModes) {
				DataExtractor.getSegments(user, transportationMode);
			}
		}
	}

}
