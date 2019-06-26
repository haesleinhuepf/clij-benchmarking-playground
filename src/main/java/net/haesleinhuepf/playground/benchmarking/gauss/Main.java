package net.haesleinhuepf.playground.benchmarking.gauss;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.imagej.ImageJ;
import net.imagej.patcher.LegacyInjector;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;

public class Main {


    static {
        LegacyInjector.preinit();
    }

    public static void main(final String... args) throws Exception {
        new ij.ImageJ();

        // Run ImageJ
        final ImageJ ij = new ImageJ();
        ij.ui().showUI();


        for (int turn = 0; turn < 10; turn++) {

            IJ.run("Close All");

            // Create test data
            ImagePlus imp = NewImage.createFloatImage("image", 100, 100, 10, NewImage.FILL_RANDOM);

            Img<FloatType> img = ImageJFunctions.convertFloat(imp);

            Object[]
                    imglibParameters =
                    new Object[]{"currentData",
                            img,
                            "sigma1",
                            2,
                            "sigma2",
                            10};
            Object[]
                    legacyParameters =
                    new Object[]{"currentData",
                            imp,
                            "sigma1",
                            2,
                            "sigma2",
                            10};

            ij.ui().show(img);

            Thread.sleep(2000);

            ij.command().run(GaussianBlurImageJOps.class, true, imglibParameters).get();
            ImagePlus opsResult = IJ.getImage();

            Thread.sleep(2000);

            ij.command().run(GaussianBlurImageJLegacy.class, true, legacyParameters).get();
            ImagePlus legacyResult = IJ.getImage();

            Thread.sleep(2000);

            ij.command().run(GaussianBlurCLIJ.class, true, legacyParameters).get();
            ImagePlus clijResult = IJ.getImage();

            Thread.sleep(5000);

            mse("IJ ops vs legacy", opsResult, legacyResult);
            mse("IJ legacy vs clij", legacyResult, clijResult);
            mse("IJ ops vs clij", opsResult, clijResult);


            System.out.println("turn ---------------------------- " + turn);

            if (turn == 4) {
                CLIJ.getInstance("1070");
            }
            //break;
        }

        //
    }

    private static double mse(String comparisonDescription, ImagePlus imp1, ImagePlus imp2) {
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
