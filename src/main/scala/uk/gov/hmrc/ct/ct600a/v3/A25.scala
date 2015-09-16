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

package uk.gov.hmrc.ct.ct600a.v3

import org.joda.time.LocalDate
import uk.gov.hmrc.ct.box.{CtBoxIdentifier, CtValue}
import uk.gov.hmrc.ct.ct600a.v3.formats.Loans


case class WriteOff(loanId: String, amountWrittenOff: Int, dateWrittenOff: LocalDate, endDateOfWriteOffAP : Option[LocalDate] = None) {

  def isReliefEarlierThanDue(acctPeriodEnd: LocalDate): Boolean = {
    requireEndDateOfWriteOffApp(acctPeriodEnd)
    val nineMonthsAndADayAfter: LocalDate = acctPeriodEnd.plusMonths(9).plusDays(1)
    dateWrittenOff.isAfter(acctPeriodEnd) && dateWrittenOff.isBefore(nineMonthsAndADayAfter)
  }

  def isLaterReliefNowDue(acctPeriodEnd: LocalDate, filingDate: LPQ07): Boolean = {
    requireEndDateOfWriteOffApp(acctPeriodEnd)
    !isReliefEarlierThanDue(acctPeriodEnd) && isFilingAfterLaterReliefDueDate(filingDate)
  }


  def isLaterReliefNotYetDue(acctPeriodEnd: LocalDate, filingDate: LPQ07): Boolean = {
    requireEndDateOfWriteOffApp(acctPeriodEnd)
    !isReliefEarlierThanDue(acctPeriodEnd) && isFilingBeforeReliefDueDate(filingDate)
  }

  private def isFilingAfterLaterReliefDueDate(filingDateBoxNumber: LPQ07):Boolean = {
    (filingDateBoxNumber.value, endDateOfWriteOffAP) match {
      case (Some(filingDate), Some(endDate)) => {
        val reliefDueDate = endDate.plusMonths(9)
        filingDate.isAfter(reliefDueDate)
      }
      case _ =>  false
    }
  }

  private def isFilingBeforeReliefDueDate(filingDate: LPQ07) =  filingDate.value match {
    case Some(date) => !isFilingAfterLaterReliefDueDate(filingDate)
    case None => true
  }

  private def requireEndDateOfWriteOffApp(acctPeriodEnd: LocalDate) = {
    val message = s"As the write off date [$dateWrittenOff] is more than 9 months after the accounting period end date [$acctPeriodEnd], the end date of the write off accounting period must be provided"
    val requirement:Boolean = if(dateWrittenOff.isAfter(acctPeriodEnd.plusMonths(9)) && endDateOfWriteOffAP.isEmpty) false else true
    require(requirement, message)
  }

}

case class A25(writeOffs: List[WriteOff] = List.empty) extends CtBoxIdentifier(name = "Loan write offs and releases.") with CtValue[List[WriteOff]] {

  override def value = writeOffs

  def +(other: A25): A25 = new A25(writeOffs ++ other.writeOffs)

  override def asBoxString = Loans.asBoxString(this)
}
