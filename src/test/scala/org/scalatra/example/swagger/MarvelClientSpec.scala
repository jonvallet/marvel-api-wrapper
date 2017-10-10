package org.scalatra.example.swagger

import org.scalatest.{Failed, FunSuite}

import scala.util.{Failure, Success}

class MarvelClientSpec extends FunSuite {

  import Config._

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
    val result = client.getCharacter("1017100")

    result match {
      case Success(character) => println(character)
      case Failure(error) => throw error
    }
  }

}
