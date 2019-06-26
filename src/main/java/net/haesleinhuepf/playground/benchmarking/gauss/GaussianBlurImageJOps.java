/*
 * To the extent possible under law, the ImageJ developers have waived
 * all copyright and related or neighboring rights to this tutorial code.
 *
 * See the CC0 1.0 Universal license for details:
 *     http://creativecommons.org/publicdomain/zero/1.0/
 */

package net.haesleinhuepf.playground.benchmarking.gauss;

import net.imagej.ops.OpService;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.ui.UIService;


@Plugin(type = Command.class, menuPath = "Plugins>DoG (Ops)")
public class GaussianBlurImageJOps<T extends RealType<T>> implements
        Command {

    @Parameter
    private Img currentData;

    @Parameter
    private UIService uiService;

    @Parameter
    private OpService opService;

    @Parameter
    private double sigma1;

    @Parameter
    private double sigma2;

    @Override
    public void run() {
        StopWatch watch = new StopWatch();
        watch.start();

        // reserve memory
        final Img<T> image = (Img<T>) currentData;
        long[] dimensions = new long[image.numDimensions()];
        image.dimensions(dimensions);
        Img<T> dog = image.factory().create(dimensions, image.cursor().next());

        // blur twice
        RandomAccessibleInterval<T> filtered = opService.filter().gauss(image, sigma1, sigma1, sigma1);

        // show result
        uiService.show(filtered);

        watch.stop("IJ Ops");
    }
}
