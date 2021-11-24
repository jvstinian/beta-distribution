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

public class CompoundPoissonScaledBetaDistributionParameters
    extends ScaledBetaDistributionParameters {
  protected double lambda;

  public CompoundPoissonScaledBetaDistributionParameters(double a, double b, double s, double l)
      throws InvalidParameterException {
    super(a, b, s);
    this.lambda = l;
  }

  public CompoundPoissonScaledBetaDistributionParameters(
      BetaDistributionParameters params, double s, double l) {
    super(params, s);
    this.lambda = l;
  }

  public double getPoissonRate() {
    return this.lambda;
  }
}
