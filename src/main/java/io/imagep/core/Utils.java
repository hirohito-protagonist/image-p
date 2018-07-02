package io.imagep.core;

import javafx.scene.image.*;

import java.nio.IntBuffer;

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
}
