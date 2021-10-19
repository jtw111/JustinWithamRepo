package example

import org.mongodb.scala._
import example.Helpers._
import scala.io.Source
import java.io.FileNotFoundException

object Proj0 {
    def main(args: Array[String]) : Unit = {
        try {
          val stringDocs = Source.fromResource("cars.json").getLines.toList
          val bsonDocs = stringDocs.map(doc => Document(doc))

          val client: MongoClient = MongoClient()
          val database: MongoDatabase = client.getDatabase("project0")
          val collection: MongoCollection[Document] = database.getCollection("cars")
          collection.insertMany(bsonDocs).printResults()

          val insertObservable = collection.insertMany(bsonDocs)
          val insertAndCount = for {
          insertResult <- insertObservable
          countResult <- collection.countDocuments()
        } yield countResult

          client.close()

        } catch {
           case fNotFound: FileNotFoundException => {
             println("The specified file could not be found, so nothing happened.")
           }
        }
    }
}