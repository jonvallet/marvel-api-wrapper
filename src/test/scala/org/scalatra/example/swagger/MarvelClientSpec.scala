package org.scalatra.example.swagger

import org.scalatest.{Failed, FunSuite}

import scala.util.{Failure, Success}

class MarvelClientSpec extends FunSuite {

  test(
  "auth with privateKey=45f2967954fca690f33361961ed4d29cf5bfd3f1 " +
    "and publicKey=99ffc7f9340c3e5d8a1b57b7356435b4 " +
    "and timeStamp=1 " +
    "should have a hash=db4649f56d310bf148557fd9d9984b81") {

    val client = new MarvelClient()

    val expectedValue = "db4649f56d310bf148557fd9d9984b81"
    val actualValue = client.auth("1")

    actualValue.toMap.get("hash") match {
      case Some(value) => assert(value == expectedValue)
      case _ => Failed
    }
  }

  test("test get all") {
    val client = new MarvelClient()
    val result = client.getAll

    result match {
      case Success(all) => println(all)
      case Failure(error) => throw error
    }
  }

  test("test get character") {
    val client = new MarvelClient()
    val result = client.getCharacter("1017100")

    result match {
      case Success(character) => println(character)
      case Failure(error) => throw error
    }
  }

}
