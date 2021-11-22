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

import com.jvstinian.math.probability.ScaledBetaDistributionSurvivalFunction;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;

public class SumOfCompoundPoissonScaledBetaDistributionOEP
    implements UnivariateDifferentiableFunction {
  private ScaledBetaDistributionSurvivalFunction[] sfs;
  private double[] lambdas;

  public SumOfCompoundPoissonScaledBetaDistributionOEP(
      ScaledBetaDistributionSurvivalFunction[] fs, double[] ls) throws DimensionMismatchException {
    if (fs.length != ls.length) {
      // check that the lambda and survival function arrays are of the same length
      throw new DimensionMismatchException(fs.length, ls.length);
    }
    this.sfs = fs;
    this.lambdas = ls;
  }

  public double logOCP(double x) {
    double ret = 0;
    for (int i = 0; i < this.lambdas.length; i++) {
      ret += -1.0 * this.lambdas[i] * this.sfs[i].value(x);
    }
    return ret;
  }

  public DerivativeStructure logOCP(DerivativeStructure t) {
    DerivativeStructure[] dss = new DerivativeStructure[this.sfs.length];
    for (int i = 0; i < dss.length; i++) {
      dss[i] = this.sfs[i].value(t);
    }
    return t.linearCombination(this.lambdas, dss).negate();
  }

  public double value(double x) {
    return 1.0 - Math.exp(this.logOCP(x));
  }

  public DerivativeStructure value(DerivativeStructure t) {
    return this.logOCP(t).exp().subtract(1.0).negate();
  }

  public double getMaxValue() {
    double ret = 0.0;
    for (int i = 0; i < this.sfs.length; i++) {
      ret = Math.max(sfs[i].getMaxValue(), ret);
    }
    return ret;
  }
}
