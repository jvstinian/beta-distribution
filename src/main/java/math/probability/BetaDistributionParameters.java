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

public class BetaDistributionParameters {
  protected double alpha;
  protected double beta;

  public BetaDistributionParameters(double a, double b) throws InvalidParameterException {
    if (a <= 0.0) {
      throw new InvalidParameterException("BetaDistributionPDF", "a", "a>0", a);
    }
    if (b <= 0.0) {
      throw new InvalidParameterException("BetaDistributionPDF", "b", "b>0", b);
    }
    this.alpha = a;
    this.beta = b;
  }

  public BetaDistributionParameters(BetaDistributionParameters params) {
    this.alpha = params.alpha;
    this.beta = params.beta;
  }

  public double getAlpha() {
    return this.alpha;
  }

  public double getBeta() {
    return this.beta;
  }
};
