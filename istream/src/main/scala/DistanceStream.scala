import zio._
import zio.http._
import zio.stream._
import zio.Duration._

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
            _ <- Console.printLine(s"body size is: ${body.length}")
          } yield body
        }
        .foreach(Console.printLine(_))
    } yield ()

    appLogic.provide(Client.default, Scope.default)

}
