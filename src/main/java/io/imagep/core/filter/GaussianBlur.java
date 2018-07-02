package io.imagep.core.filter;

import io.imagep.core.Utils;
import javafx.scene.image.*;

import java.awt.image.Kernel;

public class GaussianBlur {

    public static Image apply(Image image, float radius) {
        int width = (int)image.getWidth();
        int height = (int)image.getHeight();

        int[] inPixels = Utils.pixelsFromImage(image);
        int[] outPixels = new int[width*height];

        Kernel kernel = GaussianBlur.kernel(radius);

        GaussianBlur.blur(kernel, inPixels, outPixels, width, height);
        GaussianBlur.blur(kernel, outPixels, inPixels, height, width);

        return Utils.createImageFromPixels(image, inPixels);
    }

    private static void blur(Kernel kernel, int[] in, int[] out, int width, int height) {
        float[] matrix = kernel.getKernelData( null );
        int cols = kernel.getWidth();
        int cols2 = cols / 2;

        for (int y = 0; y < height; y++) {
            int index = y;
            int ioffset = y * width;
            for (int x = 0; x < width; x++) {
                float r = 0, g = 0, b = 0, a = 0;
                int moffset = cols2;
                for (int col = -cols2; col <= cols2; col++) {
                    float f = matrix[moffset + col];

                    if (f != 0) {
                        int ix = x + col;
                        if ( ix < 0 ) {
                            ix = 0;
                        } else if ( ix >= width) {
                            ix = width - 1;
                        }
                        int rgb = in[ioffset + ix];
                        a += f * ((rgb >> 24) & 0xff);
                        r += f * ((rgb >> 16) & 0xff);
                        g += f * ((rgb >> 8) & 0xff);
                        b += f * (rgb & 0xff);
                    }
                }
                int ia = GaussianBlur.clamp((int)(a+0.5));
                int ir = GaussianBlur.clamp((int)(r+0.5));
                int ig = GaussianBlur.clamp((int)(g+0.5));
                int ib = GaussianBlur.clamp((int)(b+0.5));
                out[index] = (ia << 24) | (ir << 16) | (ig << 8) | ib;
                index += height;
            }
        }
    }

    private static Kernel kernel(float radius) {
        int r = (int)Math.ceil(radius);
        int rows = r * 2 + 1;
        float[] matrix = new float[rows];
        float sigma = radius / 3;
        float sigma22 = 2 * sigma * sigma;
        float sigmaPI2 = (float)(2 * Math.PI * sigma);
        float sqrtSigmaPI2 = (float)Math.sqrt(sigmaPI2);
        float radius2 = radius * radius;
        float total = 0;
        int index = 0;
        for (int row = -r; row <= r; row++) {
            float distance = row * row;
            if (distance > radius) {
                matrix[index] = 0;
            } else {
                matrix[index] = (float) Math.exp(-(distance) / sigma22) / sqrtSigmaPI2;
            }
            total += matrix[index];
            index++;
        }
        for (int i = 0; i < rows; i++) {
            matrix[i] /= total;
        }
        return new Kernel(rows, 1, matrix);
    }

    private static int clamp(int c) {
        if (c < 0) {
            return 0;
        }
        if (c > 255) {
            return 255;
        }
        return c;
    }
}
