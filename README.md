JavaFX JUnit-4 Testrunner
==============
###Testrunner is going to be merged to [mvvmfx](https://github.com/sialcasa/mvvmFX)###
####1.2 is the last version of testrunner - check this page in the next weeks for more information####

---

based on http://awhite.blogspot.de/2013/04/javafx-junit-testing.html - Credits to Andy White

[![Build Status](https://travis-ci.org/sialcasa/jfx-testrunner.svg?branch=master)](https://travis-ci.org/sialcasa/jfx-testrunner)
###Maven dependency###

```
<dependency>
		<groupId>de.saxsys</groupId>
		<artifactId>jfx-testrunner</artifactId>
		<version>1.2</version>
</dependency>
```

####Use the testrunner to bootstrap JavaFX for your tests####
```
@RunWith(JfxRunner.class)
public class TestClass {
...
}
```

####Use @TestInJfxThread annotation to run specific tests in the JavaFX-thread####
```
@RunWith(JfxRunner.class)
public class TestClass {

    @Test
    @TestInJfxThread
    public void testWithFXThread() throws Exception {
        Assert.assertTrue(Platform.isFxApplicationThread());
    }
    
    @Test
    public void testWithFXThread() throws Exception {
        Assert.assertFalse(Platform.isFxApplicationThread());
    }
}
```

####Easy testing of Services with Servicewrapper####

- Allows you to wait for a Service execution
- Allows you to access the Service properties, without getting Runtime Exceptions because you accessed them from outside of the JavaFX-thread

```
@RunWith(JfxRunner.class)
public class TestClass {
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
}
```


####Advanced Testing of JavaFX Services####

- Allows you to wait for specific property values of the Service

```
@RunWith(JfxRunner.class)
public class TestClass {
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

