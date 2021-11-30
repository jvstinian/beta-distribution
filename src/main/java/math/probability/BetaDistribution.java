/*
 * Copyright 2021 Justin Smith, jvstinian.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jvstinian.math.probability;

import com.jvstinian.math.InvalidParameterException;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;
import org.apache.commons.math3.analysis.solvers.BisectionSolver;
import org.apache.commons.math3.special.Beta;

public class BetaDistribution extends BetaDistributionParameters {
  public BetaDistribution(double a, double b) throws InvalidParameterException {
    super(a, b);
  }

  public BetaDistribution(BetaDistributionParameters params) {
    super(params);
  }

  private static class BetaDistributionPDF extends BetaDistributionParameters
      implements UnivariateDifferentiableFunction {
    public BetaDistributionPDF(BetaDistributionParameters params) {
      super(params);
    }

    public double value(double x) {
      DerivativeStructure c = new DerivativeStructure(1, 0, x);
      return this.value(c).getValue();
    }

    public DerivativeStructure value(DerivativeStructure t) {
      final double tval = t.getValue();
      if ((tval <= 0.0) || (tval >= 1.0)) {
        return new DerivativeStructure(1, 0, 0.0); // constant 0.0
      } else {
        DerivativeStructure x1 = t.pow(this.alpha - 1.0); // x^(\alpha - 1}
        DerivativeStructure x2 = t.subtract(1.0).negate().pow(this.beta - 1.0); // (1-x)^{\beta - 1}
        DerivativeStructure denom = t.createConstant(Math.exp(Beta.logBeta(this.alpha, this.beta)));
        return x1.multiply(x2).divide(denom);
      }
    }
  };

  public UnivariateDifferentiableFunction getPDF() {
    return new BetaDistributionPDF((BetaDistributionParameters) this);
  }

  private static class BetaDistributionCDF extends BetaDistributionParameters
      implements UnivariateDifferentiableFunction {
    private BetaDistributionPDF pdf;

    public BetaDistributionCDF(BetaDistributionParameters params) {
      super(params);
      this.pdf = new BetaDistributionPDF(params);
    }

    public double value(double x) {
      if (x <= 0.0) {
        return 0.0;
      } else if (x >= 1.0) {
        return 1.0;
      } else {
        return Beta.regularizedBeta(x, this.alpha, this.beta);
      }
    }

    public DerivativeStructure value(DerivativeStructure t) {
      final double x = t.getValue();
      if (x <= 0.0) {
        return new DerivativeStructure(1, 0, 0.0); // constant 0.0
      } else if (x >= 1.0) {
        return new DerivativeStructure(1, 0, 1.0); // constant 1.0
      } else {
        final int order = t.getOrder();
        double[] f = new double[order + 1];

        f[0] = this.value(x);

        if (order > 0) {
          DerivativeStructure ds = new DerivativeStructure(1, order - 1, 0, x);
          double[] derivs = this.pdf.value(ds).getAllDerivatives();
          System.arraycopy(derivs, 0, f, 1, derivs.length);
        }
        return t.compose(f);
      }
    }
  };

  public UnivariateDifferentiableFunction getCDF() {
    return new BetaDistributionCDF((BetaDistributionParameters) this);
  }

  private static class BetaDistributionSurvivalFunction extends BetaDistributionParameters
      implements UnivariateDifferentiableFunction {
    private BetaDistributionCDF cdf;

    public BetaDistributionSurvivalFunction(BetaDistributionParameters params) {
      super(params);
      this.cdf = new BetaDistributionCDF(params);
    }

    public double value(double x) {
      return 1.0 - this.cdf.value(x);
    }

    public DerivativeStructure value(DerivativeStructure t) {
      return this.cdf.value(t).subtract(1.0).negate();
    }
  };

  public UnivariateDifferentiableFunction getSurvivalFunction() {
    return new BetaDistributionSurvivalFunction((BetaDistributionParameters) this);
  }

  private class BetaDistributionQuantileObjective implements UnivariateDifferentiableFunction {
    private BetaDistributionCDF cdf;
    private double prob;

    public BetaDistributionQuantileObjective(BetaDistributionCDF _cdf, double q) {
      this.cdf = _cdf;
      this.prob = q;
    }

    public double value(double x) {
      return this.cdf.value(x) - this.prob;
    }

    public DerivativeStructure value(DerivativeStructure t) {
      return this.cdf.value(t).subtract(this.prob);
    }
  };

  public double[] calculateQuantiles(double[] probs) {
    double[] result = new double[probs.length];

    BetaDistributionCDF cdf = new BetaDistributionCDF((BetaDistributionParameters) this);
    for (int i = 0; i < probs.length; i++) {
      double prob = probs[i];
      BisectionSolver solver = new BisectionSolver();
      BetaDistributionQuantileObjective betaqobj = new BetaDistributionQuantileObjective(cdf, prob);
      result[i] = solver.solve(1000000, betaqobj, 0.0, 1.0);
    }

    return result;
  }
};
