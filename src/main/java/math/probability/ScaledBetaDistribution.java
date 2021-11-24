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

public class ScaledBetaDistribution extends ScaledBetaDistributionParameters {
  public ScaledBetaDistribution(double a, double b, double s) throws InvalidParameterException {
    super(a, b, s);
  }

  public ScaledBetaDistribution(BetaDistributionParameters params, double s) {
    super(params, s);
  }

  public ScaledBetaDistribution(ScaledBetaDistributionParameters params) {
    // super((BetaDistributionParameters) params, params.getScale());
    super(params);
  }

  /* TODO: Is this used?
  public double getMaxValue() {
    return this.scale;
  }
  */

  private static class CDF implements UnivariateDifferentiableFunction {
    private UnivariateDifferentiableFunction betacdf;
    private double scale;

    public CDF(ScaledBetaDistributionParameters params) {
      this.betacdf = (new BetaDistribution((BetaDistributionParameters) params)).getCDF();
      this.scale = params.getScale();
      ;
    }

    public double value(double x) {
      return this.betacdf.value(x / this.scale);
    }

    public DerivativeStructure value(DerivativeStructure t) {
      return this.betacdf.value(t.divide(this.scale));
    }

    /* TODO: Is this needed?
    public double getMaxValue() {
      return this.scale;
    }
    */
  };

  public static class SurvivalFunction implements UnivariateDifferentiableFunction {
    private CDF cdf;

    public SurvivalFunction(ScaledBetaDistributionParameters params) {
      this.cdf = new CDF(params);
    }

    public double value(double x) {
      return 1.0 - this.cdf.value(x);
    }

    public DerivativeStructure value(DerivativeStructure t) {
      return this.cdf.value(t).subtract(1.0).negate();
    }

    /* TODO: Is this needed?
    public double getMaxValue() {
      return this.cdf.getMaxValue();
    }
    */
  };

  public SurvivalFunction getSurvivalFunction() {
    return new SurvivalFunction((ScaledBetaDistributionParameters) this);
  }
}
