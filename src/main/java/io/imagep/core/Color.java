package io.imagep.core;

import javafx.scene.image.*;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.IntStream;


public class Color {

    public static Image grayScale(final Image originalImage) {

        return Color.internalImageOperation(originalImage, Color::grayScalePixels);
    }

    public static Image invert(final Image originalImage) {

        return internalImageOperation(originalImage, Color::invertPixels);
    }

    public static Image darker(final Image originalImage) {

        return internalImageOperation(originalImage, Color::darkerPixels);
    }

    public static Image lighten(final Image originalImage) {

        return internalImageOperation(originalImage, Color::lightenPixels);
    }

    public static Image saturate(final Image originalImage) {

        return internalImageOperation(originalImage, Color::saturatePixels);
    }

    public static Image desaturate(final Image originalImage) {

        return internalImageOperation(originalImage, Color::desaturatePixels);
    }

    public static Image binarize(final Image originalImage, int low, int high) {

        return internalImageOperation(originalImage, (pixels) -> Color.binarizePixels(pixels, low, high));
    }

    public static Image posterize(final Image originalImage, final int level) {

        return internalImageOperation(originalImage, (pixels) -> Color.posterizePixels(pixels, level));
    }

    public static Image gammaCorrection(final Image originalImage, final double gamma) {

        return internalImageOperation(originalImage, (pixels) -> Color.gammaPixels(pixels, gamma));
    }

    public static Image threshold(final Image originalImage, final int thresh) {

        return internalImageOperation(originalImage, (pixels) -> Color.thresholdPixels(pixels, thresh));
    }

    private static Image internalImageOperation(final Image originalImage, Function<int[], int[]> pixelsTransformation) {

        int[] buffer = Utils.pixelsFromImage(originalImage);
        buffer = pixelsTransformation.apply(buffer);
        return Utils.createImageFromPixels(originalImage, buffer);
    }


    static int[] invertPixels(int[] pixels) {

       return Arrays.stream(pixels).map((argb) -> {
           int a = (argb >> 24) & 0xff;
           int r = (argb >> 16) & 0xff;
           int g = (argb >> 8) & 0xff;
           int b = argb & 0xff;
           return (a << 24) | ((255 - r) << 16) | ((255 - g) << 8) | (255 - b);
       }).toArray();
    }

    static int[] grayScalePixels(int[] pixels) {

        return Arrays.stream(pixels).map((argb) -> {
            int a = (argb >> 24) & 0xff;
            int r = (argb >> 16) & 0xff;
            int g = (argb >> 8) & 0xff;
            int b = argb & 0xff;
            int color = (int) (0.299 * r + 0.587 * g + 0.114 * b);
            return (a << 24) | (color << 16) | (color << 8) | color;
        }).toArray();
    }

    static int[] binarizePixels(int[] pixels, int low, int high) {

        int[] lut = IntStream.range(0, 256).map((i) -> i < low || i >= high ? 0 : 255).toArray();

        return Arrays.stream(pixels).map((argb) -> {
            int a = (argb >> 24) & 0xff;
            int r = (argb >> 16) & 0xff;
            int g = (argb >> 8) & 0xff;
            int b = argb & 0xff;
            int color = (int) (0.2126 * r + 0.7152 * g + 0.0722 * b);
            return (a << 24) | (lut[color] << 16) | (lut[color] << 8) | lut[color];
        }).toArray();
    }

    static int[] posterizePixels(int[] pixels, final int level) {

        int[] lut = IntStream.range(0, 256).map((i) -> 255 * (level * i / 256) / (level - 1)).toArray();

        return Arrays.stream(pixels).map((argb) -> {
            int a = (argb >> 24) & 0xff;
            int r = (argb >> 16) & 0xff;
            int g = (argb >> 8) & 0xff;
            int b = argb & 0xff;
            return (a << 24) | (lut[r] << 16) | (lut[g] << 8) | lut[b];
        }).toArray();
    }


    static int[] gammaPixels(int[] pixels, final double gamma) {

        int[] lut = IntStream.range(0, 256).map((i) -> (int)(255 * Math.pow(i / 255f, gamma))).toArray();

        return Arrays.stream(pixels).map((argb) -> {
            int a = (argb >> 24) & 0xff;
            int r = (argb >> 16) & 0xff;
            int g = (argb >> 8) & 0xff;
            int b = argb & 0xff;
            return (a << 24) | (lut[r] << 16) | (lut[g] << 8) | lut[b];
        }).toArray();
    }

    /** Creates side effect on pixels collection */
    public static int[] thresholdPixels(int[] pixels, final int thresh) {

        int[] lut = new int[256];
        int t = thresh;
        if (thresh > 255) t = 255;
        if (thresh < 0) t = 0;
        for (int i = 0; i < t; i++) {
            lut[i] = 0;
        }
        for (int i = t; i < lut.length; i++) {
            lut[i] = 255;
        }

        for (int i = 0; i < pixels.length; i++) {
            int argb = pixels[i];
            int a = (argb >> 24) & 0xff;
            int r = (argb >> 16) & 0xff;
            int g = (argb >> 8) & 0xff;
            int b = argb & 0xff;
            int color = (int) (0.2126 * r + 0.7152 * g + 0.0722 * b);
            pixels[i] = (a << 24) | (lut[color] << 16) | (lut[color] << 8) | lut[color];
        }
        return pixels;
    }

    public static int[] darkerPixels(int[] pixels) {

        return Color.colorTransform(pixels, javafx.scene.paint.Color::darker);
    }

    public static int[] lightenPixels(int[] pixels) {

        return Color.colorTransform(pixels, javafx.scene.paint.Color::brighter);
    }

    public static int[] saturatePixels(int[] pixels) {

        return Color.colorTransform(pixels, javafx.scene.paint.Color::saturate);
    }

    public static int[] desaturatePixels(int[] pixels) {

        return Color.colorTransform(pixels, javafx.scene.paint.Color::desaturate);
    }

    private static int[] colorTransform(int[] pixels, Function<javafx.scene.paint.Color, javafx.scene.paint.Color> mapColor) {

        return Arrays.stream(pixels)
                .parallel()
                .map((argb) -> {

                    int a = (argb >> 24) & 0xff;
                    int r = (argb >> 16) & 0xff;
                    int g = (argb >> 8) & 0xff;
                    int b = argb & 0xff;

                    javafx.scene.paint.Color c = mapColor.apply(javafx.scene.paint.Color.rgb(r, g, b, a / 255));
                    return (a << 24) | ((int)(c.getRed() * 255) << 16) | ((int)(c.getGreen() * 255) << 8) | (int)(c.getBlue() * 255);
                })
                .toArray();
    }
}
