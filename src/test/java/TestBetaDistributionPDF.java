package test.java;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.jvstinian.math.InvalidParameterException;
import com.jvstinian.math.probability.BetaDistributionPDF;
import org.junit.Test;

public class TestBetaDistributionPDF {
  @Test
  public void testBetaDistributionPDF() {
    try {
      BetaDistributionPDF cdf = new BetaDistributionPDF(0.5, 0.5);

      assertTrue(Math.abs(cdf.value(0.1) - 1.061032954) < 1e-10);
      assertTrue(Math.abs(cdf.value(0.25) - 0.7351051939) < 1e-10);
      assertTrue(Math.abs(cdf.value(0.5) - 0.6366197723) < 1e-8);
      assertTrue(Math.abs(cdf.value(0.75) - 0.7351051939) < 1e-10);
      assertTrue(Math.abs(cdf.value(0.9) - 1.061032954) < 1e-10);
    } catch (InvalidParameterException e) {
      fail("Got exception: " + e);
    }
  }
}
