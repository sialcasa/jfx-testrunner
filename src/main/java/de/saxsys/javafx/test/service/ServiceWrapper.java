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
import javafx.concurrent.Worker.State;
import javafx.event.EventHandler;


public class ServiceWrapper {
	
	private final Service service;
	
	public ServiceWrapper(Service service) {
		this.service = service;
	}
	
	public void startAndWait(long timeout) throws InterruptedException, ExecutionException, TimeoutException {
		callAndWaitService(timeout, () -> service.start());
	}
	
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
	
	
	
	// DELEGATES
	
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
	
	public final State getState() {
		return callMethodInUIThread(() -> service.getState());
	}
	
	public final ReadOnlyObjectProperty stateProperty() {
		return service.stateProperty();
	}
	
	public final Object getValue() {
		return callMethodInUIThread(() -> service.getValue());
	}
	
	public final ReadOnlyObjectProperty valueProperty() {
		return callMethodInUIThread(() -> service.valueProperty());
	}
	
	public final Throwable getException() {
		return callMethodInUIThread(() -> service.getException());
	}
	
	public final ReadOnlyObjectProperty exceptionProperty() {
		return callMethodInUIThread(() -> service.exceptionProperty());
	}
	
	public final double getWorkDone() {
		return callMethodInUIThread(() -> service.getWorkDone());
	}
	
	public final ReadOnlyDoubleProperty workDoneProperty() {
		return callMethodInUIThread(() -> service.workDoneProperty());
	}
	
	public final double getTotalWork() {
		return callMethodInUIThread(() -> service.getTotalWork());
	}
	
	public final ReadOnlyDoubleProperty totalWorkProperty() {
		return callMethodInUIThread(() -> service.totalWorkProperty());
	}
	
	public final double getProgress() {
		return callMethodInUIThread(() -> service.getProgress());
	}
	
	public final ReadOnlyDoubleProperty progressProperty() {
		return callMethodInUIThread(() -> service.progressProperty());
	}
	
	public final boolean isRunning() {
		return callMethodInUIThread(() -> service.isRunning());
	}
	
	public final ReadOnlyBooleanProperty runningProperty() {
		return callMethodInUIThread(() -> service.runningProperty());
	}
	
	public final String getMessage() {
		return callMethodInUIThread(() -> service.getMessage());
	}
	
	public final ReadOnlyStringProperty messageProperty() {
		return callMethodInUIThread(() -> service.messageProperty());
	}
	
	public final String getTitle() {
		return callMethodInUIThread(() -> service.getTitle());
	}
	
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
