file://<WORKSPACE>/lib/CHESS-WA-HTWG/src/main/scala/chess/controller/controller/ICommand.scala
### file%3A%2F%2F%2FUsers%2Fpablolarraz%2FDesktop%2FUniversidad%2F4oCurso%2Fweb-applikationen%2Fplay-scala-seed%2Flib%2FCHESS-WA-HTWG%2Fsrc%2Fmain%2Fscala%2Fchess%2Fcontroller%2Fcontroller%2FICommand.scala:10: error: `;` expected but `:` found
class Command(controller: IController) extends ICommand:
                                                       ^

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 2.13.14
Classpath:
<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.14/scala-library-2.13.14.jar [exists ]
Options:



action parameters:
uri: file://<WORKSPACE>/lib/CHESS-WA-HTWG/src/main/scala/chess/controller/controller/ICommand.scala
text:
```scala
package chess.controller.controller

import chess.controller.{controller, *}
import chess.models.game.{Chesspiece, Colors, PiecesFactory}
import chess.models.IPiecesFactory
import play.api.libs.json.{JsValue, Json}

import scala.xml.{Node, Utility}

class Command(controller: IController) extends ICommand:
  private var snapshot: Option[ISnapshot] = None

  override def saveSnapshot(): Unit = snapshot = Some(controller.snapshot)

  override def undo(): Unit = snapshot match
    case Some(snapshot) => controller.restoreSnapshot(snapshot)
    case None => throw IllegalStateException("No snapshot to restore")

  override def execute(): Unit = ()

class MovePiecesCommand(controller: IController, l1: Int, n1: Int, l2: Int, n2: Int) extends Command(controller) {
  override def execute(): Unit = {
    val mPiece = controller.getGame.getBoardList.find(p => p.getCords._1 == l1 && p.getCords._2 == n1)
    val updatedList = controller.getGame.getBoardList.filterNot(p => p.getCords == mPiece.get.getCords).filterNot(p => p.getCords == (l2, n2)) :+ PiecesFactory().addPiece(mPiece.get.getPiece, (l2, n2), mPiece.get.getColor, mPiece.get.isMoved, (l1, n1))
    mPiece match {
      case Some(p) =>
        if (p.checkMove(l1, n1, l2, n2, controller.getGame.getBoardList) && !controller.getGame.isKingInCheck(updatedList, p.getColor)) {
          // Prüfen, ob der Zug ein enPassant Zug war
          if (p.getPiece == Chesspiece.PAWN && (l2 == p.getCords._1 + 1 || l2 == p.getCords._1 - 1)
            && !controller.getGame.getBoardList.exists(p => p.getCords == (l2, n2))) {
            if (p.getColor == Colors.WHITE) {
              controller.enPassantPiece(l1, n1, l2, n2, l2, n2 - 1)
            } else {
              controller.enPassantPiece(l1, n1, l2, n2, l2, n2 + 1)
            }
          }
          // Prüfen, ob der Zug ein Short Castling Zug war
          else if (p.getPiece == Chesspiece.KING && math.abs(l2 - l1) == 2 && l2 > l1) {
            // Short Castling Bedingungen prüfen
            val rookX1 = 7
            val rookY1 = n1
            val rookX2 = l2 - 1
            if (controller.getGame.getBoardList.exists(r => r.getCords == (rookX1, rookY1) && r.getPiece == Chesspiece.ROOK && !r.isMoved)) {
              controller.shortCastling(l1, n1, l2, n2, rookX1, rookY1, rookX2)
            }
          }
          // Prüfen, ob der Zug ein Long Castling Zug war
          else if (p.getPiece == Chesspiece.KING && math.abs(l2 - l1) == 2 && l2 < l1) {
            // Long Castling Bedingungen prüfen
            val rookX1 = 0
            val rookY1 = n1
            val rookX2 = l2 + 1
            if (controller.getGame.getBoardList.exists(r => r.getCords == (rookX1, rookY1) && r.getPiece == Chesspiece.ROOK && !r.isMoved)) {
              controller.longCastling(l1, n1, l2, n2, rookX1, rookY1, rookX2)
            }
          } else {
            // falls nicht, einfach normal bewegen
            controller.movePieces(l1, n1, l2, n2)
          }
        } else if (p.getColor == Colors.WHITE) {
          controller.handleAction(CancelMoveWhite())
        } else if (p.getColor == Colors.BLACK) {
          controller.handleAction(CancelMoveBlack())
        } else {
          throw IllegalStateException("invalid Color")
        }
      case None =>
    }
  }
}


class ChangeStateCommand(state: IState, c: IController) extends Command(c):
  override def execute(): Unit = {
    c.changeState(state)
  }

case class LoadXmlCommand(controller: IController, node: Node) extends Command(controller):

  override def execute(): Unit =
    val hash = (node \ "hash").text
    val xmlSnapshot = (node \ "snapshot").head
    if hash == " " + Snapshot.hash(Utility.trim(xmlSnapshot).toString) + " " then // neu = alt -> ladet
      controller.restoreSnapshot(Snapshot.fromXml(xmlSnapshot, controller))
    else
      println(hash)
      println(Snapshot.hash(Utility.trim(xmlSnapshot).toString))
      println("Invalid XML progress file!")

case class LoadJsonCommand(controller: IController, json: JsValue) extends Command(controller):

  override def execute(): Unit =
    val hash = (json \ "hash").get.as[String]
    val jsonSnapshot = (json \ "snapshot").get
    if hash == Snapshot.hash(Json.stringify(jsonSnapshot)) then
      controller.restoreSnapshot(Snapshot.fromJson(jsonSnapshot, controller))
    else
      println("Invalid JSON progress file!")

class RestartCommand(c: IController) extends Command(c):
  override def execute(): Unit = {
    c.initGame()
    c.changeState(TurnStateWhite(c))
  }
class PromotionCommand(chesspiece: Chesspiece, c: IController) extends Command(c):
  override def execute(): Unit = {
    c.updateBoard(c.getGame.getBoard.promotePiece(chesspiece, c.getGame.getBoardList))
    if(c.getGame.getBoardList.last.getColor == Colors.WHITE) c.changeState(TurnStateBlack(c)) else c.changeState(TurnStateWhite(c))
  }
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

file%3A%2F%2F%2FUsers%2Fpablolarraz%2FDesktop%2FUniversidad%2F4oCurso%2Fweb-applikationen%2Fplay-scala-seed%2Flib%2FCHESS-WA-HTWG%2Fsrc%2Fmain%2Fscala%2Fchess%2Fcontroller%2Fcontroller%2FICommand.scala:10: error: `;` expected but `:` found
class Command(controller: IController) extends ICommand:
                                                       ^