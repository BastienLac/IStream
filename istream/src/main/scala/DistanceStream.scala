import zio._
import zio.http._
import zio.json._

object DistanceStream extends ZIOAppDefault {
  // Retrieve all data from the API using a departure city and a destination
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

      // transform data in DistanceResponse
      resultOf = body.fromJson[DistanceResponse]
      distancesTexts <- ZIO.fromEither(resultOf.fold(
        error => Left(error),
        success => Right(success.rows.flatMap(_.elements.map(_.distance.text)))
      ))

      finalDistance <- ZIO.foreach(distancesTexts)(text => Console.printLine(s"Distance between $origin and $destination is : $text"))
    } yield Console.printLine(finalDistance)
  }

  override def run: ZIO[Any, Any, Unit] =
    val appLogic = for {
      _ <- fetchData("Paris", "Lyon")
    } yield ()

    appLogic.provide(Client.default, Scope.default)
}
