package io.imagep.core;

import javafx.scene.image.*;

import java.nio.IntBuffer;
import java.util.Arrays;

public class Utils {

    public static int[] pixelsFromImage(Image image) {
        int width = (int)image.getWidth();
        int height = (int)image.getHeight();

        int[] pixels = new int[width*height];

        PixelReader pixelReader = image.getPixelReader();
        WritablePixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbInstance();
        pixelReader.getPixels(0, 0, width, height, pixelFormat, pixels, 0, width);
        return pixels;
    }

    public static Image createImageFromPixels(Image image, int[] pixels) {
        int width = (int)image.getWidth();
        int height = (int)image.getHeight();
        WritableImage outImage = new WritableImage(width, height);
        PixelWriter pixelWriter = outImage.getPixelWriter();
        WritablePixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbInstance();
        pixelWriter.setPixels(0, 0, width, height, pixelFormat, pixels, 0, width);
        return outImage;
    }

    public static int[] collectRGBSeries(int[] pixels) {

        int[] series = new int[256];
        Arrays.fill(series, 0);
        for (int argb : pixels) {
            int r = (argb >> 16) & 0xff;
            int g = (argb >> 8) & 0xff;
            int b = argb & 0xff;
            if (r == g && r == b) {
                series[r]++;
            } else {
                series[r]++;
                series[g]++;
                series[b]++;
            }
        }
        return series;
    }

    public static int[] collectRedSeries(int[] pixels) {
        int[] series = new int[256];
        Arrays.fill(series, 0);
        for (int argb : pixels) {
            int r = (argb >> 16) & 0xff;
            series[r]++;
        }
        return series;
    }

    public static int[] collectGreenSeries(int[] pixels) {

        int[] series = new int[256];
        Arrays.fill(series, 0);
        for (int argb : pixels) {
            int g = (argb >> 8) & 0xff;
            series[g]++;
        }
        return series;
    }

    public static int[] collectBlueSeries(int[] pixels) {

        int[] series = new int[256];
        Arrays.fill(series, 0);
        for (int argb : pixels) {
            int b = argb & 0xff;
            series[b]++;
        }
        return series;
    }
}
