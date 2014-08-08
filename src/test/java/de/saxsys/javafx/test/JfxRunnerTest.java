package de.saxsys.javafx.test;

import java.util.concurrent.CompletableFuture;

import javafx.application.Platform;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

//Tests dont run in travis CI because its headless
@Ignore
@RunWith(JfxRunner.class)
public class JfxRunnerTest {

    @Test
    public void testWithoutFXThread() throws Exception {
        Assert.assertFalse(Platform.isFxApplicationThread());
        CompletableFuture<Boolean> isInApplicationThread = new CompletableFuture<>();
        Platform.runLater(() -> {
            isInApplicationThread.complete(Platform.isFxApplicationThread());
        });
        Assert.assertTrue(isInApplicationThread.get());
    }

    @Test
    @TestInJfxThread
    public void testWithFXThread() throws Exception {
        Assert.assertTrue(Platform.isFxApplicationThread());
    }

}
