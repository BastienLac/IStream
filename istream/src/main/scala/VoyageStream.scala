import zio._
import zio.http._
import zio.stream.ZStream
import com.github.tototoshi.csv._
import scala.collection.mutable.ListBuffer
import DistanceStream.fetchData

object VoyageStream extends ZIOAppDefault {
  val voyages = new ListBuffer[Voyage]()

  // renvoie un voyage s'il existe sinon None
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
            // Affiche chaque voyage
            Console.printLine("Voyage numéro " + elem.voyage + ", depuis " + elem.depart + ", vers " + elem.arrivee)
        }
      _ <- ZIO.succeed(source.close())

      _ <- Console.print("Veuillez choisir le numéro de votre voyage: ")

      // Vérifie que l'entrée utilisateur est valide. Si ce n'est pas un int renvoie une erreur de type RuntimeException
      num <- Console.readLine.flatMap(s => ZIO.fromOption(s.toIntOption).orElseFail(new RuntimeException("Entrée utilisateur doit être un numéro.")))
      
      voyageOpt <- fetchVoyage(num)
      _ <- voyageOpt match {
        case Some(voyage) =>
          Console.printLine(s"Vous avez choisi le voyage ${voyage.depart} -> ${voyage.arrivee}") *>
            fetchData(voyage.depart, voyage.arrivee)
        case None =>
          Console.printLine("Aucun voyage trouvé pour le numéro spécifié.")
      }

    } yield ()

    appLogic.provide(Client.default, Scope.default)
}
