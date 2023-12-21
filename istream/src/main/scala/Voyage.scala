import zio.json._

case class Voyage(
    voyage: Int,
    depart: String,
    arrivee: String
)

object Voyage {
  implicit val voyageEncoder: JsonEncoder[Voyage] = DeriveJsonEncoder.gen[Voyage]
}