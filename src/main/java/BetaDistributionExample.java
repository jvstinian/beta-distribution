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

package main.java;

import com.jvstinian.math.InvalidParameterException;
import com.jvstinian.math.ep.RMSEventLoss;
import com.jvstinian.math.probability.CompoundPoissonScaledBetaDistributionParameters;
import com.jvstinian.math.ep.SumOfCompoundPoissonScaledBetaDistributionEP;
import com.jvstinian.math.probability.BetaDistributionCDF;
import com.jvstinian.math.probability.BetaDistributionQuantileObjective;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.solvers.BisectionSolver;

public class BetaDistributionExample {
  /**
   * Main entry point
   *
   * @param args
   */
  public static void main(String[] args) {
    try {
      BetaDistributionCDF cdf = new BetaDistributionCDF(0.5, 0.5);
      DerivativeStructure ds = new DerivativeStructure(1, 2, 0, 0.25);
      double[] derivs = cdf.value(ds).getAllDerivatives();
      System.out.println("All derivatives of Beta Distribution CDF with alpha=0.5, beta=0.5");
      for (int i = 0; i < derivs.length; i++) {
        System.out.println(String.valueOf(i) + ": " + String.valueOf(derivs[i]));
      }

    } catch (InvalidParameterException e) {
      System.err.println(e);
    }

    try {
      double[] probs = new double[] {0.01, 0.05, 0.1, 0.25, 0.5, 0.75, 0.9, 0.95, 0.99};
      for (int i = 0; i < probs.length; i++) {
        double prob = probs[i];
        BisectionSolver solver = new BisectionSolver();
        BetaDistributionQuantileObjective betaqobj =
            new BetaDistributionQuantileObjective(0.5, 0.5, prob);
        double quantile = solver.solve(1000000, betaqobj, 0.0, 1.0);
        System.out.println(
            "Quantile for probability=" + String.valueOf(prob) + ": " + String.valueOf(quantile));
      }
    } catch (InvalidParameterException e) {
      System.err.println(e);
    }
    
    try {
      System.out.println("Calculating the Occurrence PML using the new class");
      double[] probs = new double[] {0.01, 0.05, 0.1, 0.25, 0.5, 0.75, 0.9, 0.95, 0.99};
      // Calculating OEP
      RMSEventLoss[] elt =
          new RMSEventLoss[] {
            new RMSEventLoss(1, 0.1, 500.0, 500.0, 500.0, 10000.0),
            new RMSEventLoss(2, 0.1, 300.0, 400.0, 600.0, 5000.0),
            new RMSEventLoss(3, 0.5, 200.0, 300.0, 400.0, 4000.0)
          };
      CompoundPoissonScaledBetaDistributionParameters[] distparams =
          new CompoundPoissonScaledBetaDistributionParameters[elt.length];
      for (int idx = 0; idx < elt.length; idx++) {
        distparams[idx] = elt[idx].getDistributionParameters();
      }
      SumOfCompoundPoissonScaledBetaDistributionEP ep = new SumOfCompoundPoissonScaledBetaDistributionEP(distparams);
      double[] opmls = ep.calculateOccurrencePML(probs);
      for (int idx = 0; idx < probs.length; idx++) {
        System.out.println(
            "Occurrence PML for probability="
                + String.valueOf(probs[idx])
                + ": "
                + String.valueOf(opmls[idx]));
      }


    } catch (InvalidParameterException e) {
      System.err.println(e);
    }
  }
}
