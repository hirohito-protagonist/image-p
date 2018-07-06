package io.imagep.core.filter;

import javafx.scene.image.*;

import java.nio.IntBuffer;
import java.util.Arrays;

public class Pixelize {

    public static Image apply(Image image, int kernelSize) {

        int width = (int)image.getWidth();
        int height = (int)image.getHeight();

        WritableImage out = new WritableImage(width, height);

        int kernelWidth = kernelSize * 2 + 1;
        int kernelHeight = kernelWidth;

        PixelReader pixelReader = image.getPixelReader();
        PixelWriter pixelWriter = out.getPixelWriter();
        WritablePixelFormat<IntBuffer> format = WritablePixelFormat.getIntArgbInstance();
        for (int x = kernelSize; x < width - kernelSize * 2; x += kernelWidth) {
            for (int y = kernelSize; y < height - kernelSize * 2; y += kernelHeight) {

                int[] buffer = new int[kernelWidth * kernelHeight];
                pixelReader.getPixels(x, y, kernelWidth, kernelHeight, format, buffer, 0, kernelWidth);

                int alpha = 0;
                int red = 0;
                int green = 0;
                int blue = 0;

                for (int color : buffer) {
                    alpha += (color >> 24);
                    red += (color >> 16) & 0xFF;
                    green += (color >> 8) & 0xFF;
                    blue += (color & 0xFF);
                }
                alpha = alpha / kernelWidth / kernelHeight;
                red = red / kernelWidth / kernelHeight;
                green = green / kernelWidth / kernelHeight;
                blue = blue / kernelWidth / kernelHeight;

                int color = (alpha << 24) | (red << 16) | (green << 8) | blue;
                Arrays.fill(buffer, color);
                pixelWriter.setPixels(x, y, kernelWidth, kernelHeight, format, buffer, 0, kernelWidth);
            }
        }
        return out;
    }
}
