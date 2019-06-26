package net.haesleinhuepf.playground.benchmarking.dog;

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

import static net.haesleinhuepf.playground.benchmarking.Utlities.mse;

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
            ImagePlus imp = NewImage.createFloatImage("image", 100, 100, 1000, NewImage.FILL_RANDOM);

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

            ij.command().run(DoGImageJOps.class, true, imglibParameters).get();
            ImagePlus opsResult = IJ.getImage();

            Thread.sleep(2000);

            ij.command().run(DoGImageJLegacy.class, true, legacyParameters).get();
            ImagePlus legacyResult = IJ.getImage();

            Thread.sleep(2000);

            ij.command().run(DoGCLIJ.class, true, legacyParameters).get();
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

}
