package org.scalatra.example.swagger

import java.util.Properties

object Config {
  val (privateKey, publicKey, client_endpoint) = try {
    val prop = new Properties()
    prop.load(getClass.getResourceAsStream("/config.properties"))
    (
      prop.getProperty("privateKey"),
      prop.getProperty("publicKey"),
      prop.getProperty("endpoint")
    )
  }catch { case e: Exception =>
    e.printStackTrace()
    sys.exit(1)
  }
}
