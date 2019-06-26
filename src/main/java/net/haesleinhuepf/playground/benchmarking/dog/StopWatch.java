package net.haesleinhuepf.playground.benchmarking.dog;

public class StopWatch
{
  private long timeStamp;

  public void start()
  {
    timeStamp = System.currentTimeMillis();
  }

  public void stop(String reportedSectionTitle)
  {
    System.out.println(reportedSectionTitle
                       + " took "
                       + (System.currentTimeMillis() - timeStamp)
                       + " msec");
  }
}
