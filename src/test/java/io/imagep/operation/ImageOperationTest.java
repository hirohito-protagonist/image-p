package io.imagep.operation;

import java.util.Arrays;

import static org.junit.Assert.*;

public class ImageOperationTest {

    @org.junit.Test
    public void invertPixels() {

        int[] pixels = new int[]{
            argbToPixel(1, 255, 255, 255),
            argbToPixel(1, 0, 0, 0),
            argbToPixel(1, 125, 130 ,200),
            argbToPixel(1, 244, 69, 139)
        };
        assertArrayEquals(ImageOperation.invertPixels(pixels), new int[]{
            argbToPixel(1, 0, 0, 0),
            argbToPixel(1, 255, 255, 255),
            argbToPixel(1, 130, 125, 55),
            argbToPixel(1, 11, 186, 116)
        });
    }

    @org.junit.Test
    public void grayScalePixels() {

        int[] pixels = new int[]{
            argbToPixel(1, 255, 255, 255),
            argbToPixel(1, 0, 0, 0),
            argbToPixel(1, 125, 130 ,200),
            argbToPixel(1, 244, 69, 139)
        };
        assertArrayEquals(ImageOperation.grayScalePixels(pixels), new int[]{
            argbToPixel(1, 255, 255, 255),
            argbToPixel(1, 0, 0, 0),
            argbToPixel(1, 136, 136, 136),
            argbToPixel(1, 129, 129, 129)
        });
    }

    @org.junit.Test
    public void darkerPixels() {

        int[] pixels = new int[]{
            argbToPixel(1, 255, 255, 255),
            argbToPixel(1, 0, 0, 0),
            argbToPixel(1, 125, 130 ,200),
            argbToPixel(1, 244, 69, 139)
        };
        assertArrayEquals(ImageOperation.darkerPixels(pixels), new int[]{
            argbToPixel(1, 178, 178, 178),
            argbToPixel(1, 0, 0, 0),
            argbToPixel(1, 87, 91, 140),
            argbToPixel(1, 170, 48, 97)
        });
    }

    @org.junit.Test
    public void lightenPixels() {

        int[] pixels = new int[]{
            argbToPixel(1, 255, 255, 255),
            argbToPixel(1, 0, 0, 0),
            argbToPixel(1, 125, 130 ,200),
            argbToPixel(1, 244, 69, 139)
        };
        assertArrayEquals(ImageOperation.lightenPixels(pixels), new int[]{
            argbToPixel(1, 255, 255, 255),
            argbToPixel(1, 18, 18, 18),
            argbToPixel(1, 159, 165, 255),
            argbToPixel(1, 255, 72, 145)
        });
    }

    private int argbToPixel(int alpha, int red, int green, int blue) {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }
}