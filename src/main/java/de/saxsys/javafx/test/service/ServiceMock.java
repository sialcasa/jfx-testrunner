package de.saxsys.javafx.test.service;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Worker;

public class ServiceMock<V> implements Worker<V> {
	
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
	
	
	@Override
	public final ObjectProperty<State> stateProperty() {
		return this.state;
	}
	
	@Override
	public final javafx.concurrent.Worker.State getState() {
		return this.stateProperty().get();
	}
	
	public final void setState(final javafx.concurrent.Worker.State state) {
		this.stateProperty().set(state);
	}
	
	@Override
	public final ObjectProperty<V> valueProperty() {
		return this.value;
	}
	
	@Override
	public final V getValue() {
		return this.valueProperty().get();
	}
	
	public final void setValue(final V value) {
		this.valueProperty().set(value);
	}
	
	@Override
	public final ObjectProperty<Throwable> exceptionProperty() {
		return this.exception;
	}
	
	@Override
	public final java.lang.Throwable getException() {
		return this.exceptionProperty().get();
	}
	
	public final void setException(final java.lang.Throwable exception) {
		this.exceptionProperty().set(exception);
	}
	
	@Override
	public final DoubleProperty workDoneProperty() {
		return this.workDone;
	}
	
	@Override
	public final double getWorkDone() {
		return this.workDoneProperty().get();
	}
	
	public final void setWorkDone(final double workDone) {
		this.workDoneProperty().set(workDone);
	}
	
	public final DoubleProperty totalWorkToBeDoneProperty() {
		return this.totalWorkToBeDone;
	}
	
	public final double getTotalWorkToBeDone() {
		return this.totalWorkToBeDoneProperty().get();
	}
	
	public final void setTotalWorkToBeDone(final double totalWorkToBeDone) {
		this.totalWorkToBeDoneProperty().set(totalWorkToBeDone);
	}
	
	@Override
	public final DoubleProperty progressProperty() {
		return this.progress;
	}
	
	@Override
	public final double getProgress() {
		return this.progressProperty().get();
	}
	
	public final void setProgress(final double progress) {
		this.progressProperty().set(progress);
	}
	
	@Override
	public final BooleanProperty runningProperty() {
		return this.running;
	}
	
	@Override
	public final boolean isRunning() {
		return this.runningProperty().get();
	}
	
	public final void setRunning(final boolean running) {
		this.runningProperty().set(running);
	}
	
	@Override
	public final StringProperty messageProperty() {
		return this.message;
	}
	
	@Override
	public final java.lang.String getMessage() {
		return this.messageProperty().get();
	}
	
	public final void setMessage(final java.lang.String message) {
		this.messageProperty().set(message);
	}
	
	@Override
	public final StringProperty titleProperty() {
		return this.title;
	}
	
	@Override
	public final java.lang.String getTitle() {
		return this.titleProperty().get();
	}
	
	public final void setTitle(final java.lang.String title) {
		this.titleProperty().set(title);
	}
	
	@Override
	public double getTotalWork() {
		return totalWorkToBeDone.get();
	}
	
	@Override
	public ReadOnlyDoubleProperty totalWorkProperty() {
		return totalWorkToBeDone;
	}
	
	@Override
	public boolean cancel() {
		throw new RuntimeException("This method can't be used.");
	}
}
