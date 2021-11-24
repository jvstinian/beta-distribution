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

import com.jvstinian.math.InvalidParameterException;
import com.jvstinian.math.probability.CompoundPoissonScaledBetaDistributionParameters;

public class RMSEventLoss {
  private int eventId;
  private double rate;
  private double mean;
  private double sdi;
  private double sdc;
  private double exposure;

  public RMSEventLoss(
      int _eventId, double _rate, double _mean, double _sdi, double _sdc, double _exposure) {
    this.eventId = _eventId;
    this.rate = _rate;
    this.mean = _mean;
    this.sdi = _sdi;
    this.sdc = _sdc;
    this.exposure = _exposure;
  }

  private double getA() {
    double ret =
        Math.pow(this.mean / (this.sdi + this.sdc), 2.0) * (1.0 - (this.mean / this.exposure))
            - (this.mean / this.exposure);
    return ret;
  }

  private double getB() {
    return this.getA() * ((this.exposure / this.mean) - 1.0);
  }

  public double getRate() {
    return this.rate;
  }
  
  public CompoundPoissonScaledBetaDistributionParameters getDistributionParameters()
      throws InvalidParameterException {
    return new CompoundPoissonScaledBetaDistributionParameters(this.getA(), this.getB(), this.exposure, this.rate);
  }
}
