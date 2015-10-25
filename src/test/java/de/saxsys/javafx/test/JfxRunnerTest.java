package de.saxsys.javafx.test;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker.State;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.saxsys.javafx.test.service.ServiceMock;
import de.saxsys.javafx.test.service.ServiceTargetValue;
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
	
	@Test(timeout = 6000)
	@TestInJfxThread
	public void testWithFXThreadWithTimeout() throws Exception {
		Assert.assertTrue(Platform.isFxApplicationThread());
	}
	
	@Test
	public void testMultipleServiceCallsWithServiceMock() throws ExecutionException, InterruptedException,
			TimeoutException {
		
		Service<String> service = new Service<String>() {
			int counter = 0;
			
			@Override
			protected Task<String> createTask() {
				return new Task<String>() {
					@Override
					protected String call() throws Exception {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						counter++;
						return "I'm an expensive result " + counter;
					}
				};
			}
		};
		
		ServiceTargetValue<String> serviceTestHelper = new ServiceTargetValue<String>(service, 5);
		
		ServiceMock<String> mockForTestOne = serviceTestHelper.runServiceUntiTargetValueReached(
				service::stateProperty, State.SUCCEEDED);
		assertEquals("I'm an expensive result 1", mockForTestOne.getValue());
		
		ServiceMock<String> mockForTestTwo = serviceTestHelper.runServiceUntiTargetValueReached(service::stateProperty,
				State.SUCCEEDED);
		assertEquals("I'm an expensive result 2", mockForTestTwo.getValue());
		assertEquals(State.SUCCEEDED, mockForTestTwo.getState());
	}
	
	@Test
	public void testUsingWrapper() throws Exception {
		
		ServiceWrapper proxy = new ServiceWrapper(new ServiceToTest());
		proxy.startAndWait(5000);
		assertEquals("I'm an expensive result 1", proxy.getValue());
		assertEquals(1.0, proxy.getProgress(), 1);
		assertEquals("Test", proxy.getMessage());
		
		proxy.restartAndWait(5000);
		assertEquals("I'm an expensive result 2", proxy.getValue());
		
	}
	
}
