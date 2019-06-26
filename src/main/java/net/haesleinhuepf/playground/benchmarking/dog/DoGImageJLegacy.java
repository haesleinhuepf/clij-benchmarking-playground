package net.haesleinhuepf.playground.benchmarking.dog;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.Duplicator;
import ij.plugin.ImageCalculator;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Command.class, menuPath = "Plugins>DoG (ImageJ-legacy)")
public class DoGImageJLegacy implements
        Command {
    @Parameter
    private ImagePlus currentData;

    @Parameter
    private double sigma1;

    @Parameter
    private double sigma2;

    @Override
    public void run() {
        StopWatch watch = new StopWatch();
        watch.start();

        // reserve memory
        ImagePlus input = currentData;
        ImagePlus filtered1 = new Duplicator().run(input, 1, input.getNSlices());
        ImagePlus filtered2 = new Duplicator().run(input, 1, input.getNSlices());

        // blur twice
        IJ.run(filtered1, "Gaussian Blur 3D...", "x=" + sigma1 + " y=" + sigma1 + " z=" + sigma1);
        IJ.run(filtered2, "Gaussian Blur 3D...", "x=" + sigma2 + " y=" + sigma2 + " z=" + sigma2);

        // subtract to get the DoG image
        ImagePlus dog = new ImageCalculator().run("Subtract create stack", filtered1, filtered2);

        // show result
        dog.show();

        watch.stop("IJ Legacy");
    }
}
