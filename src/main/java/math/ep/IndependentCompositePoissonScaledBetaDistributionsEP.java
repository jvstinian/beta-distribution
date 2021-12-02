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

import com.jvstinian.math.probability.CompositePoissonScaledBetaDistributionParameters;
import com.jvstinian.math.probability.ScaledBetaDistribution;
import com.jvstinian.math.probability.ScaledBetaDistributionParameters;
import com.jvstinian.math.probability.IndependentCompositePoissonScaledBetaDistributionsParameters;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;
import org.apache.commons.math3.analysis.solvers.BisectionSolver;
import org.apache.commons.math3.exception.DimensionMismatchException;

public class IndependentCompositePoissonScaledBetaDistributionsEP
    extends IndependentCompositePoissonScaledBetaDistributionsParameters {
  public IndependentCompositePoissonScaledBetaDistributionsEP(
      CompositePoissonScaledBetaDistributionParameters[] params) {
    super(params);
  }

  public static class OEP implements UnivariateDifferentiableFunction {
    private ScaledBetaDistribution.SurvivalFunction[] sfs;
    private double[] lambdas;

    public OEP(ScaledBetaDistribution.SurvivalFunction[] fs, double[] ls)
        throws DimensionMismatchException {
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
  };

  public OEP getOEPFunction() {
    double[] lambdas = this.getPoissonRates();
    ScaledBetaDistributionParameters[] sbparams = this.getScaledBetaDistributionParameters();
    ScaledBetaDistribution.SurvivalFunction[] sfs =
        new ScaledBetaDistribution.SurvivalFunction[sbparams.length];
    for (int i = 0; i < sfs.length; i++) {
      ScaledBetaDistribution temp_scd = new ScaledBetaDistribution(sbparams[i]);
      sfs[i] = temp_scd.getSurvivalFunction();
    }
    return new OEP(sfs, lambdas);
  }

  private static class OPMLObjective implements UnivariateDifferentiableFunction {
    private OEP oep;
    private double prob;

    public OPMLObjective(OEP p, double q) {
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

  public double getMaxOccurrenceValue() {
    double ret = 0.0;
    ScaledBetaDistributionParameters[] sbparams = this.getScaledBetaDistributionParameters();
    for (int i = 0; i < sbparams.length; i++) {
      ret = Math.max(sbparams[i].getScale(), ret);
    }
    return ret;
  }

  public double[] calculateOccurrencePML(double[] qs) {
    OEP oep = this.getOEPFunction();

    double oepAt0 = oep.value(0.0);
    double maxValue = this.getMaxOccurrenceValue();
    BisectionSolver solver = new BisectionSolver();
    double[] ret = new double[qs.length];
    for (int i = 0; i < qs.length; i++) {
      if (qs[i] >= oepAt0) {
        ret[i] = 0.0;
      } else {
        OPMLObjective obj = new OPMLObjective(oep, qs[i]);
        ret[i] = solver.solve(10000, obj, 0.0, maxValue);
      }
    }
    return ret;
  }

  public double[] calculateOccurrencePML(ReturnPeriod[] rps) {
    double[] qs = new double[rps.length];
    for (int i = 0; i < rps.length; i++) {
      qs[i] = rps[i].getProbability();
    }
    return this.calculateOccurrencePML(qs);
  }
}
