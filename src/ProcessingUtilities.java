import java.util.ArrayList;
import java.util.List;

public class ProcessingUtilities {

	static float velocityThreshold=1.8f;	// 	m/s
	static float accelerrationThreshold=0.6f;	// 	m/s2
	static double segmentDistanceThreshold=50d ;
	
	
	static List<Boolean> labelData(List<PointHybrid> points,int M,int N){
		//in this part we label the gps points to walk or non walk points
		//this will be done based on the threshold velocity and acceleration values.
		
		//label the walk and non walk points in the list
		//first calculate velocity and acceleration values for all the point
		List<Boolean> walkPoint=new ArrayList<>();
		int i=0;
		PointHybrid p1=points.get(i),p2;
		for(i=1;i<points.size();i++) {
			p2=points.get(i);
			
			//now get the velocity and acceleration at each point and label them walk or non walk points
			if(PointFunctions.velocityPoint(p2, p1)<1.8d && PointFunctions.accelerationPoints(p2, p1)<0.6d){
				//walk point 
				walkPoint.add(true);
			}else {
				//label as non walk points
				walkPoint.add(false);
			}
			p1=p2;
		}
		walkPoint.add(true);
		
		//pre-labeling done
		//now making robust labeling using neighborhood
		// a point is a walk point in m out of its n predecessors and successors are the walk points
		float ratio=M/N;
		boolean labelChange=true;
		while(labelChange) {
			int pointer=0;
			labelChange=false;
			
			while(pointer<points.size()) {
				boolean pointWalk=walkPoint.get(pointer);
				
				//check predecessors 
				int predecessorCount=0;
				int temp=pointer-1;
				int counter1=0;
				while(temp>=(pointer-N) && temp>=0) {
					if(walkPoint.get(temp)==pointWalk) {
						predecessorCount++;
					}
					temp--;
					counter1++;
				}
				//check successors
				int successorCount=0;
				temp=pointer+1;
				int counter2=0;
				while(temp<=(pointer+N) && temp<points.size()) {
					if(walkPoint.get(temp)==pointWalk) {
						successorCount++;
					}
					temp++;
					counter2++;
				}
			//now condition checking
			if(counter1!=0 && counter2!=0) {
				if((predecessorCount/counter1)>=ratio && (successorCount/counter2)>=ratio) {
					//current point labeling is right
				}else if(((counter1-predecessorCount)/counter1)>=ratio && ((counter2-successorCount)/counter2)>=ratio) {
					//current point labeling is wrong
					walkPoint.set(pointer,  !pointWalk);
					labelChange=true;
				}
			}
				pointer++;
			}
		}
		
		return walkPoint;
	}
	
	
	public static List<List<PointHybrid>> segmentation(List<PointHybrid> points,List<Boolean> labels,int M,int N) {
		//here we shall divide the trip or gps trajectory into walk and non walk segments 
		//steps 
		//1. get candidate transition points (points with sudden and consistent change in velocity )
		List<Boolean> candidateTransitionPoint=new ArrayList<>();
		int pointer=N;
		int nonWalkPre,nonWalkSuc;
		for(int i=0;i<N;i++) {
			candidateTransitionPoint.add(false);
		}
		while(pointer<(points.size()-(N))) {
			nonWalkPre=0;
			//check predecessors 
			int temp=pointer-1;
			while(temp>=(pointer-N)) {
				if(labels.get(temp)==false) {
					nonWalkPre++;
				}
				temp--;
			}
		
			//check successors
			temp=pointer+1;
			nonWalkSuc=0;
			while(temp<=(pointer+N)) {
				if(labels.get(temp)==false) {
					nonWalkSuc++;
				}
				temp++;
			}
		//now condition checking
			if(nonWalkPre>=M && (N-nonWalkSuc)>=M ) {
				candidateTransitionPoint.add(true);
				
			}else if ((N-nonWalkPre)>=M && nonWalkSuc>=M) {
				candidateTransitionPoint.add(true);
			}else{
				candidateTransitionPoint.add(false);
			}
			
			pointer++;
		}
		for(int i=0;i<N;i++) {
			candidateTransitionPoint.add(false);
		}
		
		//2. divide the trajectory into segments based on candidate transition points and time difference between two points (<20mins)
		
		List<List<PointHybrid>> segments=new ArrayList<>();
		ArrayList<PointHybrid> segment=new ArrayList<>();
		PointHybrid p1,p2;
		for(int i=0;i<points.size();i++) {
			segment.add(points.get(i));
			if(candidateTransitionPoint.get(i)==true || (i>0 && ((points.get(i).getTimestamp().getTime()-points.get(i-1).getTimestamp().getTime())/60000)>20)) {
				segments.add(segment);
				segment=new ArrayList<>();
				segment.add(points.get(i));
			}
		}
		segments.add(segment);
		int labelPointer=0;
		int j=0;
		for(List<PointHybrid> list:segments) {
			System.out.println("Segment " +j+" : "+labelWalkSegments(labels.subList(labelPointer, labelPointer+list.size())));
			labelPointer+=(list.size()-1);
			j++;
		}
		
		//2. merge segments with distance < dthreshold
		List<List<PointHybrid>> mergedSegments=new ArrayList<>();
		
		int lastSegmentPointer=0;
		List<PointHybrid> list=segments.get(0);
		
		for(int i=1;i<segments.size();i++) {
			if(distanceSegment(segments.get(i))<20) {
				list.addAll(segments.get(i).subList(1, segments.get(i).size()));
			}else {
				mergedSegments.add(list);
				list=segments.get(i);
				lastSegmentPointer++;
			}
		}
		mergedSegments.add(list);
		
		int i=0;
		int pointerForLabels=0;
		for(List<PointHybrid> seg:mergedSegments) {
			System.out.println("Segment"+i+" distance : "+distanceSegment(seg)+"  Avg. Velocity: "+averageSpeedSegment(seg)+"  Label : "+labelWalkSegments(labels.subList(pointerForLabels, pointerForLabels+seg.size())));
			i++;
			pointerForLabels+=(seg.size()-1);
		}
		
		return null;
	}
	
	
	static String labelWalkSegments(List<Boolean> labels) {
		int walk=0;
		for(boolean label:labels) {
			if(label==true) {
				walk++;
			}
		}
		if(walk>(labels.size()-walk)) {
			return "walk";
		}
		return "non-walk";
	}
	
	
	static double averageSpeedSegment(List<PointHybrid> segment){
		if(segment.size()==0) {
			return 0d;
		}
		double velocitySum=0d;
		PointHybrid p1,p2;
		p1=segment.get(0);
		for(int i=1;i<segment.size();i++) {
			p2=segment.get(i);
			velocitySum+=PointFunctions.velocityPoint(p2, p1);
			p1=p2;
		}
		return velocitySum/segment.size();
		
	}
	
	
	static double distanceSegment(List<PointHybrid> segment){
		int i=0;
		PointHybrid p1=segment.get(i),p2;
		double distance=0d;
		for(i=1;i<segment.size();i++) {
			p2=segment.get(i);
			distance+=PointFunctions.distancePoint(p2, p1);
			p1=p2;
		}
		return distance;
	}
	
	
	
}
