package io.imagep.core;

import javafx.scene.image.*;

public class HistogramOperation {

    public static Image average(Image image) {

        int[] inPixels = Utils.pixelsFromImage(image);


        int[] h = HistogramOperation.collectRGBSeries(inPixels);
        int havg = 0;
        for (int i = 0; i < 256; i++) {
            havg += h[i];
        }
        havg /= 256;

        int R = 0;
        int Hint = 0;
        int[] left = new int[256];
        int[] right = new int[256];
        int[] new_ = new int[256];
        for (int i = 0; i < 256; i++) {
            left[i] = R;
            Hint += h[i];
            while (Hint > havg) {
                Hint -= havg;
                R++;
            }
            if (R > 255) R = 255;
            right[i] = R;
        }


        for (int i = 0; i < inPixels.length; i++) {
            int argb = inPixels[i];
            int a = (argb >> 24) & 0xff;
            int r = (argb >> 16) & 0xff;
            int g = (argb >> 8) & 0xff;
            int b = argb & 0xff;
            if (left[r] == right[r]) {
                r = left[r];
            }
            if (left[g] == right[g]) {
                g = left[g];
            }
            if (left[b] == right[b]) {
                b = left[b];
            }
            inPixels[i] = (a << 24) | (r << 16) | (g << 8) | b;
        }

        return Utils.createImageFromPixels(image, inPixels);
    }

    private static int[] collectRGBSeries(int[] pixels) {

        int[] series = new int[256];
        for (int i = 0; i < 256; i++) {
            series[i] = 0;
        }
        for (int i = 0; i < pixels.length; i++) {
            int argb = pixels[i];
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
}
