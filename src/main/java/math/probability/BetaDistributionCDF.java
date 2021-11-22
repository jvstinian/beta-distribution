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

public class BetaDistributionCDF implements UnivariateDifferentiableFunction {
  private BetaDistributionPDF pdf;
  private double alpha; // TODO: Remove
  private double beta;

  public BetaDistributionCDF(double a, double b) throws InvalidParameterException {
    this.pdf = new BetaDistributionPDF(a, b);
    this.alpha = a;
    this.beta = b;
  }

  public double value(double x) {
    return Beta.regularizedBeta(x, this.alpha, this.beta);
  }

  public DerivativeStructure value(DerivativeStructure t) {
    final double x = t.getValue();
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
