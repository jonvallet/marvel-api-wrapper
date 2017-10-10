package org.scalatra.example.swagger

import java.security.MessageDigest
import java.util.Date

import scalaj.http.{Http, HttpResponse}
import spray.json.{DefaultJsonProtocol, _}

import scala.util.{Failure, Success, Try}

case class Character(id: Int, name: String, description: String, thumbnail: Thumbnail)
case class Thumbnail(path: String, extension: String)

object MarvelClient {
  def md5(s: String): String = MessageDigest.getInstance("MD5").digest(s.getBytes).map("%02x".format(_)).mkString
}

class MarvelClient (publicKey : String = "99ffc7f9340c3e5d8a1b57b7356435b4",
                    privateKey : String = "45f2967954fca690f33361961ed4d29cf5bfd3f1",
                    endpoint: String = "http://gateway.marvel.com/v1/public/characters") extends DefaultJsonProtocol {

  import MarvelClient._
  implicit val thumbnailFormat = jsonFormat2(Thumbnail.apply)
  implicit val characterFormat = jsonFormat4(Character.apply)

  def auth(timeStamp : String): Seq[(String, String)] = {
    val toHash = s"$timeStamp$privateKey$publicKey"
    val hash = md5(toHash)

    Seq("ts" -> timeStamp,
      "apikey" -> publicKey,
      "hash" -> hash)
  }

  val auth: Seq[(String, String)] = auth(new Date().getTime.toString)

  //TODO Retrieve all the ids and save the results into memory cache.
  def getAll: Try[List[String]] = {
    val response = Http(endpoint).params(auth).asString
    if (response.isError) return Failure(new RuntimeException(response.body))
    val json: Try[JsArray] = toJsArray(response)

    json match {
      case Success(value) => Success(value.elements.map(jsValue => jsValue.asJsObject.fields("id").toString()).toList)
      case Failure(error) => Failure(error)
    }

  }


  private def toJsArray(response: HttpResponse[String]): Try[JsArray] = {
    response.body.parseJson.asJsObject.fields("data").asJsObject.fields("results") match {
      case array: JsArray => Success(array)
      case _ => Failure(new RuntimeException("Expected an array of results"))
    }
  }

  def getCharacter(characterId: String): Try[Character] = {
    val response = Http(s"$endpoint/$characterId").params(auth).asString
    if (response.isError) return Failure(new RuntimeException(response.body))
    val json: Try[JsArray] = toJsArray(response)
    json match {
      case Success(value) => Success(value.elements.map(e => e.convertTo[Character]).head)
      case Failure(error) => Failure(error)
    }
  }
}


