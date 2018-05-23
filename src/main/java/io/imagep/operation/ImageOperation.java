package io.imagep.operation;

import com.sun.istack.internal.NotNull;
import javafx.scene.image.*;
import javafx.scene.paint.Color;

import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.function.Function;


public class ImageOperation {

    public static WritableImage grayScale(@NotNull final Image originalImage) {

        return ImageOperation.internalImageOperation(originalImage, ImageOperation::grayScalePixels);
    }

    public static WritableImage invert(@NotNull final Image originalImage) {

        return internalImageOperation(originalImage, ImageOperation::invertPixels);
    }

    public static WritableImage darker(@NotNull final Image originalImage) {

        return internalImageOperation(originalImage, ImageOperation::darkerPixels);
    }

    public static WritableImage lighten(@NotNull final Image originalImage) {

        return internalImageOperation(originalImage, ImageOperation::lightenPixels);
    }

    public static WritableImage saturate(@NotNull final Image originalImage) {

        return internalImageOperation(originalImage, ImageOperation::saturatePixels);
    }

    public static WritableImage desaturate(@NotNull final Image originalImage) {

        return internalImageOperation(originalImage, ImageOperation::desaturatePixels);
    }

    public static WritableImage binarize(@NotNull final Image originalImage, int low, int high) {

        return internalImageOperation(originalImage, (pixels) -> ImageOperation.binarizePixels(pixels, low, high));
    }

    public static WritableImage posterize(@NotNull final Image originalImage, final int level) {

        return internalImageOperation(originalImage, (pixels) -> ImageOperation.posterizePixels(pixels, level));
    }

    public static WritableImage gammaCorrection(@NotNull final Image originalImage, final double gamma) {

        return internalImageOperation(originalImage, (pixels) -> ImageOperation.gammaPixels(pixels, gamma));
    }

    public static WritableImage threshold(@NotNull final Image originalImage, final int thresh) {

        return internalImageOperation(originalImage, (pixels) -> ImageOperation.thresholdPixels(pixels, thresh));
    }

    private static WritableImage internalImageOperation(@NotNull final Image originalImage, Function<int[], int[]> pixelsTransformation) {

        int width = (int)originalImage.getWidth();
        int height = (int)originalImage.getHeight();

        WritableImage image = new WritableImage(width, height);

        PixelWriter pixelWriter = image.getPixelWriter();
        PixelReader pixelReader = originalImage.getPixelReader();

        int[] buffer = new int[width * height];
        WritablePixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbInstance();
        pixelReader.getPixels(0, 0, width, height, pixelFormat, buffer, 0, width);
        buffer = pixelsTransformation.apply(buffer);
        pixelWriter.setPixels(0, 0, width, height, pixelFormat, buffer, 0, width);

        return image;
    }

    /** Creates side effect on pixels collection */
    public static int[] invertPixels(@NotNull int[] pixels) {

        for (int i = 0; i < pixels.length; i++) {
            int argb = pixels[i];
            int a = (argb >> 24) & 0xff;
            int r = (argb >> 16) & 0xff;
            int g = (argb >> 8) & 0xff;
            int b = argb & 0xff;
            pixels[i] = (a << 24) | ((255 - r) << 16) | ((255 - g) << 8) | (255 - b);
        }
        return pixels;
    }

    /** Creates side effect on pixels collection */
    public static int[] grayScalePixels(@NotNull int[] pixels) {

        for (int i = 0; i < pixels.length; i++) {
            int argb = pixels[i];
            int a = (argb >> 24) & 0xff;
            int r = (argb >> 16) & 0xff;
            int g = (argb >> 8) & 0xff;
            int b = argb & 0xff;
            int color = (int) (0.299 * r + 0.587 * g + 0.114 * b);
            pixels[i] = (a << 24) | (color << 16) | (color << 8) | color;
        }
        return pixels;
    }

    /** Creates side effect on pixels collection */
    public static int[] binarizePixels(@NotNull int[] pixels, int low, int high) {

        int[] lut = new int[256];
        for (int i = 0; i < lut.length; i++) {
            lut[i] = i < low || i >= high ? 0 : 255;
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

    /** Creates side effect on pixels collection */
    public static int[] posterizePixels(@NotNull int[] pixels, final int level) {

        int[] lut = new int[256];
        for (int i = 0; i < lut.length; i++) {
            lut[i] = 255 * (level * i / 256) / (level - 1);
        }

        for (int i = 0; i < pixels.length; i++) {
            int argb = pixels[i];
            int a = (argb >> 24) & 0xff;
            int r = (argb >> 16) & 0xff;
            int g = (argb >> 8) & 0xff;
            int b = argb & 0xff;
            pixels[i] = (a << 24) | (lut[r] << 16) | (lut[g] << 8) | lut[b];
        }
        return pixels;
    }

    /** Creates side effect on pixels collection */
    public static int[] gammaPixels(@NotNull int[] pixels, final double gamma) {

        int[] lut = new int[256];
        for (int i = 0; i < lut.length; i++) {
            lut[i] = (int)(255 * Math.pow(i / 255f, gamma));
        }

        for (int i = 0; i < pixels.length; i++) {
            int argb = pixels[i];
            int a = (argb >> 24) & 0xff;
            int r = (argb >> 16) & 0xff;
            int g = (argb >> 8) & 0xff;
            int b = argb & 0xff;
            pixels[i] = (a << 24) | (lut[r] << 16) | (lut[g] << 8) | lut[b];
        }
        return pixels;
    }

    /** Creates side effect on pixels collection */
    public static int[] thresholdPixels(@NotNull int[] pixels, final int thresh) {

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

    public static int[] darkerPixels(@NotNull int[] pixels) {

        return ImageOperation.colorTransform(pixels, Color::darker);
    }

    public static int[] lightenPixels(@NotNull int[] pixels) {

        return ImageOperation.colorTransform(pixels, Color::brighter);
    }

    public static int[] saturatePixels(@NotNull int[] pixels) {

        return ImageOperation.colorTransform(pixels, Color::saturate);
    }

    public static int[] desaturatePixels(@NotNull int[] pixels) {

        return ImageOperation.colorTransform(pixels, Color::desaturate);
    }

    private static int[] colorTransform(@NotNull int[] pixels, Function<Color, Color> mapColor) {

        return Arrays.stream(pixels)
                .parallel()
                .map((argb) -> {

                    int a = (argb >> 24) & 0xff;
                    int r = (argb >> 16) & 0xff;
                    int g = (argb >> 8) & 0xff;
                    int b = argb & 0xff;

                    Color c = mapColor.apply(Color.rgb(r, g, b, a / 255));
                    return (a << 24) | ((int)(c.getRed() * 255) << 16) | ((int)(c.getGreen() * 255) << 8) | (int)(c.getBlue() * 255);
                })
                .toArray();
    }
}
