package com.bff.model

import net.liftweb.record.{MetaRecord, Record}
import net.liftweb.squerylrecord.KeyedRecord
import net.liftweb.squerylrecord.RecordTypeMode._
import org.squeryl.Query
import org.squeryl.annotations.Column
import net.liftweb.record.field._

trait CarType extends Enumeration {
  
  type CarType = Value
  
  val Sedan = Value(1, "Sedan")
  val Coupe = Value(2, "Coupe")
  val SUV = Value(3, "Sports Utility Vehicle")
}

object CarType extends CarType 
