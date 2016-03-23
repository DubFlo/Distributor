package test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

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