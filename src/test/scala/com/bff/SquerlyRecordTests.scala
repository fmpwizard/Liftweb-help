package com.bff

import org.specs.Specification
import org.specs.runner.JUnit4
import org.junit.runner.RunWith
import org.specs.runner.JUnitSuiteRunner
import bootstrap.liftweb.DBHelper
import net.liftweb.squerylrecord.RecordTypeMode._
import net.liftweb.record.{ BaseField, Record }
import com.bff.model.GardenDB.{ TestData => td }
import com.bff.model.GardenDB.{ plants, strains, notices }
import com.bff.model.Plant
import com.bff.model.Strain
import com.bff.model.Notice

/**
 * Class for running the specs tests with JUnit4.
 */
@RunWith(classOf[JUnitSuiteRunner])
class SquerylRecordSpecsTest extends JUnit4(SquerylRecordSpecs)

object SquerylRecordSpecs extends Specification {

  doBeforeSpec {
    DBHelper.initSquerylRecordWithInMemoryDB()
    DBHelper.createTestSchema()
  }

  "SquerylRecord" should {

    "load record by ID" >> {
      transaction {
        val plant = plants.lookup(td.p1.id)
        checkPlantsEqual(plant.get, td.p1)

        val strain = strains.lookup(td.s1.id)
        checkStrainEqual(strain.get, td.s1)

        val notice = notices.lookup(td.n1.id)
        checkNoticeEqual(notice.get, td.n1)
      }
    }
  }

  class TransactionRollbackException extends RuntimeException

  /**
   * Runs the given code in a transaction and rolls
   * back the transaction afterwards.
   */
  private def transactionWithRollback[T](code: => T): T = {
    var result: T = null.asInstanceOf[T]
    try {
      transaction {
        result = code
        throw new TransactionRollbackException()
      }
    } catch {
      case e: TransactionRollbackException => // OK, was rolled back
    }

    result
  }

  private def checkPlantsEqual(p1: Plant, p2: Plant) {
    val cmp = new RecordComparer[Plant](p1, p2)
    cmp.check(_.idField)
    cmp.check(_.harvestDate)
    cmp.check(_.strainId)
    cmp.check(_.created)

    cmp.checkXHtml()
  }

  private def checkStrainEqual(s1: Strain, s2: Strain) {
    val cmp = new RecordComparer[Strain](s1, s2)
    cmp.check(_.name)
    cmp.check(_.created)
    cmp.check(_.daysToFlower)
    cmp.check(_.description)
    cmp.check(_.maker)
  }

  private def checkNoticeEqual(n1: Notice, n2: Notice) {
    val cmp = new RecordComparer[Notice](n1, n2)
    cmp.check(_.description)
    cmp.check(_.created)
    cmp.check(_.discovered)
    cmp.check(_.remedy)
    cmp.check(_.diagnosis)
  }

  class RecordComparer[T <: Record[T]](val r1: T, val r2: T) {
    def check(fieldExtractor: (T) => BaseField) {
      val f1 = fieldExtractor(r1)
      val f2 = fieldExtractor(r2)
      f1.get must_== f2.get
      f1.name must_== f2.name
    }

    def checkXHtml() {
      r1.toXHtml must_== r2.toXHtml
    }
  }
}