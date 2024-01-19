import zio._
import zio.http._
import zio.stream._
import zio.Duration._
import zio.json._
import scala.compiletime.ops.string
import zio.json.ast.JsonCursor
import javax.print.attribute.standard.Destination

object HttpStream extends ZIOAppDefault {
  def fetchData(origin: String, destination: String) = {
    val url = URL
      .decode(
        s"https://maps.googleapis.com/maps/api/distancematrix/json?origins=$origin&destinations=$destination&key=AIzaSyB9UK97M3nPsMismYLKUSArWMFa80Ry_cw"
      )
      .toOption
      .get

    for {
      client <- ZIO.service[Client]
      res <- client.url(url).get("")
      body <- res.body.asString
      resultOf = body.fromJson[DistanceResponse]
      distancesTexts <- ZIO.fromEither(resultOf.fold(
        error => Left(error),
        success => Right(success.rows.flatMap(_.elements.map(_.distance.text)))
      ))

      finalDistance <- ZIO.foreach(distancesTexts)(text => Console.printLine(s"La distance entre $origin et $destination est de : $text"))
    } yield finalDistance
  }

  def calculateDistance(origin: String, destination: String) = {
    for {
      _ <- fetchData(origin, destination)
    } yield Console.printLine(_)
  }

  override def run: ZIO[Any, Any, Unit] =
    val appLogic = for {
      _ <- calculateDistance("Paris", "Lyon")
    } yield ()

    appLogic.provide(Client.default, Scope.default)
}
