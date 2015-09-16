/*
 * Copyright 2015 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.ct.computations

import uk.gov.hmrc.ct.box.{Calculated, CtBoxIdentifier, CtOptionalInteger}
import uk.gov.hmrc.ct.computations.calculations.LowEmissionCarsCalculator
import uk.gov.hmrc.ct.computations.retriever.ComputationsBoxRetriever

case class CP670(value: Option[Int]) extends CtBoxIdentifier(name = "Special rate pool balancing charge") with CtOptionalInteger

object CP670 extends Calculated[CP670, ComputationsBoxRetriever] with LowEmissionCarsCalculator {

  override def calculate(fieldValueRetriever: ComputationsBoxRetriever): CP670 = {

    calculateSpecialRatePoolBalancingCharge(
      fieldValueRetriever.retrieveLEC01(),
      fieldValueRetriever.retrieveCPQ8(),
      fieldValueRetriever.retrieveCP666(),
      fieldValueRetriever.retrieveCP667()

    )

  }

}
