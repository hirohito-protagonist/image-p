package io.imagep.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class ColorTest {

    @Test
    public void testInvertPixels() {

        int[] pixels = new int[]{
            argbToPixel(1, 255, 255, 255),
            argbToPixel(1, 0, 0, 0),
            argbToPixel(1, 125, 130 ,200),
            argbToPixel(1, 244, 69, 139)
        };
        assertArrayEquals(Color.invertPixels(pixels), new int[]{
            argbToPixel(1, 0, 0, 0),
            argbToPixel(1, 255, 255, 255),
            argbToPixel(1, 130, 125, 55),
            argbToPixel(1, 11, 186, 116)
        });
    }

    @Test
    public void testGrayScalePixels() {

        int[] pixels = new int[]{
            argbToPixel(1, 255, 255, 255),
            argbToPixel(1, 0, 0, 0),
            argbToPixel(1, 125, 130 ,200),
            argbToPixel(1, 244, 69, 139)
        };
        assertArrayEquals(Color.grayScalePixels(pixels), new int[]{
            argbToPixel(1, 255, 255, 255),
            argbToPixel(1, 0, 0, 0),
            argbToPixel(1, 136, 136, 136),
            argbToPixel(1, 129, 129, 129)
        });
    }

    @Test
    public void testDarkerPixels() {

        int[] pixels = new int[]{
            argbToPixel(1, 255, 255, 255),
            argbToPixel(1, 0, 0, 0),
            argbToPixel(1, 125, 130 ,200),
            argbToPixel(1, 244, 69, 139)
        };
        assertArrayEquals(Color.darkerPixels(pixels), new int[]{
            argbToPixel(1, 178, 178, 178),
            argbToPixel(1, 0, 0, 0),
            argbToPixel(1, 87, 91, 140),
            argbToPixel(1, 170, 48, 97)
        });
    }

    @Test
    public void testLightenPixels() {

        int[] pixels = new int[]{
            argbToPixel(1, 255, 255, 255),
            argbToPixel(1, 0, 0, 0),
            argbToPixel(1, 125, 130 ,200),
            argbToPixel(1, 244, 69, 139)
        };
        assertArrayEquals(Color.lightenPixels(pixels), new int[]{
            argbToPixel(1, 255, 255, 255),
            argbToPixel(1, 18, 18, 18),
            argbToPixel(1, 159, 165, 255),
            argbToPixel(1, 255, 72, 145)
        });
    }

    @Test
    public void testSaturatePixels() {

        int[] pixels = new int[]{
            argbToPixel(1, 255, 255, 255),
            argbToPixel(1, 0, 0, 0),
            argbToPixel(1, 125, 130 ,200),
            argbToPixel(1, 244, 69, 139)
        };

        assertArrayEquals(Color.saturatePixels(pixels), new int[]{
            argbToPixel(1, 255, 255, 255),
            argbToPixel(1, 0, 0, 0),
            argbToPixel(1, 92, 100, 200),
            argbToPixel(1, 244, 0, 97)
        });
    }

    @Test
    public void testDesaturatePixels() {

        int[] pixels = new int[]{
            argbToPixel(1, 255, 255, 255),
            argbToPixel(1, 0, 0, 0),
            argbToPixel(1, 125, 130 ,200),
            argbToPixel(1, 244, 69, 139)
        };

        assertArrayEquals(Color.desaturatePixels(pixels), new int[]{
            argbToPixel(1, 255, 255, 255),
            argbToPixel(1, 0, 0, 0),
            argbToPixel(1, 147, 151, 200),
            argbToPixel(1, 244, 121, 170)
        });
    }

    @Test
    public void testBinarizePixels() {

        int[] pixels = new int[]{
            argbToPixel(1, 255, 255, 255),
            argbToPixel(1, 0, 0, 0),
            argbToPixel(1, 125, 130 ,200),
            argbToPixel(1, 244, 69, 139)
        };

        assertArrayEquals(Color.binarizePixels(pixels, 127, 255), new int[]{
            argbToPixel(1, 255, 255, 255),
            argbToPixel(1, 0, 0, 0),
            argbToPixel(1, 255, 255, 255),
            argbToPixel(1, 0, 0, 0)
        });
    }

    @Test
    public void testGammaPixels() {

        int[] pixels = new int[]{
            argbToPixel(1, 255, 255, 255),
            argbToPixel(1, 0, 0, 0),
            argbToPixel(1, 125, 130 ,200),
            argbToPixel(1, 244, 69, 139)
        };

        assertArrayEquals(Color.gammaPixels(pixels, 0.5), new int[]{
            argbToPixel(1, 255, 255, 255),
            argbToPixel(1, 0, 0, 0),
            argbToPixel(1, 178, 182, 225),
            argbToPixel(1, 249, 132, 188)
        });
    }


    @Test
    public void testThresholdPixels() {

        int[] pixels = new int[]{
            argbToPixel(1, 255, 255, 255),
            argbToPixel(1, 0, 0, 0),
            argbToPixel(1, 125, 120 ,120),
            argbToPixel(1, 244, 134, 139)
        };

        assertArrayEquals(Color.thresholdPixels(pixels, 134), new int[]{
            argbToPixel(1, 255, 255, 255),
            argbToPixel(1, 0, 0, 0),
            argbToPixel(1, 0, 0, 0),
            argbToPixel(1, 255, 255, 255)
        });
    }

    private int argbToPixel(int alpha, int red, int green, int blue) {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }
}