package com.thenewmotion.oplaadpalen

import spray.json._

/**
 * @author Yaroslav Klymko
 */
object OplaadpalenJsonProtocol extends DefaultJsonProtocol {
  type Availabilities = Seq[Availability]
  type Owners = Seq[String]

  implicit object AvailabilitiesReader extends RootJsonReader[Availabilities] {

    object AvailabilityReader extends JsonReader[Availability] {

      case class JsAvailability(id: String, nroutlets: String, available: String, occupied: String, failure: String)

      val jsAvailabilityReader = jsonFormat5(JsAvailability)

      def toValue(x: JsAvailability) = Availability(
        id = x.id.toLong,
        nrOfOutlets = x.nroutlets.toInt,
        available = x.available.toInt,
        occupied = x.occupied.toInt,
        failure = x.failure.toInt)

      def read(json: JsValue) = toValue(jsAvailabilityReader.read(json))
    }

    def read(json: JsValue) = json match {
      case JsArray(xs) => xs.map(AvailabilityReader.read)
      case _ => deserializationError("Array of availabilities expected")
    }
  }

  implicit object OwnersReader extends RootJsonReader[Owners] {
    def read(json: JsValue) = json match {
      case JsArray(xs) => xs.map(StringJsonFormat.read)
      case _ => deserializationError("Array of owners expected")
    }
  }


  case class JsAddress(lng: String,
                       lat: String,
                       address: Option[String],
                       postalcode: Option[String],
                       city: Option[String],
                       country: Option[String])

  case class JsChargingPoint(id: String,
                             name: String,
                             phone: Option[String],
                             url: Option[String],
                             owner: Option[String],
                             email: Option[String],
                             opentimes: Option[String],
                             chargetype: String,
                             connectortype: String,
                             nroutlets: String,
                             cards: Seq[String],
                             pricemethod: Option[String],
                             price: Option[String],
                             power: String,
                             vehicle: String,
                             facilities: Seq[String],
                             realtimestatus: Boolean)

  implicit object ChargingPointsReader extends RootJsonReader[Seq[ChargingPoint]] {

    val jsChargepointFormat = jsonFormat17(JsChargingPoint)
    val JsAddressFormat = jsonFormat6(JsAddress)

    def toAddress(x: JsAddress) = Address(
      coordinates = Coordinates(lat = x.lat.toDouble, lng = x.lng.toDouble),
      address = x.address,
      postalCode = x.postalcode,
      city = x.city,
      country = x.country)

    def toChargingPoint(cp: JsChargingPoint, a: JsAddress) = {
      def enum[T <: Enumeration](t: T, x: String): Option[T#Value] = t.values.find(_.toString equalsIgnoreCase x)

      ChargingPoint(id = cp.id.toLong,
        name = cp.name,
        phone = cp.phone,
        address = toAddress(a),
        url = cp.url,
        owner = cp.owner,
        email = cp.email,
        openTimes = cp.opentimes,
        chargeType = cp.chargetype,
        connectorType = enum(ConnectorType, cp.connectortype),
        nrOfOutlets = cp.nroutlets.toInt,
        cards = cp.cards,
        priceMethod = cp.pricemethod,
        price = cp.price.map(_.toDouble),
        power = cp.power,
        vehicle = enum(VehicleType, cp.vehicle),
        facilities = cp.facilities,
        realtimeStatus = cp.realtimestatus)
    }

    def read(json: JsValue) = json match {
      case JsArray(JsString("error") :: JsString("no chargingpoints found") :: Nil) => Nil

      case JsArray(xs) => xs.map(json =>
        toChargingPoint(jsChargepointFormat.read(json), JsAddressFormat.read(json)))

      case _ => deserializationError("Array of chargepoints expected")
    }
  }
}
