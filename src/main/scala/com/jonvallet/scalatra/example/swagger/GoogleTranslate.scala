package com.jonvallet.scalatra.example.swagger

import com.google.cloud.translate.Translate.TranslateOption
import com.google.cloud.translate.TranslateOptions
import com.jonvallet.scalatra.example.swagger.Config.googleKey


object GoogleTranslate {
  def translate(apiKey: String, text: String, targetCode: String, sourceCode: String = "en"): String = {
    val translate = TranslateOptions.newBuilder.setApiKey(googleKey).build.getService

    val translation = translate.translate(
      text,
      TranslateOption.sourceLanguage(sourceCode),
      TranslateOption.targetLanguage(targetCode))

    translation.getTranslatedText
  }
}
