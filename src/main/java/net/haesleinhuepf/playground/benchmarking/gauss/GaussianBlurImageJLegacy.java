package net.haesleinhuepf.playground.benchmarking.gauss;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.Duplicator;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Command.class, menuPath = "Plugins>DoG (ImageJ-legacy)")
public class GaussianBlurImageJLegacy implements
        Command {
    @Parameter
    private ImagePlus currentData;

    @Parameter
    private double sigma;

    @Override
    public void run() {
        StopWatch watch = new StopWatch();
        watch.start();

        // reserve memory
        ImagePlus input = currentData;
        ImagePlus filtered = new Duplicator().run(input, 1, input.getNSlices());

        // blur
        IJ.run(filtered, "Gaussian Blur 3D...", "x=" + sigma + " y=" + sigma + " z=" + sigma);

        // show result
        filtered.show();

        watch.stop("IJ Legacy");
    }
}
