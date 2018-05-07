package io.imagep.operation;

import com.sun.istack.internal.NotNull;
import javafx.scene.image.*;

import java.nio.IntBuffer;
import java.util.function.Function;


public class ImageOperation {

    public static WritableImage grayScale(@NotNull final Image originalImage) {

        return ImageOperation.internalImageOperation(originalImage, ImageOperation::grayScalePixels);
    }

    public static WritableImage invert(@NotNull final Image originalImage) {

        return internalImageOperation(originalImage, ImageOperation::invertPixels);
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
    private static int[] invertPixels(@NotNull int[] pixels) {

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
    private static int[] grayScalePixels(@NotNull int[] pixels) {

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
}
