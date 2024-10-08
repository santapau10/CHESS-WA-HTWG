file://<WORKSPACE>/lib/CHESS-WA-HTWG/src/main/scala/chess/controller/controller/Snapshot.scala
### file%3A%2F%2F%2FUsers%2Fpablolarraz%2FDesktop%2FUniversidad%2F4oCurso%2Fweb-applikationen%2Fplay-scala-seed%2Flib%2FCHESS-WA-HTWG%2Fsrc%2Fmain%2Fscala%2Fchess%2Fcontroller%2Fcontroller%2FSnapshot.scala:24: error: `;` expected but `:` found
object Snapshot:
               ^

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 2.13.14
Classpath:
<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.14/scala-library-2.13.14.jar [exists ]
Options:



action parameters:
uri: file://<WORKSPACE>/lib/CHESS-WA-HTWG/src/main/scala/chess/controller/controller/Snapshot.scala
text:
```scala
package chess.controller.controller

import chess.controller.{ISnapshot, IState, IController}
import chess.models.IGame
import chess.models.game.Game

import play.api.libs.json.{JsValue, Json}
import java.security.MessageDigest
import scala.xml.{Elem, Node}
class Snapshot(game: IGame, state: IState) extends ISnapshot {
  override def toXml: Elem =
    <snapshot>
      {game.toXml}{state.toXml}
    </snapshot>

  override def toJson: JsValue = Json.obj(
    "game" -> game.toJson,
    "state" -> state.toJson
  )
  override def getState: IState ={this.state}
  override def getGame: IGame ={this.game}
}

object Snapshot:
  def fromXml(node: Node, controller: IController): Snapshot =
    val game = Game.fromXml((node \ "game").head)
    val state = (node \ "state").text.trim match
      case "PreGameState" => PreGameState(controller)
      case "TurnStateWhite" => TurnStateWhite(controller)
      case "TurnStateBlack" => TurnStateBlack(controller)
      case _ => throw IllegalArgumentException("Invalid state")
    Snapshot(game, state)

  def fromJson(json: JsValue, controller: IController): Snapshot =
    val game = Game.fromJson((json \ "game").get)
    val state = (json \ "state").as[String] match
      case "PreGameState" => PreGameState(controller)
      case "TurnStateWhite" => TurnStateWhite(controller)
      case "TurnStateBlack" => TurnStateBlack(controller)
      case _ => throw IllegalArgumentException("Invalid state")
    Snapshot(game, state)

  def hash(input: String): String =
    val md = MessageDigest.getInstance("SHA-256")
    val hashBytes = md.digest(input.getBytes("UTF-8"))
    hashBytes.map("%02x".format(_)).mkString
```



#### Error stacktrace:

```
scala.meta.internal.parsers.Reporter.syntaxError(Reporter.scala:16)
	scala.meta.internal.parsers.Reporter.syntaxError$(Reporter.scala:16)
	scala.meta.internal.parsers.Reporter$$anon$1.syntaxError(Reporter.scala:22)
	scala.meta.internal.parsers.Reporter.syntaxError(Reporter.scala:17)
	scala.meta.internal.parsers.Reporter.syntaxError$(Reporter.scala:17)
	scala.meta.internal.parsers.Reporter$$anon$1.syntaxError(Reporter.scala:22)
	scala.meta.internal.parsers.ScalametaParser.syntaxErrorExpected(ScalametaParser.scala:394)
	scala.meta.internal.parsers.ScalametaParser.acceptStatSep(ScalametaParser.scala:450)
	scala.meta.internal.parsers.ScalametaParser.acceptStatSepOpt(ScalametaParser.scala:452)
	scala.meta.internal.parsers.ScalametaParser.statSeqBuf(ScalametaParser.scala:4107)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$statSeq$1(ScalametaParser.scala:4096)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$statSeq$1$adapted(ScalametaParser.scala:4096)
	scala.meta.internal.parsers.ScalametaParser.scala$meta$internal$parsers$ScalametaParser$$listBy(ScalametaParser.scala:562)
	scala.meta.internal.parsers.ScalametaParser.statSeq(ScalametaParser.scala:4096)
	scala.meta.internal.parsers.ScalametaParser.bracelessPackageStats$1(ScalametaParser.scala:4285)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$source$1(ScalametaParser.scala:4288)
	scala.meta.internal.parsers.ScalametaParser.atPos(ScalametaParser.scala:325)
	scala.meta.internal.parsers.ScalametaParser.autoPos(ScalametaParser.scala:369)
	scala.meta.internal.parsers.ScalametaParser.source(ScalametaParser.scala:4264)
	scala.meta.internal.parsers.ScalametaParser.entrypointSource(ScalametaParser.scala:4291)
	scala.meta.internal.parsers.ScalametaParser.parseSourceImpl(ScalametaParser.scala:119)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$parseSource$1(ScalametaParser.scala:116)
	scala.meta.internal.parsers.ScalametaParser.parseRuleAfterBOF(ScalametaParser.scala:58)
	scala.meta.internal.parsers.ScalametaParser.parseRule(ScalametaParser.scala:53)
	scala.meta.internal.parsers.ScalametaParser.parseSource(ScalametaParser.scala:116)
	scala.meta.parsers.Parse$.$anonfun$parseSource$1(Parse.scala:30)
	scala.meta.parsers.Parse$$anon$1.apply(Parse.scala:37)
	scala.meta.parsers.Api$XtensionParseDialectInput.parse(Api.scala:22)
	scala.meta.internal.semanticdb.scalac.ParseOps$XtensionCompilationUnitSource.toSource(ParseOps.scala:15)
	scala.meta.internal.semanticdb.scalac.TextDocumentOps$XtensionCompilationUnitDocument.toTextDocument(TextDocumentOps.scala:161)
	scala.meta.internal.pc.SemanticdbTextDocumentProvider.textDocument(SemanticdbTextDocumentProvider.scala:54)
	scala.meta.internal.pc.ScalaPresentationCompiler.$anonfun$semanticdbTextDocument$1(ScalaPresentationCompiler.scala:469)
```
#### Short summary: 

file%3A%2F%2F%2FUsers%2Fpablolarraz%2FDesktop%2FUniversidad%2F4oCurso%2Fweb-applikationen%2Fplay-scala-seed%2Flib%2FCHESS-WA-HTWG%2Fsrc%2Fmain%2Fscala%2Fchess%2Fcontroller%2Fcontroller%2FSnapshot.scala:24: error: `;` expected but `:` found
object Snapshot:
               ^