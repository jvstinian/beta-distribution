package test.java;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.jvstinian.math.InvalidParameterException;
import com.jvstinian.math.probability.BetaDistributionCDF;
import org.junit.Test;

public class TestBetaDistributionCDF {
  @Test
  public void testBetaDistributionCDF() {
    try {
      BetaDistributionCDF cdf = new BetaDistributionCDF(0.5, 0.5);

      assertTrue(Math.abs(cdf.value(0.1) - 0.2048327647) < 1e-10);
      assertTrue(Math.abs(cdf.value(0.25) - 0.3333333333) < 1e-10);
      assertTrue(Math.abs(cdf.value(0.5) - 0.5) < 1e-10);
      assertTrue(Math.abs(cdf.value(0.75) - 0.6666666667) < 1e-10);
      assertTrue(Math.abs(cdf.value(0.9) - 0.7951672353) < 1e-10);
    } catch (InvalidParameterException e) {
      fail("Got exception: " + e);
    }
  }
}
