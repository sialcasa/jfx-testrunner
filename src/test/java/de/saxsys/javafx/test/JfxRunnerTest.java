package de.saxsys.javafx.test;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javafx.application.Platform;
import javafx.concurrent.Worker.State;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.saxsys.javafx.test.service.ServiceWrapper;

//Tests dont run in travis CI because its headless
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
	
	@Test
	public void testMultipleServiceCallsUsingTargetValue() throws ExecutionException, InterruptedException,
			TimeoutException {
		ServiceToTest service = new ServiceToTest();
		ServiceWrapper wrapper = new ServiceWrapper(service);
		
		wrapper.startAndWaitForValue(
				service::stateProperty, State.SUCCEEDED, 5000);
		assertEquals("I'm an expensive result 1", wrapper.getValue());
		
		wrapper.restartAndWaitForValue(service::stateProperty,
				State.SUCCEEDED, 5000);
		assertEquals("I'm an expensive result 2", wrapper.getValue());
		assertEquals(State.SUCCEEDED, wrapper.getState());
	}
	
	@Test
	public void testMultipleServiceCallsWithWrapper() throws Exception {
		
		ServiceWrapper proxy = new ServiceWrapper(new ServiceToTest());
		proxy.startAndWait(5000);
		
		assertEquals("I'm an expensive result 1", proxy.getValue());
		assertEquals(1.0, proxy.getProgress(), 1);
		assertEquals("Test", proxy.getMessage());
		
		proxy.reset();
		assertEquals(null, proxy.getValue());
		assertEquals(-1, proxy.getProgress(), 1);
		
		proxy.startAndWait(5000);
		assertEquals("I'm an expensive result 2", proxy.getValue());
		
		proxy.restartAndWait(5000);
		assertEquals("I'm an expensive result 3", proxy.getValue());
		
	}
	
}
