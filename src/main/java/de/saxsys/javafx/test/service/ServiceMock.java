package de.saxsys.javafx.test.service;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;

/**
 * Mirror of {@link Worker} / {@link Service} to be used for testing to avoid thread-access-issues.
 * 
 * @author sialcasa
 *
 * @param <V>
 *            type of the Service which should be testet
 */
public class ServiceMock<V> {
	
	private final ObjectProperty<State> state = new SimpleObjectProperty<>(this, "state", State.READY);
	private final ObjectProperty<V> value = new SimpleObjectProperty<>(this, "value");
	private final ObjectProperty<Throwable> exception = new SimpleObjectProperty<>(this, "exception");
	private final DoubleProperty workDone = new SimpleDoubleProperty(this, "workDone", -1);
	private final DoubleProperty totalWorkToBeDone = new SimpleDoubleProperty(this, "totalWorkToBeDone", -1);
	private final DoubleProperty progress = new SimpleDoubleProperty(this, "progress", -1);
	private final BooleanProperty running = new SimpleBooleanProperty(this, "running", false);
	private final StringProperty message = new SimpleStringProperty(this, "message", "");
	private final StringProperty title = new SimpleStringProperty(this, "title", "");
	
	public ServiceMock(Service<V> service) {
		this.setException(service.getException());
		this.setMessage(service.getMessage());
		this.setProgress(service.getProgress());
		this.setRunning(service.isRunning());
		this.setState(service.getState());
		this.setTitle(service.getTitle());
		this.setTotalWorkToBeDone(service.getTotalWork());
		this.setValue(service.getValue());
		this.setWorkDone(service.getWorkDone());
	}
	
	
	public final Worker.State getState() {
		return state.get();
	}
	
	private final void setState(final javafx.concurrent.Worker.State state) {
		this.state.set(state);
	}
	
	public final V getValue() {
		return value.get();
	}
	
	private final void setValue(final V value) {
		this.value.set(value);
	}
	
	public final Throwable getException() {
		return this.exception.get();
	}
	
	private final void setException(final Throwable exception) {
		this.exception.set(exception);
	}
	
	public final double getWorkDone() {
		return this.workDone.get();
	}
	
	private final void setWorkDone(final double workDone) {
		this.workDone.set(workDone);
	}
	
	
	public final double getTotalWorkToBeDone() {
		return this.totalWorkToBeDone.get();
	}
	
	private final void setTotalWorkToBeDone(final double totalWorkToBeDone) {
		this.totalWorkToBeDone.set(totalWorkToBeDone);
	}
	
	public final double getProgress() {
		return this.progress.get();
	}
	
	private final void setProgress(final double progress) {
		this.progress.set(progress);
	}
	
	
	public final boolean isRunning() {
		return this.running.get();
	}
	
	private final void setRunning(final boolean running) {
		this.running.set(running);
	}
	
	public final String getMessage() {
		return this.message.get();
	}
	
	private final void setMessage(final String message) {
		this.message.set(message);
	}
	
	
	public final String getTitle() {
		return this.title.get();
	}
	
	private final void setTitle(final String title) {
		this.title.set(title);
	}
	
	public double getTotalWork() {
		return totalWorkToBeDone.get();
	}
}
