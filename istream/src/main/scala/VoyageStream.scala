import zio._
import zio.stream.ZStream
import com.github.tototoshi.csv._

object VoyageStream extends ZIOAppDefault {

  override val run: ZIO[Any & ZIOAppArgs & Scope, Throwable, Unit] =
    for {
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
        .grouped(5)
        .foreach(Console.printLine(_))
      _ <- ZIO.succeed(source.close())
    } yield ()
}
