package test.java;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.jvstinian.math.InvalidParameterException;
import com.jvstinian.math.probability.BetaDistribution;
import java.util.Random;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;
import org.junit.Test;

public class TestBetaDistribution {
  @Test
  public void testBetaDistribution() {
    try {
      BetaDistribution dist = new BetaDistribution(0.5, 0.5);
      UnivariateDifferentiableFunction pdf = dist.getPDF();

      assertTrue(Math.abs(pdf.value(0.1) - 1.061032954) < 1e-10);
      assertTrue(Math.abs(pdf.value(0.25) - 0.7351051939) < 1e-10);
      assertTrue(Math.abs(pdf.value(0.5) - 0.6366197723) < 1e-10);
      assertTrue(Math.abs(pdf.value(0.75) - 0.7351051939) < 1e-10);
      assertTrue(Math.abs(pdf.value(0.9) - 1.061032954) < 1e-10);
    } catch (InvalidParameterException e) {
      fail("Got exception: " + e);
    }
  }

  @Test
  public void testBetaDistributionCDF() {
    try {
      BetaDistribution dist = new BetaDistribution(0.5, 0.5);
      UnivariateDifferentiableFunction cdf = dist.getCDF();

      assertTrue(Math.abs(cdf.value(0.1) - 0.2048327647) < 1e-10);
      assertTrue(Math.abs(cdf.value(0.25) - 0.3333333333) < 1e-10);
      assertTrue(Math.abs(cdf.value(0.5) - 0.5) < 1e-10);
      assertTrue(Math.abs(cdf.value(0.75) - 0.6666666667) < 1e-10);
      assertTrue(Math.abs(cdf.value(0.9) - 0.7951672353) < 1e-10);
    } catch (InvalidParameterException e) {
      fail("Got exception: " + e);
    }
  }

  @Test
  public void testBetaCDFDerivative() {
    try {
      // Select parameters so that beta distribution is
      // the uniform distribution.
      // The CDF is then the identity function.
      BetaDistribution dist = new BetaDistribution(1.0, 1.0);
      UnivariateDifferentiableFunction cdf = dist.getCDF();
      DerivativeStructure ds = new DerivativeStructure(1, 2, 0, 0.25);
      double[] derivs = cdf.value(ds).getAllDerivatives();
      assertTrue(Math.abs(derivs[0] - 0.25) < 1e-10);
      assertTrue(Math.abs(derivs[1] - 1.0) < 1e-10);
      assertTrue(Math.abs(derivs[2] - 0.0) < 1e-10);
    } catch (InvalidParameterException e) {
      System.err.println(e);
    }
  }

  @Test
  public void testBetaDistributionPDFOutsideSupport() {
    try {
      Random rnd = new Random();
      double a = 0.5 + 2.0 * rnd.nextDouble();
      double b = 0.5 + 2.0 * rnd.nextDouble();

      BetaDistribution dist = new BetaDistribution(a, b);
      UnivariateDifferentiableFunction pdf = dist.getPDF();

      double[] testPoints = new double[] {-0.1, 0.0, 1.0, 1.1};
      for (int i = 0; i < testPoints.length; i++) {
        assertTrue(Math.abs(pdf.value(testPoints[i]) - 0.0) < 1e-10);
      }
    } catch (InvalidParameterException e) {
      fail("Got exception: " + e);
    }
  }

  @Test
  public void testBetaDistributionCDFOutsideSupport() {
    try {
      Random rnd = new Random();
      double a = 0.5 + 2.0 * rnd.nextDouble();
      double b = 0.5 + 2.0 * rnd.nextDouble();

      BetaDistribution dist = new BetaDistribution(a, b);
      UnivariateDifferentiableFunction cdf = dist.getCDF();

      double[] testPointsLower = new double[] {-1.0, -0.1, 0.0};
      for (int i = 0; i < testPointsLower.length; i++) {
        assertTrue(Math.abs(cdf.value(testPointsLower[i]) - 0.0) < 1e-10);
      }

      double[] testPointsUpper = new double[] {1.0, 1.1, 2.0};
      for (int i = 0; i < testPointsUpper.length; i++) {
        assertTrue(Math.abs(cdf.value(testPointsUpper[i]) - 1.0) < 1e-10);
      }
    } catch (InvalidParameterException e) {
      fail("Got exception: " + e);
    }
  }
}
