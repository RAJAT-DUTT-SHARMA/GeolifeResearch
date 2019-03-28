package FeatureStudyExtractedData;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;

/**
 * An example of a time series chart.  For the most part, default settings are used, except that
 * the renderer is modified to show filled shapes (as well as lines) at each data point.
 *
 */
public class DualAxisChart extends ApplicationFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * A demonstration application showing how to create a time series chart with dual axes.
     *
     * @param title  the frame title.
	 * @param xyDataset2 
	 * @param xyDataset 
     */
    public DualAxisChart(final String title, XYDataset xyDataset, XYDataset xyDataset2) {

        super(title);

        
        // create a title...
        final String chartTitle = "Dual Axis Demo 2";

        final JFreeChart chart = ChartFactory.createXYLineChart(
            chartTitle, 
            "Stay Point Number ", 
            "Time (mins)",
            xyDataset,PlotOrientation.VERTICAL,true,true,false
        );
        

        //final StandardLegend legend = (StandardLegend) chart.getLegend();
        //legend.setDisplaySeriesShapes(true);

        
        final XYPlot plot = chart.getXYPlot();
        
        final NumberAxis axis2 = new NumberAxis("Dist Diff (kilo metres)");
        
        axis2.setAutoRangeIncludesZero(true);
        plot.setRangeAxis(1, axis2);
        plot.setDataset(1, xyDataset2);
        plot.mapDatasetToRangeAxis(1, 1);
        
        plot.setDomainGridlinesVisible(true);
       
        XYLineAndShapeRenderer rendr=(XYLineAndShapeRenderer) plot.getRenderer();
        rendr.setBaseShapesVisible(true);
        
        XYLineAndShapeRenderer renderer2=new XYLineAndShapeRenderer();
        
      //  rendr.setSeriesItemLabelsVisible(0, true);
    //    rendr.setSeriesItemLabelsVisible(1, true);

       
        renderer2.setSeriesPaint(0, Color.black);
        renderer2.setBaseShapesVisible(true);
  //      renderer2.setSeriesItemLabelsVisible(0, true);

        XYItemLabelGenerator labelGenerator=new XYItemLabelGenerator() {
			
			@Override
			public String generateLabel(XYDataset arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub
				String text=arg0.getY(arg1, arg2)+"";
				
				return text.length()>5?text.substring(0,5):text;
			}
		};
        
        //renderer2.setItemLabelGenerator(labelGenerator);
        //rendr.setItemLabelGenerator(labelGenerator);
        
        plot.setRenderer(1, renderer2);
        ValueAxis axis=plot.getDomainAxis();
        
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
        
    }
}
