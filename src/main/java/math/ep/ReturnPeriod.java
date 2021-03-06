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

public class ReturnPeriod {
  private double rp;

  public ReturnPeriod(double returnPeriod) throws InvalidParameterException {
    if (returnPeriod <= 0.0) {
      throw new InvalidParameterException(
          "ReturnPeriod", "returnPeriod", "returnPeriod>0", returnPeriod);
    }
    this.rp = returnPeriod;
  }

  public double getReturnPeriod() {
    return this.rp;
  }

  public double getProbability() {
    return 1.0 / this.rp;
  }
}
