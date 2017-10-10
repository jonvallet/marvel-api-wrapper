package org.scalatra.example.swagger

import org.scalatra.ScalatraServlet
import org.scalatra.swagger.{ApiInfo, NativeSwaggerBase, Swagger}


class ResourcesApp(implicit val swagger: Swagger) extends ScalatraServlet with NativeSwaggerBase

object CharactersSwagger{
  val Info = ApiInfo(
    "The Characters API",
    "Docs for the Characters API",
    "http://scalatra.org",
    "apiteam@scalatra.org",
    "MIT",
    "http://opensource.org/licenses/MIT")
}
class CharactersSwagger extends Swagger("2.0", "1", CharactersSwagger.Info)
