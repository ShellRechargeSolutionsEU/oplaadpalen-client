# oplaadpalen-client [![Build Status](https://secure.travis-ci.org/thenewmotion/oplaadpalen-client.png)](http://travis-ci.org/thenewmotion/oplaadpalen-client)

Client for [Oplaadpalen.nl](http://www.oplaadpalen.nl) [Rest Json API](http://www.oplaadpalen.nl/w/outbound-api)

## Examples

### Charging Points

```scala
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
```

### Owners

```scala
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
```

### Availability

```scala
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
```

## Setup

1. Add this repository to your pom.xml:
```xml
    <repository>
        <id>thenewmotion</id>
        <name>The New Motion Repository</name>
        <url>http://nexus.thenewmotion.com/content/repositories/releases-public</url>
    </repository>
```

2. Add dependency to your pom.xml:
```xml
    <dependency>
        <groupId>com.thenewmotion</groupId>
        <artifactId>oplaadpalen-client</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>
```