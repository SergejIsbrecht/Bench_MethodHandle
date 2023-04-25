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
    blackhole.consume(calls.plainLong());
  }

  @Benchmark
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  @BenchmarkMode(Mode.AverageTime)
  public void methodHandleStaticFinal(Blackhole blackhole) throws Throwable {
    blackhole.consume(Calls.mh_plainLong_staticFinal.invoke(calls));
  }

  @Benchmark
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  @BenchmarkMode(Mode.AverageTime)
  public void mh_staticPlainLong_staticFinal(Blackhole blackhole) throws Throwable {
    blackhole.consume(Calls.mh_staticPlainLong_staticFinal.invoke());
  }

  @Benchmark
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  @BenchmarkMode(Mode.AverageTime)
  public void mh_plainObject_staticFinal(Blackhole blackhole) throws Throwable {
    blackhole.consume(Calls.mh_plainObject_staticFinal.invoke(calls));
  }
}

final class Calls {
  static final MethodHandle mh_plainLong_staticFinal;
  static final MethodHandle mh_plainObject_staticFinal;
  static final MethodHandle mh_staticPlainLong_staticFinal;

  static {
    try {
      mh_plainLong_staticFinal = MethodHandles.lookup().findVirtual(Calls.class, "plainLong", MethodType.methodType(long.class));
      mh_plainObject_staticFinal = MethodHandles.lookup().findVirtual(Calls.class, "plainObject", MethodType.methodType(Object.class));
      mh_staticPlainLong_staticFinal = MethodHandles.lookup().findStatic(Calls.class, "staticPlainLong", MethodType.methodType(long.class));
    } catch (NoSuchMethodException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  private final Object object1 = new Object();

  public static long staticPlainLong() {
    return 42L;
  }

  public long plainLong() {
    return 42L;
  }

  public Object plainObject() {
    return object1;
  }
}
