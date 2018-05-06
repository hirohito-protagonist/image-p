package io.imagep.operation;

import com.sun.istack.internal.NotNull;
import javafx.scene.image.*;

import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.stream.Stream;


public class ImageOperation {

    public static WritableImage grayScale(@NotNull final Image originalImage) {

        int width = (int)originalImage.getWidth();
        int height = (int)originalImage.getHeight();

        WritableImage image = new WritableImage(width, height);

        PixelWriter pixelWriter = image.getPixelWriter();
        PixelReader pixelReader = originalImage.getPixelReader();

        int[] buffer = new int[width * height];
        WritablePixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbInstance();
        pixelReader.getPixels(0, 0, width, height, pixelFormat, buffer, 0, width);
        ImageOperation.grayScalePixels(buffer);
        pixelWriter.setPixels(0, 0, width, height, pixelFormat, buffer, 0, width);

        return image;
    }

    public static WritableImage invert(@NotNull final Image originalImage) {

        int width = (int)originalImage.getWidth();
        int height = (int)originalImage.getHeight();

        WritableImage image = new WritableImage(width, height);

        PixelWriter pixelWriter = image.getPixelWriter();
        PixelReader pixelReader = originalImage.getPixelReader();

        int[] buffer = new int[width * height];
        WritablePixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbInstance();
        pixelReader.getPixels(0, 0, width, height, pixelFormat, buffer, 0, width);
        ImageOperation.invertPixels(buffer);
        pixelWriter.setPixels(0, 0, width, height, pixelFormat, buffer, 0, width);

        return image;
    }

    private static void invertPixels(@NotNull int[] pixels) {

        for (int i = 0; i < pixels.length; i++) {
            int argb = pixels[i];
            int a = (argb >> 24) & 0xff;
            int r = (argb >> 16) & 0xff;
            int g = (argb >> 8) & 0xff;
            int b = argb & 0xff;
            pixels[i] = (a << 24) | ((255 - r) << 16) | ((255 - g) << 8) | (255 - b);
        }
    }

    private static void grayScalePixels(@NotNull int[] pixels) {

        for (int i = 0; i < pixels.length; i++) {
            int argb = pixels[i];
            int a = (argb >> 24) & 0xff;
            int r = (argb >> 16) & 0xff;
            int g = (argb >> 8) & 0xff;
            int b = argb & 0xff;
            int color = (int) (0.299 * r + 0.587 * g + 0.114 * b);
            pixels[i] = (a << 24) | (color << 16) | (color << 8) | color;
        }
    }
}
