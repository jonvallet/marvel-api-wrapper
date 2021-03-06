import com.jonvallet.scalatra.example.swagger._
import org.scalatra.LifeCycle
import javax.servlet.ServletContext

import com.jonvallet.scalatra.example.swagger.{CharactersController, CharactersSwagger, ResourcesApp}

class ScalatraBootstrap extends LifeCycle {

  implicit val swagger = new CharactersSwagger

  override def init(context: ServletContext) {
    context.mount(new CharactersController, "/characters", "characters")
    context.mount (new ResourcesApp, "/api-docs")
  }
}
