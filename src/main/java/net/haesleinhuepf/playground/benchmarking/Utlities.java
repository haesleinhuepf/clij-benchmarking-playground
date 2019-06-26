package net.haesleinhuepf.playground.benchmarking;

import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;

/**
 * Utlities
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 06 2019
 */
public class Utlities {

    public static double mse(String comparisonDescription, ImagePlus imp1, ImagePlus imp2) {
        CLIJ clij = CLIJ.getInstance();

        ClearCLBuffer buffer1 = clij.push(imp1);
        ClearCLBuffer buffer2 = clij.push(imp2);
        ClearCLBuffer difference = clij.create(buffer1);
        ClearCLBuffer squared = clij.create(buffer1);

        clij.op().subtractImages(buffer1, buffer2, difference);

        clij.op().power(difference, squared, 2f);

        double mse = clij.op().sumPixels(squared) / squared.getWidth() / squared.getHeight() / squared.getDepth();

        buffer1.close();
        buffer2.close();
        difference.close();
        squared.close();

        System.out.println("MSE (" + comparisonDescription + ") = " + mse);

        return mse;
    }
}
