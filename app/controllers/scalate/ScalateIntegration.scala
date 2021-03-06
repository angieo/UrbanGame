/**Copyright 2013 BLStream, BLStream's Patronage Program Contributors
 *       http://blstream.github.com/UrbanGame/
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package controllers

import play.api._
import play.api.templates.{Html, HtmlFormat}
import http.{Writeable, ContentTypeOf, ContentTypes}
import mvc.Codec
import play.api.Play.current
import org.fusesource.scalate.layout.DefaultLayoutStrategy
import java.io.File

object Scalate {

  import org.fusesource.scalate._
  import org.fusesource.scalate.util._
  import org.fusesource.scalate.scaml.ScamlOptions
  import org.fusesource.scalate.Binding

  var format = Play.configuration.getString("scalate.format") match {
    case Some(configuredFormat) => configuredFormat
    case _ => "scaml"
  }

  lazy val path = Play.application.path

  lazy val scalateEngine = {
    val engine = new TemplateEngine
    engine.resourceLoader = new FileResourceLoader(Some(Play.getFile("app/views")))
    engine.sourceDirectories = new File(path, "app") :: Nil
    engine.layoutStrategy = new DefaultLayoutStrategy(engine, "app/views/layouts/default." + format)
    engine.classpath = "tmp/classes"
    engine.workingDirectory = Play.getFile("tmp")
    engine.combinedClassPath = true
    engine.classLoader = Play.classloader
    engine.importStatements = List(
      "import controllers._",
      "import play.api.i18n._",
      "import play.api.Application",
      "import models.utils._",
      "import play.api.mvc._"
    )
    ScamlOptions.format = ScamlOptions.Format.html5
    engine
  }

  def apply(template: String, myformat: String = format) = Template(template + "." + myformat)

  case class Template(name: String) {
    def render(args: (Symbol, Any)*)(implicit request: play.api.mvc.RequestHeader, lang: play.api.i18n.Lang, app: play.api.Application) = {
        
      import scala.language.postfixOps

      val extraBindings: Traversable[Binding] = Traversable(Binding(name="req", className="play.api.mvc.RequestHeader", isImplicit=true), 
        Binding(name="lan", className="play.api.i18n.Lang", isImplicit=true), Binding(name="app", className="play.api.Application", isImplicit=true))
      val bindings = args.map {
            case (k, v) => k.name -> v
      }.toMap + (("req", request),("lan", lang),("app", app))

      ScalateContent{
        scalateEngine.layout(name, bindings, extraBindings)
      }
    }
  }

  case class ScalateContent(val cont: String)

  implicit def writeableOf_ScalateContent[SC <: ScalateContent](implicit codec: Codec): Writeable[SC] = {
    Writeable(scalate => codec.encode(scalate.cont))
  }

  implicit def contentTypeOf_ScalateContent[SC <: ScalateContent](implicit codec: Codec): ContentTypeOf[SC] = {
    ContentTypeOf(Some(ContentTypes.HTML))
  }
}
