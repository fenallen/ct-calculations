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

package uk.gov.hmrc.ct.accounts.retriever

import uk.gov.hmrc.ct.accounts._
import uk.gov.hmrc.ct.box.retriever.{BoxRetriever, BoxValues}

object AccountsBoxRetriever extends BoxValues[AccountsBoxRetriever]

trait AccountsBoxRetriever extends BoxRetriever {
  def retrieveAC1(): AC1
  def retrieveAC3(): AC3
  def retrieveAC4(): AC4

  def retrieveAC12(): AC12

  def retrieveAC205(): AC205
  def retrieveAC206(): AC206
}
