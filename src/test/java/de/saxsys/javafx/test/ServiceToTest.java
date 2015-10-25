package de.saxsys.javafx.test;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ServiceToTest extends Service<String> {
	int counter = 0;
	
	@Override
	protected Task<String> createTask() {
		return new Task<String>() {
			@Override
			protected String call() throws Exception {
				try {
					Thread.sleep(1000);
					updateProgress(1, 1);
					updateMessage("Test");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				counter++;
				return "I'm an expensive result " + counter;
			}
		};
	}
}
