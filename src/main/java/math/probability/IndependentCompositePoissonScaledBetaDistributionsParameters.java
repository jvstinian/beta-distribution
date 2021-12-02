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

public class IndependentCompositePoissonScaledBetaDistributionsParameters {
  protected CompositePoissonScaledBetaDistributionParameters[] distparams;

  public IndependentCompositePoissonScaledBetaDistributionsParameters(
      CompositePoissonScaledBetaDistributionParameters[] params) {
    this.distparams = params;
  }

  public double[] getPoissonRates() {
    double[] result = new double[this.distparams.length];
    for (int i = 0; i < result.length; i++) {
      result[i] = this.distparams[i].getPoissonRate();
    }
    return result;
  }

  public ScaledBetaDistributionParameters[] getScaledBetaDistributionParameters() {
    ScaledBetaDistributionParameters[] result =
        new ScaledBetaDistributionParameters[this.distparams.length];
    for (int i = 0; i < result.length; i++) {
      result[i] = (ScaledBetaDistributionParameters) this.distparams[i];
    }
    return result;
  }
}
