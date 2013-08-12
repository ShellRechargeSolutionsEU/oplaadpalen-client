package com.thenewmotion.oplaadpalen
package examples

/**
 * @author Yaroslav Klymko
 */
object Examples {

  object ChargingPointsExample {
    import com.thenewmotion.oplaadpalen._
    import akka.actor.ActorSystem
    import scala.concurrent.Future

    implicit val system = ActorSystem()
    import system.dispatcher

    val client = new Client(devKey = "<request key at http://oplaadpalen.nl/dev/register>")
    val req = ChargingPointsReq(
      city = Some("Amsterdam"),
      vehicleType = Some(VehicleType.Car))
    val future: Future[ChargingPointsRes] = client(req)
    future.onSuccess {
      case ChargingPointsRes(chargingPoints) =>
    }
  }

  object OwnersExample {
    import com.thenewmotion.oplaadpalen._
    import akka.actor.ActorSystem
    import scala.concurrent.Future

    implicit val system = ActorSystem()
    import system.dispatcher

    val client = new Client(devKey = "<request key at http://oplaadpalen.nl/dev/register>")
    val future: Future[OwnersRes] = client(OwnersReq)
    future.onSuccess {
      case OwnersRes(owners) =>
    }
  }

  object AvailabilityExample {
    import com.thenewmotion.oplaadpalen._
    import akka.actor.ActorSystem
    import scala.concurrent.Future

    implicit val system = ActorSystem()
    import system.dispatcher

    val client = new Client(devKey = "<request key at http://oplaadpalen.nl/dev/register>")
    val future: Future[AvailabilityRes] = client(AvailabilityReq)
    future.onSuccess {
      case AvailabilityRes(availabilities) =>
    }
  }

}
