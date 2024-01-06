import zio._

object Main extends ZIOAppDefault {
    val voyages: List[Voyage] = List(Voyage(1, "Lyon", "Paris"))
    def run = Console.print(voyages.tail)
}