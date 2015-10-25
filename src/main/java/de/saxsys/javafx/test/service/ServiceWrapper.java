package de.saxsys.javafx.test.service;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Service;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;

/**
 * Class which wrapps a Service and delegates the calls to this service in the UI-Thread. It helps you to access the
 * values of the Services from outside of the UI-Thread.
 * 
 * @author sialcasa
 */
public class ServiceWrapper implements Worker {
	
	private final Service service;
	
	/**
	 * Create the Wrapper with a given service.
	 * 
	 * @param service
	 */
	public ServiceWrapper(Service service) {
		this.service = service;
	}
	
	/**
	 * Calls the {@link Service#start()} method of the given service and blocks the caller thread until the state
	 * RUNNING, FAILED, or CANCELLED (was RUNNING befor) is reached.
	 * 
	 * @param timeout
	 *            maximum duration of the call
	 * 
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	public void startAndWait(long timeout) throws InterruptedException, ExecutionException, TimeoutException {
		callAndWaitService(timeout, () -> service.start());
	}
	
	/**
	 * Calls the {@link Service#restart()} method of the given service and blocks the caller thread until the state
	 * RUNNING, FAILED, or CANCELLED (was RUNNING befor) is reached.
	 * 
	 * @param timeout
	 *            maximum duration of the call
	 * 
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	public void restartAndWait(long timeout) throws InterruptedException, ExecutionException, TimeoutException {
		callAndWaitService(timeout, () -> service.restart());
	}
	
	private void callAndWaitService(long timeout, Runnable serviceCall) throws InterruptedException,
			ExecutionException, TimeoutException {
		CompletableFuture<Void> called = new CompletableFuture<>();
		
		ChangeListener<State> listener = (ChangeListener<State>) (observable, oldValue, newValue) -> {
			if (newValue == State.CANCELLED && oldValue == State.RUNNING || newValue == State.FAILED
					|| newValue == State.SUCCEEDED) {
				called.complete(null);
			}
		};
		
		Platform.runLater(() -> {
			service.stateProperty().addListener(listener);
			serviceCall.run();
		});
		called.get(timeout, TimeUnit.MILLISECONDS);
		
		CompletableFuture<Void> removeListener = new CompletableFuture<>();
		Platform.runLater(() -> {
			service.stateProperty().removeListener(listener);
			removeListener.complete(null);
		});
		removeListener.get(1000, TimeUnit.MILLISECONDS);
	}
	
	// Async logic for delegates
	
	private <T extends Object> T callMethodInUIThread(Supplier<T> call) {
		CompletableFuture<T> called = new CompletableFuture<>();
		Platform.runLater(() -> {
			try {
				called.complete(call.get());
			} catch (Exception e) {
				called.completeExceptionally(e);
			}
		});
		
		try {
			return called.get();
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
	
	// DELEGATES
	
	@Override
	public final State getState() {
		return callMethodInUIThread(() -> service.getState());
	}
	
	@Override
	public final ReadOnlyObjectProperty stateProperty() {
		return service.stateProperty();
	}
	
	@Override
	public final Object getValue() {
		return callMethodInUIThread(() -> service.getValue());
	}
	
	@Override
	public final ReadOnlyObjectProperty valueProperty() {
		return callMethodInUIThread(() -> service.valueProperty());
	}
	
	@Override
	public final Throwable getException() {
		return callMethodInUIThread(() -> service.getException());
	}
	
	@Override
	public final ReadOnlyObjectProperty exceptionProperty() {
		return callMethodInUIThread(() -> service.exceptionProperty());
	}
	
	@Override
	public final double getWorkDone() {
		return callMethodInUIThread(() -> service.getWorkDone());
	}
	
	@Override
	public final ReadOnlyDoubleProperty workDoneProperty() {
		return callMethodInUIThread(() -> service.workDoneProperty());
	}
	
	@Override
	public final double getTotalWork() {
		return callMethodInUIThread(() -> service.getTotalWork());
	}
	
	@Override
	public final ReadOnlyDoubleProperty totalWorkProperty() {
		return callMethodInUIThread(() -> service.totalWorkProperty());
	}
	
	@Override
	public final double getProgress() {
		return callMethodInUIThread(() -> service.getProgress());
	}
	
	@Override
	public final ReadOnlyDoubleProperty progressProperty() {
		return callMethodInUIThread(() -> service.progressProperty());
	}
	
	@Override
	public final boolean isRunning() {
		return callMethodInUIThread(() -> service.isRunning());
	}
	
	@Override
	public final ReadOnlyBooleanProperty runningProperty() {
		return callMethodInUIThread(() -> service.runningProperty());
	}
	
	@Override
	public final String getMessage() {
		return callMethodInUIThread(() -> service.getMessage());
	}
	
	@Override
	public final ReadOnlyStringProperty messageProperty() {
		return callMethodInUIThread(() -> service.messageProperty());
	}
	
	@Override
	public final String getTitle() {
		return callMethodInUIThread(() -> service.getTitle());
	}
	
	@Override
	public final ReadOnlyStringProperty titleProperty() {
		return callMethodInUIThread(() -> service.titleProperty());
	}
	
	
	public final Executor getExecutor() {
		return callMethodInUIThread(() -> service.getExecutor());
	}
	
	public final ObjectProperty executorProperty() {
		return callMethodInUIThread(() -> service.executorProperty());
	}
	
	public final ObjectProperty onReadyProperty() {
		return callMethodInUIThread(() -> service.onReadyProperty());
	}
	
	public final EventHandler getOnReady() {
		return callMethodInUIThread(() -> service.getOnReady());
	}
	
	public final ObjectProperty onScheduledProperty() {
		return callMethodInUIThread(() -> service.onScheduledProperty());
	}
	
	public final EventHandler getOnScheduled() {
		return callMethodInUIThread(() -> service.getOnScheduled());
	}
	
	
	public final ObjectProperty onRunningProperty() {
		return callMethodInUIThread(() -> service.onRunningProperty());
	}
	
	public final EventHandler getOnRunning() {
		return callMethodInUIThread(() -> service.getOnRunning());
	}
	
	
	public final ObjectProperty onSucceededProperty() {
		return callMethodInUIThread(() -> service.onSucceededProperty());
	}
	
	public final EventHandler getOnSucceeded() {
		return callMethodInUIThread(() -> service.getOnSucceeded());
	}
	
	public final ObjectProperty onCancelledProperty() {
		return callMethodInUIThread(() -> service.onCancelledProperty());
	}
	
	public final EventHandler getOnCancelled() {
		return callMethodInUIThread(() -> service.getOnCancelled());
	}
	
	
	public final ObjectProperty onFailedProperty() {
		return callMethodInUIThread(() -> service.onFailedProperty());
	}
	
	public final EventHandler getOnFailed() {
		return callMethodInUIThread(() -> service.getOnFailed());
	}
	
	@Override
	public boolean cancel() {
		return callMethodInUIThread(() -> service.cancel());
	}
	
	public void reset() {
		callMethodInUIThread(() -> {
			service.reset();
			return null;
		});
	}
	
	
}
