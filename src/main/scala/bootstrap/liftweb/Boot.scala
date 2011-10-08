package bootstrap.liftweb

import net.liftweb._
import util._
import Helpers._
import common._
import http._
import com.bff.controller.RootController
import net.liftweb.db.StandardDBVendor
import net.liftweb.db.DB
import net.liftweb.db.DefaultConnectionIdentifier
import net.liftweb.squerylrecord.SquerylRecord
import org.squeryl.adapters.H2Adapter
import _root_.net.liftweb.http.LiftRules
import _root_.java.util.Locale
import _root_.javax.mail._
import _root_.javax.mail.internet._

import _root_.net.liftweb.widgets.autocomplete._
import _root_.net.liftweb.util._
import _root_.net.liftweb.sitemap._
import net.liftweb.http.provider.HTTPRequest

import com.mchange.v2.c3p0.ComboPooledDataSource

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {

    val cpds = new ComboPooledDataSource
    cpds.setDriverClass(Props.get("db.driver") openOr
      "org.h2.Driver")
    cpds.setJdbcUrl(Props.get("db.url") openOr "jdbc:h2:tcp:// localhost//usr/dev/data/portfolio-manager")
    cpds.setUser(Props.get("db.user") openOr "sa")
    cpds.setPassword(Props.get("db.password") openOr "")

    DBHelper.initSquerylRecordWithInMemoryDB()
    LiftRules.unloadHooks.append(() => cpds.close()) 

    DBHelper.createTestSchema()
    

    // where to search snippet
    LiftRules.addToPackages("com.bff")

    //for auto complete
    AutoComplete.init()

    //Custom 404 error page   
    LiftRules.uriNotFound.prepend(NamedPF("404handler") {
      case (req, failure) =>
        NotFoundAsTemplate(ParsePath(List("404"), "html", false, false))
    })

    LiftRules.determineContentType = {
      case (Full(Req("location" :: Nil, _,
        GetRequest)), Full(accept)) => "text/html; charset=utf-8"
      case x => "text/html; charset=utf-8"
    }

    //allows to create custom ajax calls that need to get to the server
    LiftRules.liftRequest.append({
      case r if (r.path.partPath match {
        case "ajax" :: _ => true case _ => false
      }) => false
    })

    // the mail configurations
    configMailer("smtp.gmail.com", "dmshoutmail@gmail.com", ")(87POiuy")

    // Use jQuery 1.4
    LiftRules.jsArtifacts = net.liftweb.http.js.jquery.JQuery14Artifacts

    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    LiftRules.dispatch.append(RootController)
  }

  /**
   * Force the request to be UTF-8
   */
  private def makeUtf8(req: HTTPRequest) {
    req.setCharacterEncoding("UTF-8")
  }

  private def configMailer(host: String, user: String, password: String) {
    // Enable TLS support
    System.setProperty("mail.smtp.starttls.enable", "true");
    // Set the host name
    System.setProperty("mail.smtp.host", host) // Enable authentication
    System.setProperty("mail.smtp.auth", "true") // Provide a means for authentication. Pass it a Can, which can either be Full or Empty
    Mailer.authenticator = Full(new Authenticator {
      override def getPasswordAuthentication = new PasswordAuthentication(user, password)
    })
  }

}

/**
 * 	Singleton object for handling all the menus add
 * 	to the site map
 */
object MenuInfo extends Logger {

  /**
   * defines the site map
   */
  def siteMap() = SiteMap(
    Menu.i("Home") / "index",
    Menu.i("Strain") / "car" / "list",
    Menu.i("Strain-edit") / "car" / "edit",
    
    Menu("error", S ? "Error") / "error")
}

import net.liftweb.http.rest._

object MyCSSMorpher extends RestHelper {
  serve {
    case r @ Req("dynocss" :: file :: _, "css", GetRequest) =>
      for {
        convertFunc <- findConvertFunc(r)
        fileContents <- readFile(file + ".css")
        converted <- convertFunc(fileContents)
      } yield CSSResponse(converted)
  }

  // based on the browser detected, return a function 
  // that will convert HTML5 css into CSS for that browser
  def findConvertFunc(req: Req): Box[String => Box[String]] =
    Empty

  // load the file from the specific location...
  // are you going put the CSS templates in
  // resources, etc.
  def readFile(name: String): Box[String] = Empty

}
