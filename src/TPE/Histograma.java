package TPE;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class Histograma {

    private Double[] histogram;
    private String name;

    public Histograma(Double[] histogram, String name) {
        this.histogram = histogram;
        this.name = name;
    }

    public void saveAsPNG() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 0; i < histogram.length; i++) {
            if (!histogram[i].equals(0d))
                dataset.setValue(histogram[i]*100, "tonos", (Integer) i);
        }

        JFreeChart chart = ChartFactory.createBarChart3D
                ("Histograma " + name, "tonos de grises", "porcentaje", dataset, PlotOrientation.VERTICAL, false, false, false);

        chart.setBackgroundPaint(Color.black);
        chart.getTitle().setPaint(Color.white);
        chart.getCategoryPlot().setRangeGridlinePaint(Color.black);

        BufferedImage bufferImage = chart.createBufferedImage(1366,768);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            ImageIO.write(bufferImage, "png", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] byteArr = baos.toByteArray();
        InputStream ins = new ByteArrayInputStream(byteArr);
        BufferedImage image = null;

        try {
            image = ImageIO.read(ins);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File outputfile = new File("Histograma_"+name+".png");

        try {
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
