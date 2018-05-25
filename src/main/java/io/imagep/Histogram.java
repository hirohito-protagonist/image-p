package io.imagep;

import com.sun.istack.internal.NotNull;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;

import java.nio.IntBuffer;

public class Histogram {

    public static XYChart.Series rgb(@NotNull final Image image) {

        int width = (int)image.getWidth();
        int height = (int)image.getHeight();
        int[] buffer = new int[width * height];
        PixelReader pixelReader = image.getPixelReader();
        WritablePixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbInstance();
        pixelReader.getPixels(0, 0, width, height, pixelFormat, buffer, 0, width);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        int[] rgbSeries = new int[256];
        for (int i = 0; i < 256; i++) {
            rgbSeries[i] = 0;
        }
        for (int i = 0; i < buffer.length; i++) {
            int argb = buffer[i];
            int a = (argb >> 24) & 0xff;
            int r = (argb >> 16) & 0xff;
            int g = (argb >> 8) & 0xff;
            int b = argb & 0xff;
            if (r == g && r == b) {
                rgbSeries[r]++;
            } else {
                rgbSeries[r]++;
                rgbSeries[g]++;
                rgbSeries[b]++;
            }
        }
        for (int i = 0; i < 256; i++) {
            series.getData().add(new XYChart.Data<String, Number>(Integer.toString(i), rgbSeries[i]));
        }
        return series;
    }
}
