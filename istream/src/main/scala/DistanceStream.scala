import zio._
import zio.http._
import zio.stream._
import zio.Duration._
import zio.json._
import scala.compiletime.ops.string
import zio.json.ast.JsonCursor
import javax.print.attribute.standard.Destination

object HttpStream extends ZIOAppDefault {
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
