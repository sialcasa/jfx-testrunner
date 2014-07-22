JavaFX JUnit-4 Testrunner
==============

based on http://awhite.blogspot.de/2013/04/javafx-junit-testing.html - Credits to Andy White

###Maven dependency###

```
<dependency>
		<groupId>de.saxsys</groupId>
		<artifactId>jfx-testrunner</artifactId>
		<version>1.0</version>
</dependency>
```

#How To - Choose whether the test should be performed in the JavaFX Thread or not#

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
}
```

