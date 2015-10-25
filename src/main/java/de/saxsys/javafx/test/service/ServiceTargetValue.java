package de.saxsys.javafx.test.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Worker.State;
import de.saxsys.javafx.test.JfxRunner;

/**
 * This is a helper class for testing services. This class enables you to test your service with a J-Unit test. You need
 * a bootstrapped FX Application, so you should use the {@link JfxRunner} for your test execution.
 * <p>
 * Code example to test the Service state in the {@link State}==SUCCEEDED:
 * <p>
 * TestService service = new TestService(); <br/>
 * ServiceTestHelper<String> serviceTestHelper = new ServiceTestHelper<String>(service, 5); <br/>
 * ServiceMock<String> serviceMock = serviceTestHelper.runServiceUntiTargetValueReached(service::stateProperty,
 * State.SUCCEEDED); <br/>
 * assertEquals("I'm an expensive result.", serviceMock.getValue());
 * 
 * @author sialcasa
 *
 * @param <V>
 */
public class ServiceTargetValue<V> {
	
	private final Service<V> service;
	private final long timeoutInSeconds;
	
	/**
	 * Initializes the {@link ServiceTargetValue} with the Service to be tested with a default timeout of 1 second,
	 * which will be used for the following executions of
	 * {@link #runServiceUntiTargetValueReached(ObservableGetter, Object)}.
	 * 
	 * @param service
	 */
	public ServiceTargetValue(Service<V> service) {
		this.service = service;
		this.timeoutInSeconds = 1;
	}
	
	/**
	 * Initializes the {@link ServiceTargetValue} with the Service to be tested and a default timeout which will be used
	 * for the following executions of {@link #runServiceUntiTargetValueReached(ObservableGetter, Object)}.
	 * 
	 * @param service
	 * @param timeoutInSeconds
	 */
	public ServiceTargetValue(Service<V> service, long timeoutInSeconds) {
		this.service = service;
		this.timeoutInSeconds = timeoutInSeconds;
	}
	
	/**
	 * Starts the {@link Service} and returns a mirrored object with the result state after it reached the @targetValue
	 * of the {@link ObservableValue} which is taken by the @observableGetter. This could be for example the
	 * {@link Service#valueProperty()}.
	 * 
	 * @param observableGetter
	 *            which is the value to be testet (e.g. {@link Service#valueProperty()})
	 * @param targetValue
	 *            which is the value to be reached (e.g. State {@link State.SUCCEEDED})
	 * @param timeoutInSeconds
	 *            that fails the execution if the @targetValue could not reached on @observableGetter in time
	 * @return
	 */
	synchronized public ServiceMock<V> startAndWaitForValue(ObservableGetter observableGetter,
			Object targetValue,
			long timeoutInSeconds) throws InterruptedException, ExecutionException, TimeoutException
	{
		ServiceMock<V> serviceMock = callService(observableGetter, targetValue, timeoutInSeconds, () -> service.start());
		
		return serviceMock;
	}
	
	/**
	 * Restarts the {@link Service} and returns a mirrored object with the result state after it reached the @targetValue
	 * of the {@link ObservableValue} which is taken by the @observableGetter. This could be for example the
	 * {@link Service#valueProperty()}.
	 * 
	 * @param observableGetter
	 *            which is the value to be testet (e.g. {@link Service#valueProperty()})
	 * @param targetValue
	 *            which is the value to be reached (e.g. State {@link State.SUCCEEDED})
	 * @param timeoutInSeconds
	 *            that fails the execution if the @targetValue could not reached on @observableGetter in time
	 * @return
	 */
	synchronized public ServiceMock<V> restartAndWaitForValue(ObservableGetter observableGetter,
			Object targetValue,
			long timeoutInSeconds) throws InterruptedException, ExecutionException, TimeoutException
	{
		ServiceMock<V> serviceMock = callService(observableGetter, targetValue, timeoutInSeconds,
				() -> service.restart());
		
		return serviceMock;
	}
	
	private ServiceMock<V> callService(ObservableGetter observableGetter, Object targetValue, long timeoutInSeconds,
			Runnable serviceCall)
			throws InterruptedException, ExecutionException, TimeoutException {
		CompletableFuture<ServiceMock<V>> future = new CompletableFuture<>();
		
		ChangeListener changeListener = (b, o, newValue) -> {
			if (newValue == targetValue) {
				future.complete(new ServiceMock<V>(service));
			}
		};
		
		Platform.runLater(() -> {
			observableGetter.getValue().addListener(changeListener);
			serviceCall.run();
		});
		
		ServiceMock<V> serviceMock = future.get(timeoutInSeconds, TimeUnit.SECONDS);
		
		CompletableFuture<Void> removedListenerFuture = new CompletableFuture<Void>();
		
		Platform.runLater(() -> {
			observableGetter.getValue().removeListener(changeListener);
			removedListenerFuture.complete(null);
		});
		
		removedListenerFuture.get(1, TimeUnit.SECONDS);
		return serviceMock;
	}
	
}
