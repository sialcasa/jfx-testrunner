package de.saxsys.javafx.test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import javafx.application.Application;
import javafx.stage.Stage;

import org.junit.runners.model.InitializationError;

/**
 * This is the application which starts JavaFx. It is controlled through the
 * startJavaFx() method.
 */
public class SingleJfxApplication extends Application {

    /** The lock that guarantees that only one JavaFX thread will be started. */
    private static final ReentrantLock LOCK = new ReentrantLock();

    /** Started flag. */
    private static AtomicBoolean started = new AtomicBoolean();

    /**
     * Start JavaFx.
     * 
     * @throws InitializationError
     * @throws InterruptedException
     */
    public static void startJavaFx() throws InitializationError {
        try {
            // Lock or wait. This gives another call to this method time to
            // finish
            // and release the lock before another one has a go
            LOCK.lock();

            if (!started.get()) {
                // start the JavaFX application
                final ExecutorService executor = Executors.newSingleThreadExecutor();
                Future<?> jfxLaunchFuture = executor.submit(() -> SingleJfxApplication.launch());

                while (!started.get()) {
                    try {
                        jfxLaunchFuture.get(1, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException | TimeoutException e) {
                        // continue waiting until success or error
                    }
                    Thread.yield();
                }
            }
        } catch (ExecutionException e) {
            throw new InitializationError(e);
        } finally {
            LOCK.unlock();
        }
    }

    /**
     * Launch.
     */
    protected static void launch() {
        Application.launch();
    }

    /**
     * An empty start method.
     *
     * @param stage
     *            The stage
     */
    @Override
    public void start(final Stage stage) {
        started.set(Boolean.TRUE);
    }
}