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

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;

public class SumOfCompoundPoissonScaledBetaDistributionOPMLObjective
    implements UnivariateDifferentiableFunction {
  private SumOfCompoundPoissonScaledBetaDistributionOEP oep;
  private double prob;

  public SumOfCompoundPoissonScaledBetaDistributionOPMLObjective(
      SumOfCompoundPoissonScaledBetaDistributionOEP p, double q) {
    this.oep = p;
    this.prob = q;
  }

  public double value(double x) {
    return this.oep.logOCP(x) - Math.log(1.0 - this.prob);
  }

  public DerivativeStructure value(DerivativeStructure t) {
    return this.oep.logOCP(t).subtract(Math.log(1.0 - this.prob));
  }
}
