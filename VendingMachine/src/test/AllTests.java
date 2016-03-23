package test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Launches all the other tests of this package.
 * Note that because of the final "S", it is not executed by the build.xml.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
  ChangeMachineTest.class,
  ChangeTest.class,
  HeatingSystemTest.class,
  StockTest.class,
  UtilsTest.class,
  ContextTest.class
})

public class AllTests {

}