package io.imagep.operation;

import javafx.scene.image.*;

import java.nio.IntBuffer;

public class ImageOperation {

    public static WritableImage grayScale(Image originalImage) {

        int width = (int)originalImage.getWidth();
        int height = (int)originalImage.getHeight();

        WritableImage image = new WritableImage(width, height);

        PixelWriter pixelWriter = image.getPixelWriter();
        PixelReader pixelReader = originalImage.getPixelReader();

        int[] buffer = new int[width * height];
        WritablePixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbInstance();
        pixelReader.getPixels(0, 0, width, height, pixelFormat, buffer, 0, width);
        for (int i = 0; i < buffer.length; i++) {
            int argb = buffer[i];
            int a = (argb >> 24) & 0xff;
            int r = (argb >> 16) & 0xff;
            int g = (argb >> 8) & 0xff;
            int b = argb & 0xff;
            int color = (int) (0.299 * r + 0.587 * g + 0.114 * b);
            buffer[i] = (a << 24) | (color << 16) | (color << 8) | color;
        }
        pixelWriter.setPixels(0, 0, width, height, pixelFormat, buffer, 0, width);

        return image;
    }
}
