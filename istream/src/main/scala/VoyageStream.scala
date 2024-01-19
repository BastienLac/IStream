import zio._
import zio.stream.ZStream
import com.github.tototoshi.csv._
import scala.collection.mutable.ListBuffer
import HttpStream.calculateDistance
import zio.http._
import zio.stream._
import zio.Duration._
import zio.json._
import scala.compiletime.ops.string
import zio.json.ast.JsonCursor
import javax.print.attribute.standard.Destination

object VoyageStream extends ZIOAppDefault {
  val voyages = new ListBuffer[Voyage]()
  override val run: ZIO[Any, Any, Unit] =
    var appLogic = for {
      url <- ZIO.succeed(getClass().getClassLoader().getResource("voyage.csv"))
      source <- ZIO.succeed(CSVReader.open(url.getFile()))
      stream <- ZStream
        .fromIterator[Seq[String]](source.iterator)
        .take(10)
        .map[Option[Voyage]](line =>
          line match
            case line if line.head == "voyage" => None
            case line =>
              Some(
                Voyage(
                  voyage = line.head.toInt,
                  depart = line.tail.head,
                  arrivee = line.tail.tail.head,
                )
              )
        )
        .collectSome[Voyage]
        .foreach{
          elem =>
            voyages += elem
            Console.printLine("Voyage numéro " + elem.voyage + ", depuis " + elem.depart + ", vers " + elem.arrivee)
        }
      _ <- ZIO.succeed(source.close())
      _ <- Console.print("Veuillez choisir le numéro de votre voyage: ")
      num <- Console.readLine
      voyage = voyages.toList.filter(x => (x.voyage == num.toInt)).head
      _ <- Console.printLine("Vous avez choisi le voyage " + voyage.depart + " -> " + voyage.arrivee)
      // AJOUTER API ICI
      _ <- calculateDistance(voyage.depart, voyage.arrivee)
    } yield ()

    appLogic.provide(Client.default, Scope.default)
}
