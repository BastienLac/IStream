import zio._
import zio.http._
import zio.stream._
import zio.Duration._

object HttpStream extends ZIOAppDefault {
  def fetchData() = {
    val url = URL
      .decode(
        // "https://distance-calculator.p.rapidapi.com/distance/simple?lat_1=47.373535&long_1=8.541109&lat_2=42.335321&long_2=-71.023516&unit=miles&decimal_places=2"
      )
      .toOption
      .get

    for {
      client <- ZIO.service[Client]
      res <- client.url(url).get("/")
    } yield res
  }

  override def run: ZIO[Any, Any, Unit] =
    val appLogic = for {
      _ <- ZStream(fetchData())
        .repeat(Schedule.spaced(10.seconds))
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
