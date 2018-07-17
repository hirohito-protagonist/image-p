package io.imagep.core;

import javafx.scene.image.*;

import java.util.Arrays;

public class HistogramOperation {

    public static Image average(Image image) {

        int[] inPixels = Utils.pixelsFromImage(image);
        int[] h = Utils.collectRGBSeries(inPixels);
        double havg = Arrays.stream(h).average().orElseGet(() -> 0.0);

        int R = 0;
        int Hint = 0;
        int[] left = new int[256];
        int[] right = new int[256];
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

        return Utils.createImageFromPixels(image, Arrays.stream(inPixels).parallel().map((argb) -> {
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
            return (a << 24) | (r << 16) | (g << 8) | b;
        }).toArray());
    }
}
