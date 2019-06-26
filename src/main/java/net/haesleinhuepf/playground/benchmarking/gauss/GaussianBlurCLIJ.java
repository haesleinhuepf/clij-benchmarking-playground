package net.haesleinhuepf.playground.benchmarking.gauss;

import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.imglib2.type.numeric.RealType;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Command.class, menuPath = "Plugins>DoG (CLIJ)")
public class GaussianBlurCLIJ<T extends RealType<T>> implements Command {
    @Parameter
    private ImagePlus currentData;

    @Parameter
    private float sigma1;

    @Parameter
    private float sigma2;

    @Override
    public void run() {
        StopWatch watch = new StopWatch();
        watch.start();

        CLIJ clij = CLIJ.getInstance();

        // convert input and reserve memory
        ClearCLBuffer input = clij.convert(currentData, ClearCLBuffer.class);
        ClearCLBuffer filtered = clij.create(input);

        // blur
        clij.op().blur(input, filtered, sigma1, sigma1, sigma1);

        // show result
        clij.show(filtered, "GaussianBlur");

        // cleanup memory
        input.close();
        filtered.close();
        watch.stop(this.getClass().getSimpleName() + " " + clij.getGPUName());
    }
}
