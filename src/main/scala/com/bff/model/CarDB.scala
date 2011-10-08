package com.bff.model

import org.squeryl.Schema
import net.liftweb.squerylrecord.RecordTypeMode._
import java.util.Calendar
import org.squeryl.KeyedEntity
import org.squeryl.ForeignKeyDeclaration
import com.bff.utils.RecordUtils 

object CarDB extends Schema {
  val cars = table[Car]

  // the default constraint for all foreign keys in this schema : 
  override def applyDefaultForeignKeyPolicy(foreignKeyDeclaration: ForeignKeyDeclaration) =
    foreignKeyDeclaration.unConstrainReference()

  /**
   * Drops an old schema if exists and then creates
   * the new schema.
   */
  def dropAndCreate {
    drop
    create
  }

  /**
   * Creates some test instances of cars saves them in the database.
   */
  def createTestData {
    import TestData._

    allCars.foreach(cars.insert(_))
  }

  object TestData {
    
    
    val s1 = Car.createRecord.name("Accord").
      description("Japaneese Car").
      kind(CarType.Sedan).
      maker("Honda").
      created(Calendar.getInstance())
      

      val allCars = List(s1)

  }
}
