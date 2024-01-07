import zio.json._

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