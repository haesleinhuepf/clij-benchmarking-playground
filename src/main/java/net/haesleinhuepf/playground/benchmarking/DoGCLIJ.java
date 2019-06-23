package net.haesleinhuepf.playground.benchmarking;

import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.imglib2.type.numeric.RealType;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.ui.UIService;

@Plugin(type = Command.class, menuPath = "Plugins>DoG (CLIJ)")
public class DoGCLIJ<T extends RealType<T>> implements Command {
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
        ClearCLBuffer filtered1 = clij.create(input);
        ClearCLBuffer filtered2 = clij.create(input);
        ClearCLBuffer dog = clij.create(input);

        // blur twice
        clij.op().blur(input, filtered1, sigma1, sigma1, sigma1);
        clij.op().blur(input, filtered2, sigma2, sigma2, sigma2);

        // subtract to get the DoG image
        clij.op().subtract(filtered1, filtered2, dog);

        // show result
        clij.show(dog, "DoG");

        // cleanup memory
        input.close();
        filtered1.close();
        filtered2.close();
        dog.close();
        watch.stop(this.getClass().getSimpleName() + " " + clij.getGPUName());
    }
}
