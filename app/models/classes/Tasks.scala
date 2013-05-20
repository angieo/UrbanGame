/**Copyright 2013 BLStream, BLStream's Patronage Program Contributors
 *     http://blstream.github.com/UrbanGame/
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package models

import play.api.Play.current
import play.api.db.slick.DB
import play.api.db.slick.Config.driver.simple._
import slick.lifted.{MappedTypeMapper, TypeMapper}
import com.github.nscala_time.time.Imports._
import java.sql.Timestamp
import scala.language.postfixOps
import models.mutils._

object Tasks extends Table[TasksList]("TASKS") {
	def id = column[Int]("id", O.NotNull, O.AutoInc)
	def gameId = column[Int]("gameId", O.NotNull)
	def version = column[Int]("version", O.NotNull, O.Default(1))
	def name = column[String]("name", O.NotNull)
	def description = column[String]("description", O.NotNull)
	def deadline = column[DateTime]("deadline", O.NotNull)
	def maxpoints = column[Int]("maxpoints", O.NotNull)
	def maxattempts = column[Int]("maxattempts", O.NotNull)
	def * = id.? ~ gameId ~ version ~ name ~ description ~ deadline ~ maxpoints ~ maxattempts <> (TasksList, TasksList.unapply _)

	implicit val DateTimeMapper: TypeMapper[DateTime] = MappedTypeMapper.base[DateTime, Timestamp](d => new Timestamp(d millis), t => new DateTime(t getTime))

}

trait Tasks { this: ImplicitSession =>

}