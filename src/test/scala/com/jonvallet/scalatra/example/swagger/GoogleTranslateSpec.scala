package com.jonvallet.scalatra.example.swagger

import org.scalatest.FunSuite
import GoogleTranslate._

class GoogleTranslateSpec extends FunSuite {
  import Config.googleKey

  test("Test google translate") {

    val expectedValue = "Esto es una prueba"
    val actualValue = translate(googleKey, "This is a test", "es")

    assert(expectedValue == actualValue)
  }

}
