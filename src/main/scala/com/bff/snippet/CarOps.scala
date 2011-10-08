package com.bff.snippet

import net.liftweb.http.js.jquery.JqJsCmds._
import net.liftweb.common._
import Logger._
import net.liftweb.http.RequestVar
import net.liftweb.http.js.JE.JsRaw
import net.liftweb.http.SHtml
import net.liftweb.util._
import Helpers._
import net.liftweb.http.js._
import JsCmds._
import net.liftweb.squerylrecord.RecordTypeMode._

import com.bff.model.CarDB._
import com.bff.model.Car
import com.bff.model.CarType

import scala.xml.Text


class CarOps extends Logger {

  var chosenKind: Box[CarType.Value] = Empty

  var daysToBloom: Box[String] = Empty

  private object selectCar extends RequestVar[Box[Car]](Empty)

  def render = {

    "#kind" #> SHtml.selectObj[CarType.Value](
      CarType.values.toList.map(v => (v, v.toString)),
      Empty,
      selected => chosenKind = Full(selected)) &
      "#createCar" #> <button id="create-car" onclick="createCar()">Create new car</button>

  }

  def list = {

    transaction {
      val allCars = from(cars)(c => select(c)).toList
      
      def deleteCar(name: String, id: Long) = {
        JsRaw("deleteCar('" + name + "'," + id.toString() + ")")
      }

      def updateCar(c: Car)(value:String)={
        println("**********")
        SetHtml("car-" + c.id.toString(), <span>new car name</span>)
      }

      ".car *" #> allCars.map(c =>
        ".name *" #> SHtml.swappable(<span id={"car-" + c.id} >{c.name}</span>, SHtml.ajaxText(c.name.is, updateCar(c) _)) &
          ".kind *" #> c.kind &
          ".maker *" #> c.maker &
          ".description *" #> c.description.is &
          ".edit [href] " #> SHtml.link("/edit", () => selectCar(Full(c)), Text("edit")) &
          ".delete" #> <a class="delete" href="javascript://" onclick={ deleteCar(c.name.is, c.id).toJsCmd }>delete</a>)
    }
  }
}