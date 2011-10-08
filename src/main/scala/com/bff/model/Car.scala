package com.bff.model

import net.liftweb.record.{MetaRecord, Record}
import net.liftweb.squerylrecord.KeyedRecord
import net.liftweb.squerylrecord.RecordTypeMode._
import org.squeryl.Query
import org.squeryl.annotations.Column
import net.liftweb.record.field._
import org.squeryl.dsl.ManyToOne
import org.squeryl.dsl.OneToMany

/**
 * The Car entity object
 */
class Car extends Record[Car] with KeyedRecord[Long] {
  
  override def meta = Car
  
  @Column(name="id")
  override val idField = new LongField(this)
  
  val name = new StringField(this, "")
  
  val description = new OptionalTextareaField(this, 1000)
  
  val maker = new StringField(this, "")
  
  val created = new DateTimeField(this)
  
  val kind = new EnumField[Car,CarType](this, CarType) 
  
}

object Car extends Car with MetaRecord[Car]

