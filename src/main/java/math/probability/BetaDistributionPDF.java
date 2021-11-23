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
import org.apache.commons.math3.special.Beta;

public class BetaDistributionPDF implements UnivariateDifferentiableFunction {
  private double alpha;
  private double beta;

  public BetaDistributionPDF(double a, double b) throws InvalidParameterException {
    if (a <= 0.0) {
      throw new InvalidParameterException("BetaDistributionPDF", "a", "a>0", a);
    }
    if (b <= 0.0) {
      throw new InvalidParameterException("BetaDistributionPDF", "b", "b>0", b);
    }
    this.alpha = a;
    this.beta = b;
  }

  public double getAlpha() {
    return this.alpha;
  }

  public double getBeta() {
    return this.beta;
  }

  public double value(double x) {
    DerivativeStructure c = new DerivativeStructure(1, 0, x);
    return this.value(c).getValue();
  }

  public DerivativeStructure value(DerivativeStructure t) {
    DerivativeStructure x1 = t.pow(this.alpha - 1.0); // x^(\alpha - 1}
    DerivativeStructure x2 = t.subtract(1.0).negate().pow(this.beta - 1.0); // (1-x)^{\beta - 1}
    DerivativeStructure denom = t.createConstant(Math.exp(Beta.logBeta(this.alpha, this.beta)));
    return x1.multiply(x2).divide(denom);
  }
};
