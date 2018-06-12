package io.imagep.core;

import static org.junit.Assert.*;

public class ColorTest {

    @org.junit.Test
    public void invertPixels() {

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

    @org.junit.Test
    public void grayScalePixels() {

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

    @org.junit.Test
    public void darkerPixels() {

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

    @org.junit.Test
    public void lightenPixels() {

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

    @org.junit.Test
    public void saturatePixels() {

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

    @org.junit.Test
    public void desaturatePixels() {

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

    @org.junit.Test
    public void binarizePixels() {

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

    @org.junit.Test
    public void gammaPixels() {

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


    @org.junit.Test
    public void thresholdPixels() {

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