package fr.feavy.ezlogger;

import org.junit.jupiter.api.Test;

public class LoggerTest {
    @Test
    public void test_log() {
        Logger.info("Info log with number %s.", 42);
    }
}
