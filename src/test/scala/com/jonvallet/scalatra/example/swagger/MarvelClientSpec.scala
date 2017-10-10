package com.jonvallet.scalatra.example.swagger

import org.scalatest.{Failed, FunSuite}

import scala.util.{Failure, Success}

class MarvelClientSpec extends FunSuite {

  import com.jonvallet.scalatra.example.swagger.Config._

  test(
  "auth with privateKey=privateKey" +
    "and publicKey=publicKey " +
    "and timeStamp=1 " +
    "should have a hash=90c0bdf9b064fe3c9e373d9547eea445") {

    val client = new MarvelClient("privateKey", "publicKey", "endpoint")

    val expectedValue = "90c0bdf9b064fe3c9e373d9547eea445"
    val actualValue = client.auth("1")

    actualValue.toMap.get("hash") match {
      case Some(value) => assert(value == expectedValue)
      case _ => Failed
    }
  }

  test("test get all") {
    val client = new MarvelClient(publicKey, privateKey, client_endpoint)
    val result = client.getAll

    result match {
      case Success(all) => println(all)
      case Failure(error) => throw error
    }
  }

  test("test get character") {
    val client = new MarvelClient(publicKey, privateKey, client_endpoint)
    val result = client.getCharacter("1009610")

    result match {
      case Success(character) => println(character)
      case Failure(error) => throw error
    }
  }

  test("test get character Powers") {
    val client = new MarvelClient(publicKey, privateKey, client_endpoint)
    val result = client.getCharacterPowers("1009610")

    result match {
      case Success(character) => println(character)
      case Failure(error) => throw error
    }
  }

  test("test scrap wiki") {
    val client = new MarvelClient(publicKey, privateKey, client_endpoint)

    val expectedPowers = "Peter can cling to most surfaces, has superhuman strength (able to lift 10 tons optimally) and is roughly 15 times more agile than a regular human. The combination of his acrobatic leaps and web-slinging enables him to travel rapidly from place to place. His spider-sense provides an early warning detection system linked with his superhuman kinesthetics, enabling him the ability to evade most any injury, provided he doesn't cognitively override the autonomic reflexes. Note: his power enhancements through his transformation by the Queen and after battling Morlun - including his organic web glands and stingers - have been undone after Spider-Man's deal with Mephisto."
    val actualPowers = client.extractPowers("http://marvel.com/universe/Spider-Man_(Peter_Parker)?utm_campaign=apiRef&utm_source=99ffc7f9340c3e5d8a1b57b7356435b4")

    assert(actualPowers == expectedPowers)
  }
}
