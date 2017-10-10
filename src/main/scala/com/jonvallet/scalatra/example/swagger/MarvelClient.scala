package com.jonvallet.scalatra.example.swagger

import java.security.MessageDigest
import java.util.Date

import scalaj.http.{Http, HttpResponse}
import spray.json.{DefaultJsonProtocol, _}
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._

import scala.util.{Failure, Success, Try}

case class Character(id: Int, name: String, description: String, thumbnail: Thumbnail, urls: List[Map[String,String]])
case class Thumbnail(path: String, extension: String)
case class Url(aType: String, url: String)
case class CharacterPowers(id: Int, powers: String)

object MarvelClient {
  def md5(s: String): String = MessageDigest.getInstance("MD5").digest(s.getBytes).map("%02x".format(_)).mkString
}

class MarvelClient (publicKey : String,
                    privateKey : String,
                    endpoint: String) extends DefaultJsonProtocol {

  import MarvelClient._
  implicit val thumbnailFormat = jsonFormat2(Thumbnail.apply)
  implicit val characterFormat = jsonFormat5(Character.apply)

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

  def getCharacterPowers(characterId: String): Try[CharacterPowers] = {
    val character: Character = getCharacter(characterId) match {
      case Success(value) => value
      case Failure(error) => return Failure(error)
    }

    val wikiUrl = character.urls.filter(e => e.get("type").contains("wiki")).head.get("url") match {
      case Some(url) => url
      case None => return Success(CharacterPowers(character.id, "No powers found"))
    }

    Success(CharacterPowers(character.id, extractPowers(wikiUrl)))
  }

  def extractPowers(wikiUrl: String): String = {
    val browser = JsoupBrowser()
    val doc = browser.get(wikiUrl)

    doc >> text("#char-powers-content")
  }
}


