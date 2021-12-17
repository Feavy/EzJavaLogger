# EzLogger Java

## Simple.

```java
public class LoggerTest {
    @Test
    public void test_log() {
        Logger.info("Info log with number %s.", 42);
    }
}
```

Prints :

````java
[INFO] 2021-09-25 18:39:49 LoggerTest.test_log(LoggerTest.java:9) : Info log with number 42.
````
