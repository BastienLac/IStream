import zio._

object Main extends ZIOAppDefault {
  def run =
    for {
      _    <- Console.print("Please enter your name: ")
      name <- Console.readLine
      _    <- Console.printLine(s"Hello, $name!")
    } yield ()
}