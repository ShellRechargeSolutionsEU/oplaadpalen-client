package com.thenewmotion.oplaadpalen

/**
 * @author Yaroslav Klymko
 */
sealed trait Msg
sealed trait Req extends Msg
sealed trait Res extends Msg

case class ChargingPointsReq(owner: Option[String] = None,
                                batchType: Option[BatchType.Value] = None,
                                connectorType: Option[ConnectorType.Value] = None,
                                vehicleType: Option[VehicleType.Value] = None,
                                box: Option[Box] = None,
                                city: Option[String] = None) extends Req
case class ChargingPointsRes(chargingPoints: Seq[ChargingPoint]) extends Res


case object OwnersReq extends Req
case class OwnersRes(owners: Seq[String]) extends Res


case object AvailabilityReq extends Req
case class AvailabilityRes(availabilities: Seq[Availability]) extends Res


