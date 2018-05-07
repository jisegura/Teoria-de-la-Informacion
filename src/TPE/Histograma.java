package TPE;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Histograma {

    private ArrayList<Double[]> histograms;
    private ArrayList<String> names;
    private ArrayList<Double> medias;
    private ArrayList<Double> desvios;

    public Histograma() {
        this.histograms = new ArrayList<>();
        this.names = new ArrayList<>();
        this.medias = new ArrayList<>();
        this.desvios = new ArrayList<>();
    }

    public void addHistograma(Double[] histogram, String name, Double media, Double desvio) {
        this.histograms.add(histogram);
        this.names.add(name);
        this.medias.add(media);
        this.desvios.add(desvio);
    }

    public void saveAsPNG() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        Iterator<Double[]> it_his = histograms.iterator();
        Iterator<String> it_name = names.iterator();
        Iterator<Double> it_media = medias.iterator();
        Iterator<Double> it_desvio = desvios.iterator();

        while (it_his.hasNext()) {
            Double[] histograma = it_his.next();
            String name = it_name.next();
            String media = String.format("%.2f", it_media.next());
            String desvio = String.format("%.2f", it_desvio.next());
            String rowKey = name + ":\nmedia: " + media + "\ndesvio: " + desvio;

            for (int i = 0; i < histograma.length; i++) {
                if (!histograma[i].equals(0d)) {
                    dataset.addValue(histograma[i] * 100, rowKey, (Integer) i);
                }
            }
        }

        JFreeChart chart = ChartFactory.createBarChart3D
                ("Histograma", "Tonos de grises", null, dataset,
                        PlotOrientation.VERTICAL, true, false, false);

        chart.setBackgroundPaint(Color.black);
        chart.getTitle().setPaint(Color.white);
        chart.getCategoryPlot().setRangeGridlinePaint(Color.black);

        this.savePNG(chart.createBufferedImage(1366,768));

    }

    private void savePNG(BufferedImage imagen) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            ImageIO.write(imagen, "png", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] byteArr = baos.toByteArray();
        InputStream ins = new ByteArrayInputStream(byteArr);
        BufferedImage img = null;

        try {
            img = ImageIO.read(ins);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File outputfile = new File("Histograma_(salida_ej02).png");

        try {
            ImageIO.write(img, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
