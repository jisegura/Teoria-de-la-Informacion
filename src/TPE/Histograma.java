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

    public void saveAsBMP(String nombreDestino) {
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

        JFreeChart chart = ChartFactory.createBarChart
                ("Histograma", "Tonos de grises", "Frecuencia (%)", dataset,
                        PlotOrientation.VERTICAL, true, false, false);

        chart.setBackgroundPaint(Color.black);
        chart.getTitle().setPaint(Color.white);
        chart.getCategoryPlot().setRangeGridlinePaint(Color.black);

        this.saveBMP(chart.createBufferedImage(1280,720, BufferedImage.TYPE_INT_RGB, null), nombreDestino);

    }

    private void saveBMP(BufferedImage imagen, String nombreDestino) {

        File outputfile = new File(nombreDestino + ".bmp");

        try {
            ImageIO.write(imagen, "bmp", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
