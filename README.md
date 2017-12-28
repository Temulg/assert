# assert - Attempt at multi-threaded unit test assertions

This small library provides a simplistic adaptation of TestNG's `Assert` functionality for
cases where asynchronous execution may be expected as part of unit test.

A basic example will look the following:

```java
import java.util.concurrent.ForkJoinPool;

import org.testng.annotations.Test;

import udentric.test.Assert;
import udentric.test.Tester;

public class ExampleTest {
  @Test
  public void example() {
    Tester.beginAsync(); // setup the async state
                         // should be called by test runner

    Assert.assertEquals(3, 3);

    ForkJoinPool.commonPool().execute(() -> {
      Assert.assertEquals(5, 5);
      Assert.done(); // notify the test runner that one task was completed successfully
    });

    Tester.endAsync(1); // wait for 1 async task to complete
                        // should also be called by test runner
  }
}
```
