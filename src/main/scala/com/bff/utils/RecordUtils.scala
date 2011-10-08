package com.bff.utils

import java.util.Calendar
import net.liftweb.record.field.DateTimeField
import net.liftweb.common.Logger
import net.liftweb.squerylrecord.RecordTypeMode._
import com.bff.model.CarDB._
import com.bff.model.Car
import java.text.SimpleDateFormat

object RecordUtils extends Logger {

  
  def lookUpCar(id: Long): Car = {
    transaction {
      debug("Looking up strain with id: " + id)
      var car = cars.lookup(id)
      car.get
    }
  }

  def formatDate(date: Calendar) = {
    var value = ""
    val sdf = new SimpleDateFormat("MM.dd.yyyy")
    sdf.format(date.getTime())

  }
}