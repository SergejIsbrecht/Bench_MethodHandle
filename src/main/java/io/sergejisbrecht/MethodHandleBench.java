package io.sergejisbrecht;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
public class MethodHandleBench {
  private final Calls calls = new Calls();

  @Benchmark
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  @BenchmarkMode(Mode.AverageTime)
  public void baseline(Blackhole blackhole) throws Exception {
    blackhole.consume(calls.plain());
  }

  @Benchmark
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  @BenchmarkMode(Mode.AverageTime)
  public void methodHandleStaticFinal(Blackhole blackhole) throws Throwable {
    blackhole.consume(Calls.methodHandleStaticFinal.invoke(calls));
  }

  @Benchmark
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  @BenchmarkMode(Mode.AverageTime)
  public void methodHandleStatic(Blackhole blackhole) throws Throwable {
    blackhole.consume(Calls.methodHandleFinal.invoke(calls));
  }
}

final class Calls {
  static final MethodHandle methodHandleStaticFinal;

  static MethodHandle methodHandleFinal = null;

  static {
    try {
      methodHandleStaticFinal = MethodHandles.lookup().findVirtual(Calls.class, "plain", MethodType.methodType(long.class));
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public Calls() {
    try {
      methodHandleFinal = MethodHandles.lookup().findVirtual(Calls.class, "plain", MethodType.methodType(long.class));
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public long plain() {
    return 42L;
  }
}
