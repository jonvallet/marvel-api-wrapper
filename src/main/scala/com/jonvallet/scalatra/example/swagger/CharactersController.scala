package com.jonvallet.scalatra.example.swagger

import org.scalatra._
import org.scalatra.swagger.ResponseMessage

import scala.util.{Failure, Success}

// Swagger-specific Scalatra imports
import org.scalatra.swagger._

// JSON-related libraries
import org.json4s.{DefaultFormats, Formats}

// JSON handling support from Scalatra
import org.scalatra.json._

class CharactersController(implicit val swagger: Swagger) extends ScalatraServlet with NativeJsonSupport with SwaggerSupport  {

  // Sets up automatic case class to JSON output serialization, required by
  // the JValueResult trait.
  protected implicit val jsonFormats: Formats = DefaultFormats

  // A description of our application. This will show up in the Swagger docs.
  protected val applicationDescription = "The Marvel characters API. It exposes operations for browsing and searching marvel characters"

  // Before every action runs, set the content type to be in JSON format.
  before() {
    contentType = formats("json")
  }

  import Config._

  val client = new MarvelClient(publicKey, privateKey, client_endpoint)

  val getCharacters: SwaggerSupportSyntax.OperationBuilder =
    (apiOperation[List[Character]]("getCharactersIds")
      summary "Show all marvel characters ids"
      tags "characters"
      notes "Shows all the marvel characters ids."
      )

  get("/", operation(getCharacters)) {
    client.getAll match {
      case Success(value) => value
      case Failure(error) =>
        error.printStackTrace()
        halt(404, error)
    }
  }

  val errorResponse = ResponseMessage(404, "Character Not Found")
  val findByCharacterId: SwaggerSupportSyntax.OperationBuilder =
    (apiOperation[Character]("findByCharacterId")
      summary "Find a marvel character by id"
      tags "characters"
      parameters pathParam[String]("characterId").description("Character Id need to be fetched")
      responseMessage errorResponse)

  get("/:characterId", operation(findByCharacterId)) {
    client.getCharacter(params("characterId")) match {
      case Success(value) => value
      case Failure(error) =>
        error.printStackTrace()
        halt(404, errorResponse)
    }
  }

  val characterPowers: SwaggerSupportSyntax.OperationBuilder =
    (apiOperation[CharacterPowers]("getCharacterPowers")
      summary "Get a list of a characters powers"
      tags "characters"
      parameters pathParam[String]("characterId").description("Character Id")
      parameter queryParam[Option[String]]("language").description("Language code to translete to")
      responseMessage errorResponse)

  get("/:characterId/powers", operation(characterPowers)) {
    val characterPowers = client.getCharacterPowers(params("characterId")) match {
      case Success(characterPowers) => characterPowers
      case Failure(error) =>
        error.printStackTrace()
        halt(404, errorResponse)
    }
    params.get("language") match {
      case None => characterPowers
      case Some(language) =>
        val translateText = GoogleTranslate.translate(googleKey, characterPowers.powers, language)
        CharacterPowers(characterPowers.id, translateText)
    }
  }

}


