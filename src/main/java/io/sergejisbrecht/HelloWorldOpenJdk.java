package io.sergejisbrecht;

import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class HelloWorldOpenJdk {
  public static void main(String[] args) throws Throwable {
    Options opt = new OptionsBuilder()
        .include(MethodHandleBench.class.getSimpleName())
        .forks(1)
        // .jvmArgs("-XX:TieredStopAtLevel=1")
        .warmupIterations(5)
        .measurementIterations(5)
        .addProfiler("gc")
        // .addProfiler("async", "libPath=./async-profiler-2.9-linux-x64/build/libasyncProfiler.so;output=flamegraph")
        .mode(Mode.AverageTime)
        .build();
    new Runner(opt).run();
  }
}
