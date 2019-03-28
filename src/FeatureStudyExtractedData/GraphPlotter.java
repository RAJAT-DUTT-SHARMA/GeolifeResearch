package FeatureStudyExtractedData;
import java.awt.Color;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;


public class GraphPlotter extends JFrame {
		  private static final long serialVersionUID = 6294689542092367723L;

		  XYSeriesCollection datasets;
		  
		  public GraphPlotter(String title,XYSeriesCollection datasets2) {
		    super(title);

		    
		    // Create chartl
		    JFreeChart chart = ChartFactory.createXYLineChart(
		        title, 
		        "timediff(secs)", "velocity(m/s)",datasets2,PlotOrientation.VERTICAL,true,true,false);

		    
		    //Changes background color
		    XYPlot plot = (XYPlot)chart.getPlot();
		    plot.setBackgroundPaint(new Color(255,228,196));
		    XYLineAndShapeRenderer renderer=(XYLineAndShapeRenderer) plot.getRenderer();
		    renderer.setBaseShapesVisible(true);
		    renderer.setBaseItemLabelsVisible(true);
		    renderer.setShapesVisible(true);
		    plot.setRenderer(renderer);
		 
		    // Create Panel
		    ChartPanel panel = new ChartPanel(chart);
		    setContentPane(panel);		   
		    
		    /* 
		    //Changes background color
		    XYPlot plot = (XYPlot)chart.getPlot();
		    plot.setBackgroundPaint(new Color(0,0,0));
		  
		    plot.setDataset(0, datasets2.get(0));
		    plot.setDataset(1, datasets2.get(1));
		    plot.setDataset(2, datasets2.get(2));
		    plot.setDataset(3, datasets2.get(3));
		    plot.setDataset(4, datasets2.get(4));
		    plot.setDataset(5, datasets2.get(5));
		    plot.setDataset(6, datasets2.get(6));
		    
		    
		    
		    XYLineAndShapeRenderer renderer=new XYLineAndShapeRenderer();
		    
		    
		    try {
				ChartUtilities.saveChartAsJPEG(new File("/home/errajatds/Desktop/"+title+".jpg"), chart, 1366, 768);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    /*
		    
		    ChartPanel chartPanel=new ChartPanel(chart);
		    chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
		      
		    setContentPane( chartPanel );
		 */
		    }

		  
		  public GraphPlotter(String title,XYSeriesCollection dataset,String type,String xTitile,String yTitle) {
			    super(title);
			    JFreeChart chart=null;
			    if(type.equals("scatter")) {
			    	// Create chartl
				    chart = ChartFactory.createScatterPlot(
				        title, 
				        xTitile, yTitle,dataset);
			    }else {
			    
			    	chart = ChartFactory.createXYLineChart(
					        title, 
					        xTitile, yTitle,dataset,PlotOrientation.VERTICAL,true,true,false);
			    }
			    
			    
			    
			    //Changes background color
			    XYPlot plot = (XYPlot)chart.getPlot();
			    plot.setBackgroundPaint(new Color(255,228,196));
			    XYLineAndShapeRenderer renderer=(XYLineAndShapeRenderer) plot.getRenderer();
			    renderer.setBaseShapesVisible(true);
			    renderer.setBaseItemLabelsVisible(true);
			    renderer.setShapesVisible(true);
			        plot.setRenderer(renderer);
			   
			    // Create Panel
			    ChartPanel panel = new ChartPanel(chart);
			    setContentPane(panel);		   
		  
		  }
		  
		  
		  
		  
		  public static void plotScatterChart(String title , XYSeriesCollection collection,String xT,String yT){
			  SwingUtilities.invokeLater(new Runnable() {	
					public void run() {
							GraphPlotter example = new GraphPlotter(title,collection,"scatter",xT,yT);				      
							 example.pack( );          
						      RefineryUtilities.centerFrameOnScreen( example ); 
							example.setVisible(true);
					}
				});	
				  
		  }
		  
		  public static void plotXYLineChart(final String title,final XYSeriesCollection datasets,String xT,String yT) {
				
				SwingUtilities.invokeLater(new Runnable() {	
				public void run() {
						GraphPlotter example = new GraphPlotter(title,datasets,"line",xT,yT);				      
						 example.pack( );          
					      RefineryUtilities.centerFrameOnScreen( example ); 
						example.setVisible(true);
				}
				});	
			}
		  
		  public static void plotGraphs(final String title,final XYSeriesCollection datasets) {
				
				SwingUtilities.invokeLater(new Runnable() {	
				public void run() {
						GraphPlotter example = new GraphPlotter(title,datasets);				      
						 example.pack( );          
					      RefineryUtilities.centerFrameOnScreen( example ); 
						example.setVisible(true);
				}
				});	
			}


		public static void plotXYLineChartForCollections(String title, List<XYSeriesCollection> datasets,
				String xT, String yT) {
				for(int i=0;i<datasets.size();i++) {
					plotXYLineChart(title, datasets.get(i), xT, yT);
				}
			
			
		}
		  
		  
		  
		  
}