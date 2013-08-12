package com.thenewmotion.oplaadpalen

import akka.actor.ActorSystem
import scala.concurrent.Future
import spray.httpx.SprayJsonSupport._
import spray.client.pipelining._
import com.typesafe.scalalogging.slf4j.Logging


/**
 * @author Yaroslav Klymko
 */
class Client(devKey: String, url: String = "http://oplaadpalen.nl")(implicit system: ActorSystem) extends Logging {

  import system.dispatcher

  def apply[REQ, RES](req: REQ)(implicit reqRes: ReqRes[REQ, RES]): Future[RES] = {
    val pipeline = sendReceive ~> unmarshal[RES]

    val suffix = reqRes match {
      case x: ReqParameters[REQ] =>
        val params = x.params(req).map {
          case (key, value) => s"$key=$value"
        }.mkString("&")
        if (params.isEmpty) "" else s"?$params"

      case _ => ""
    }

    val get = Get(s"$url/api/${reqRes.path}/$devKey/json$suffix")
    logger.debug(get.toString)
    pipeline(get)
  }
}


case class Availability(id: Long, nrOfOutlets: Int, available: Int, occupied: Int, failure: Int)


object BatchType extends Enumeration {
  val DCfast, // fast charger (CHAdeMO)
  DCsemi, // semi-quick charger (CHAdeMO)
  AC3phase, // phase power current (400V)
  ACsimple = Value // single phase 230V
}


object ConnectorType extends Enumeration {
  val CHAdeMO, //CHAdeMO fast charging plug
  J1772, // SAE J1772 connector type 1
  Mennekes, // type 2 Mennekes plug
  CEErood, // CEE red 400V 3 phase plug
  CEEblauw, // CEE blue 230V single phase plug
  Schuko = Value // regular outlet
}


object VehicleType extends Enumeration {
  val Car, Bike = Value
}


case class Coordinates(lat: Double, lng: Double)


case class Box(leftTop: Coordinates, rightBot: Coordinates)

object Box {
  def apply(ltLat: Double, ltLng: Double, rbLat: Double, rbLng: Double): Box = Box(
    leftTop = Coordinates(lat = ltLat, lng = ltLng),
    rightBot = Coordinates(lat = rbLat, lng = rbLng))
}


case class Address(coordinates: Coordinates,
                   address: Option[String],
                   postalCode: Option[String],
                   city: Option[String],
                   country: Option[String])


case class ChargingPoint(id: Long,
                         name: String,
                         phone: Option[String],
                         address: Address,
                         url: Option[String],
                         owner: Option[String],
                         email: Option[String],
                         openTimes: Option[String],
                         chargeType: String,
                         connectorType: Option[ConnectorType.Value],
                         nrOfOutlets: Int,
                         cards: Seq[String],
                         priceMethod: Option[String],
                         price: Option[Double],
                         power: String, // `50kW`, `44kW`, `22kW`, `20kW`, `11kW`, `5kW`, `3,7kw`
                         vehicle: Option[VehicleType.Value],
                         facilities: Seq[String],
                         realtimeStatus: Boolean)