package com.thenewmotion.oplaadpalen

import spray.json._

/**
 * @author Yaroslav Klymko
 */
trait ReqRes[REQ, RES] extends RootJsonReader[RES] {
  def path: String
}

trait ReqParameters[REQ] {
  def params(req: REQ): Map[String, String]
}

object ReqRes {
  import OplaadpalenJsonProtocol._

  implicit object GetChargingPointsReqRes extends ReqRes[ChargingPointsReq, ChargingPointsRes] with ReqParameters[ChargingPointsReq] {
    def path = "chargingpoints"
    def read(json: JsValue) = ChargingPointsRes(ChargingPointsReader.read(json))

    def params(req: ChargingPointsReq) = Seq(
      "owner" -> req.owner,
      "batchtype" -> req.batchType,
      "connectortype" -> req.connectorType,
      "vehicleType" -> req.vehicleType,
      "box" -> req.box.map(x => Seq(
        x.leftTop.lat,
        x.leftTop.lng,
        x.rightBot.lat,
        x.rightBot.lng).mkString(",")),
      "city" -> req.city).collect {
      case (key, Some(value)) => key -> value.toString
    }.toMap
  }

  implicit object AvailabilityReqRes extends ReqRes[AvailabilityReq.type, AvailabilityRes] {
    def path = "availability"
    def read(json: JsValue) = AvailabilityRes(AvailabilitiesReader.read(json))
  }

  implicit object OwnersReqRes extends ReqRes[OwnersReq.type, OwnersRes] {
    def path = "owners"
    def read(json: JsValue) = OwnersRes(OwnersReader.read(json))
  }
}