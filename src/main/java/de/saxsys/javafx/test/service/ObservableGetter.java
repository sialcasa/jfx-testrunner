package de.saxsys.javafx.test.service;

import javafx.beans.value.ObservableValue;

@FunctionalInterface
public interface ObservableGetter {
	
	/**
	 * @param model
	 *            the model instance.
	 * @return the value of the field.
	 */
	ObservableValue getValue();
}