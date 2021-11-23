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

import org.apache.commons.math3.analysis.solvers.BisectionSolver;

public class SumOfCompoundPoissonScaledBetaDistributionOPMLCalculator {
  private SumOfCompoundPoissonScaledBetaDistributionOEP oep;

  public SumOfCompoundPoissonScaledBetaDistributionOPMLCalculator(
      SumOfCompoundPoissonScaledBetaDistributionOEP p) {
    this.oep = p;
  }

  public double[] calculateOccurrencePML(double[] qs) {
    double oepAt0 = this.oep.value(0.0);
    double maxValue = this.oep.getMaxValue();
    BisectionSolver solver = new BisectionSolver();
    double[] ret = new double[qs.length];
    for (int i = 0; i < qs.length; i++) {
      if (qs[i] >= oepAt0) {
        ret[i] = 0.0;
      } else {
        SumOfCompoundPoissonScaledBetaDistributionOPMLObjective obj =
            new SumOfCompoundPoissonScaledBetaDistributionOPMLObjective(this.oep, qs[i]);
        ret[i] = solver.solve(10000, obj, 0.0, maxValue);
      }
    }
    return ret;
  }
}
