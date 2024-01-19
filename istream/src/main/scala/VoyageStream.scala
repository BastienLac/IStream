import zio._
import zio.http._
import zio.stream.ZStream
import com.github.tototoshi.csv._
import scala.collection.mutable.ListBuffer
import DistanceStream.fetchData

object VoyageStream extends ZIOAppDefault {
  val voyages = new ListBuffer[Voyage]()

  // return a trip if it exists or None
  def fetchVoyage(num: Int): ZIO[Any, Nothing, Option[Voyage]] =
    ZIO.succeed(voyages.find(_.voyage == num))

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
            // Display each trip
            Console.printLine("Trip number " + elem.voyage + ", from " + elem.depart + ", to " + elem.arrivee)
        }
      _ <- ZIO.succeed(source.close())

      _ <- Console.print("Please select the trip's number you want: ")

      // Check if the entry is valid. If it's not an int return a RuntimeException
      num <- Console.readLine.flatMap(s => ZIO.fromOption(s.toIntOption).orElseFail(new RuntimeException("The entry must be a number.")))
      
      voyageOpt <- fetchVoyage(num)
      _ <- voyageOpt match {
        case Some(voyage) =>
          Console.printLine(s"You selected the trip ${voyage.depart} -> ${voyage.arrivee}") *>
            fetchData(voyage.depart, voyage.arrivee)
        case None =>
          Console.printLine("No trip correspond to the selected number.")
      }

    } yield ()

    appLogic.provide(Client.default, Scope.default)
}
