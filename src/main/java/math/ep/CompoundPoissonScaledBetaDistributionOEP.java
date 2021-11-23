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

package com.jvstinian.math.ep;

import com.jvstinian.math.InvalidParameterException;
import com.jvstinian.math.probability.ScaledBetaDistributionSurvivalFunction;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;

public class CompoundPoissonScaledBetaDistributionOEP implements UnivariateDifferentiableFunction {
  private ScaledBetaDistributionSurvivalFunction scaledbetasf;
  private double lambda;

  public CompoundPoissonScaledBetaDistributionOEP(double a, double b, double s, double l)
      throws InvalidParameterException {
    this.scaledbetasf = new ScaledBetaDistributionSurvivalFunction(a, b, s);
    this.lambda = l;
  }

  public CompoundPoissonScaledBetaDistributionOEP(
      ScaledBetaDistributionSurvivalFunction sf, double l) {
    this.scaledbetasf = sf;
    this.lambda = l;
  }

  public double logOCP(double x) {
    return -1.0 * this.lambda * this.scaledbetasf.value(x);
  }

  public DerivativeStructure logOCP(DerivativeStructure t) {
    return this.scaledbetasf.value(t).multiply(-1.0 * this.lambda);
  }

  public double value(double x) {
    return 1.0 - Math.exp(-1.0 * this.lambda * this.scaledbetasf.value(x));
  }

  public DerivativeStructure value(DerivativeStructure t) {
    return this.scaledbetasf.value(t).multiply(-1.0 * this.lambda).exp().subtract(1.0).negate();
  }
}
