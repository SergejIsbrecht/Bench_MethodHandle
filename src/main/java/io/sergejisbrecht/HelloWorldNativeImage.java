package io.sergejisbrecht;

import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class HelloWorldNativeImage {
  public static void main(String[] args) throws Throwable {
    Options opt = new OptionsBuilder()
        .include(MethodHandleBench.class.getSimpleName())
        .forks(0)
        .warmupIterations(5)
        .measurementIterations(5)
        .addProfiler("gc")
        .mode(Mode.AverageTime)
        .build();
    new Runner(opt).run();
  }
}
