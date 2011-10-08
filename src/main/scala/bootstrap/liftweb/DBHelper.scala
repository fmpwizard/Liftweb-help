package bootstrap.liftweb

import net.liftweb.squerylrecord.RecordTypeMode._
import net.liftweb.record.field._
import net.liftweb.record.Field
import org.squeryl.{ SessionFactory, Session, Schema }
import org.squeryl.adapters.H2Adapter
import org.squeryl.annotations.Column
import org.squeryl.internals.AutoIncremented
import org.squeryl.internals.PrimaryKey
import org.squeryl.dsl.CompositeKey2
import org.squeryl.KeyedEntity
import java.sql.DriverManager
import net.liftweb.squerylrecord.SquerylRecord
import com.bff.model.CarDB

object DBHelper {
  def initSquerylRecordWithInMemoryDB() {
    SquerylRecord.initWithSquerylSession { 
      val session = Session.create(DriverManager.getConnection("jdbc:h2:mem:testSquerylRecordDB;DB_CLOSE_DELAY=-1", "sa", ""), new H2Adapter)
      //session.setLogger(statement => println(statement))
      session
    }
  }

  /**
   * Creates the test schema in a new transaction. Drops an old schema if
   * it exists.
   */
  def createTestSchema() {
    inTransaction {
    	try {
	      CarDB.printDdl 
	      CarDB.dropAndCreate
	      CarDB.createTestData
    	} catch {
    		case e => e.printStackTrace()
    		  throw e;
    	}
    }
  }
}