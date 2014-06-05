package com.thenewmotion.oplaadpalen

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.specification.Scope
import akka.actor.ActorSystem
import scala.concurrent.duration._
import scala.concurrent.Await

/**
 * @author Yaroslav Klymko
 */
class ClientSpec extends SpecificationWithJUnit {
  "client" should {
    "get charge points" in new ClientScope {
      for {
        owner <- None :: List("e-laad", "essent").map(Some.apply)
        batchType <- enums(BatchType)
        connectorType <- enums(ConnectorType)
        vehicleType <- enums(VehicleType)
        box <- List(None, Some(Box(51.25, 3.4, 51.45, 4)))
        city <- None :: List("terneuzen", "Amsterdam").map(Some.apply)
      } {
        val req = ChargingPointsReq(
          owner = owner,
          batchType = batchType,
          connectorType = connectorType,
          vehicleType = vehicleType,
          box = box,
          city = city)
        ?(req) must not(throwA[RuntimeException])
      }
    }
    "get owners" in new ClientScope {
      ?(OwnersReq).owners must not beEmpty
    }
    "get availability" in new ClientScope {
      ?(AvailabilityReq).availabilities must not beEmpty
    }
  }

  trait ClientScope extends Scope {
    implicit val system = ActorSystem()
    val client = new Client(devKey = "<request key at http://oplaadpalen.nl/dev/register>")

    def enums[T <: Enumeration](x: T): List[Option[T#Value]] = {
      x.values.map(Some.apply).toList
    }

    def ?[REQ, RES](req: REQ)(implicit reqRes: ReqRes[REQ, RES]): RES =
      Await.result(client(req), FiniteDuration(10, SECONDS))
  }

}
