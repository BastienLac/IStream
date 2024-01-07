import zio._
import zio.http._
import zio.stream._
import zio.Duration._
import zio.json._
import scala.compiletime.ops.string
import zio.json.ast.JsonCursor
import javax.print.attribute.standard.Destination

object HttpStream extends ZIOAppDefault {
  case class Distance(text: String, value: Int)
  case class Duration(text: String, value: Int)
  case class Element(distance: Distance, duration: Duration, status: String)
  case class Row(elements: List[Element])
  case class DistanceResponse(destination_addresses: List[String], origin_addresses: List[String], rows: List[Row], status: String)

  object Distance {
    implicit val distanceDecoder: JsonDecoder[Distance] = DeriveJsonDecoder.gen[Distance]
  }

  object Duration {
    implicit val durationDecoder: JsonDecoder[Duration] = DeriveJsonDecoder.gen[Duration]
  }

  object Element {
    implicit val elementDecoder: JsonDecoder[Element] = DeriveJsonDecoder.gen[Element]
  }

  object Row {
    implicit val rowDecoder: JsonDecoder[Row] = DeriveJsonDecoder.gen[Row]
  }

  object DistanceResponse {
    implicit val distanceResponseDecoder: JsonDecoder[DistanceResponse] = DeriveJsonDecoder.gen[DistanceResponse]
  }

  def fetchData() = {
    val url = URL
      .decode(
        "https://maps.googleapis.com/maps/api/distancematrix/json?origins=Paris&destinations=Lyon&key=AIzaSyB9UK97M3nPsMismYLKUSArWMFa80Ry_cw"
      )
      .toOption
      .get

    for {
      client <- ZIO.service[Client]
      res <- client.url(url).get("")
    } yield res
  }

  override def run: ZIO[Any, Any, Unit] =
    val appLogic = for {
      _ <- ZStream(fetchData())
        .mapZIO { z =>
          for {
            res <- z
            body <- res.body.asString
            resultOf = body.fromJson[DistanceResponse]
            
            // Traitement des erreurs de désérialisation
            distancesTexts <- ZIO.fromEither(resultOf.fold(
              error => Left(error),
              success => Right(success.rows.flatMap(_.elements.map(_.distance.text)))
            ))

            // Afficher le texte de chaque instance de Distance
            _ <- ZIO.foreach(distancesTexts)(text => Console.printLine(s"Distance Text: $text"))
          } yield resultOf
        }
        .foreach(Console.printLine(_))
    } yield ()

    appLogic.provide(Client.default, Scope.default)
}
