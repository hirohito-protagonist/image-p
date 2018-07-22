package io.imagep.core;

import javafx.scene.image.*;

import java.util.Arrays;
import java.util.Random;

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

    public static Image random(Image image) {

        int[] pixels = Utils.pixelsFromImage(image);
        int[] h = Utils.collectRGBSeries(pixels);

        int havg = Arrays.stream(h).sum() / 256;

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
            new_[i] = right[i] - left[i];
        }
        Random random = new Random();

        return Utils.createImageFromPixels(image, Arrays.stream(pixels).parallel().map((argb) -> {
            int a = (argb >> 24) & 0xff;
            int r = (argb >> 16) & 0xff;
            int g = (argb >> 8) & 0xff;
            int b = argb & 0xff;
            if (left[r] == right[r]) {
                r = left[r];
            } else {
                int value = random.nextInt(new_[r]);
                r = left[r] + value;
            }
            if (left[g] == right[g]) {
                g = left[g];
            } else {
                int value = random.nextInt(new_[g]);
                g = left[g] + value;
            }
            if (left[b] == right[b]) {
                b = left[b];
            } else {
                int value = random.nextInt(new_[b]);
                b = left[b] + value;
            }
            return (a << 24) | (r << 16) | (g << 8) | b;
        }).toArray());
    }

    public static Image stretch(Image image) {

        int[] pixels = Utils.pixelsFromImage(image);
        int maxRed = 0;
        int maxGreen = 0;
        int maxBlue = 0;
        int minRed = 255;
        int minGreen = 255;
        int minBlue = 255;

        for (int pixel: pixels) {
            int r = (pixel >> 16) & 0xff;
            int g = (pixel >> 8) & 0xff;
            int b = pixel & 0xff;
            if (r > maxRed) maxRed = r;
            if (g > maxGreen) maxGreen = g;
            if (b > maxBlue) maxBlue = b;
            if (r < minRed) minRed = r;
            if (g < minGreen) minGreen = g;
            if (b < minBlue) minBlue = b;
        }

        int[] lutRed = new int[256];
        int[] lutGreen = new int[256];
        int[] lutBlue = new int[256];

        for (int i = 0; i < 256; i++) {
            lutRed[i] = (255 / (maxRed - minRed) * (i - minRed));
            lutGreen[i] = (255 / (maxGreen - minGreen) * (i - minGreen));
            lutBlue[i] = (255 / (maxBlue - minBlue) * (i - minBlue));
        }

        return Utils.createImageFromPixels(image, Arrays.stream(pixels).parallel().map((argb) -> {
            int a = (argb >> 24) & 0xff;
            int r = (argb >> 16) & 0xff;
            int g = (argb >> 8) & 0xff;
            int b = argb & 0xff;

            return (a << 24) | (lutRed[r] << 16) | (lutGreen[g] << 8) | lutBlue[b];
        }).toArray());
    }
}
