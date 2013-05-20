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

import scala.slick.session._
import slick.lifted.{MappedTypeMapper, TypeMapper}
import com.github.nscala_time.time.Imports._
import java.sql.Timestamp
import scala.language.postfixOps

object dal extends dal {

  object Bridges {
    def Games(implicit session: Session) = new ImplicitSession with Games { override val implicitSession = session }
    def Operators(implicit session: Session) = new ImplicitSession with Operators { override val implicitSession = session }
    def Tasks(implicit session: Session) = new ImplicitSession with Tasks { override val implicitSession = session }
  }
  
}

trait dal {
}


trait ImplicitSession {
  implicit val implicitSession: Session
}