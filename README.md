JavaFX JUnit-4 Testrunner
==============

based on http://awhite.blogspot.de/2013/04/javafx-junit-testing.html - Credits to Andy White

[![Build Status](https://travis-ci.org/sialcasa/jfx-testrunner.svg?branch=master)](https://travis-ci.org/sialcasa/jfx-testrunner)
###Maven dependency###

```
<dependency>
		<groupId>de.saxsys</groupId>
		<artifactId>jfx-testrunner</artifactId>
		<version>1.2-SNAPSHOT</version>
</dependency>
```

####How To - Choose whether the test should be performed in the JavaFX Thread or not####

```
@RunWith(JfxRunner.class)
public class TestClass {

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
	public void testMultipleServiceCallsWithWrapper() throws Exception {
		
		ServiceWrapper wrapper = new ServiceWrapper(new ServiceToTest());
		wrapper.startAndWait(5000);
		
		assertEquals("I'm an expensive result 1", wrapper.getValue());
		assertEquals(1.0, wrapper.getProgress(), 1);
		assertEquals("Test", wrapper.getMessage());
		
		wrapper.reset();
		assertEquals(null, wrapper.getValue());
		assertEquals(-1, wrapper.getProgress(), 1);
		
		wrapper.startAndWait(5000);
		assertEquals("I'm an expensive result 2", wrapper.getValue());
		
		wrapper.restartAndWait(5000);
		assertEquals("I'm an expensive result 3", wrapper.getValue());
	}
	
	@Test
	public void testMultipleServiceCallsUsingTargetValue() throws ExecutionException, InterruptedException,
			TimeoutException {
		ServiceWrapper wrapper = new ServiceWrapper(new ServiceToTest());
		
		wrapper.startAndWaitForValue(
				service::stateProperty, State.SUCCEEDED, 5000);
		assertEquals("I'm an expensive result 1", wrapper.getValue());
		
		wrapper.restartAndWaitForValue(service::stateProperty,
				State.SUCCEEDED, 5000);
		assertEquals("I'm an expensive result 2", wrapper.getValue());
		assertEquals(State.SUCCEEDED, wrapper.getState());
	}
}
```


