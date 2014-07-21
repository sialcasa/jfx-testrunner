JavaFX JUnit-4 Testrunner
==============

based on http://awhite.blogspot.de/2013/04/javafx-junit-testing.html - Credits to Andy White

###Maven dependency###

```
<dependency>
		<groupId>de.saxsys</groupId>
		<artifactId>jfx-testrunner</artifactId>
		<version>${jfx-version}</version>
</dependency>
```

###How to use###

```
@RunWith(JfxRunner.class)
public class TestClass {

	@Test
	public void testWithoutFXThread() throws Exception {
		Assert.assertFalse(Platform.isFxApplicationThread());
	}

	@Test
	@TestInJfxThread
	public void testWithFXThread() throws Exception {
		Assert.assertTrue(Platform.isFxApplicationThread());
	}

}
```

