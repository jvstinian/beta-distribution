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
import com.jvstinian.math.ep.ReturnPeriod;
import com.jvstinian.math.ep.IndependentCompositePoissonScaledBetaDistributionsEP;
import com.jvstinian.math.probability.BetaDistribution;
import com.jvstinian.math.probability.CompositePoissonScaledBetaDistributionParameters;

public class BetaDistributionExample {
  /**
   * Main entry point
   *
   * @param args
   */
  public static void main(String[] args) {
    try {
      // Calculate OEP
      System.out.println("==================================================");
      System.out.println("Calculating the Occurrence PML for return periods ");
      System.out.println("==================================================");
      System.out.println("Return Period    Occurrence PML");
      System.out.println("=============    ==============");
      ReturnPeriod[] rps =
          new ReturnPeriod[] {
            new ReturnPeriod(10.0),
            new ReturnPeriod(20.0),
            new ReturnPeriod(50.0),
            new ReturnPeriod(100.0),
            new ReturnPeriod(200.0),
            new ReturnPeriod(250.0),
            new ReturnPeriod(500.0),
            new ReturnPeriod(1000.0)
          };
      RMSEventLoss[] elt =
          new RMSEventLoss[] {
            new RMSEventLoss(1, 0.1, 500.0, 500.0, 500.0, 10000.0),
            new RMSEventLoss(2, 0.1, 300.0, 400.0, 600.0, 5000.0),
            new RMSEventLoss(3, 0.5, 200.0, 300.0, 400.0, 4000.0)
          };
      CompositePoissonScaledBetaDistributionParameters[] distparams =
          new CompositePoissonScaledBetaDistributionParameters[elt.length];
      for (int idx = 0; idx < elt.length; idx++) {
        distparams[idx] = elt[idx].getDistributionParameters();
      }
      IndependentCompositePoissonScaledBetaDistributionsEP ep =
          new IndependentCompositePoissonScaledBetaDistributionsEP(distparams);
      double[] opmls = ep.calculateOccurrencePML(rps);
      for (int idx = 0; idx < rps.length; idx++) {
        System.out.print(String.format("%13s", String.valueOf(rps[idx].getReturnPeriod())));
        System.out.print("    ");
        System.out.println(String.format("%14s", String.format("%.6f", opmls[idx])));
      }
    } catch (InvalidParameterException e) {
      System.err.println(e);
    }
  }
}
