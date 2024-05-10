package controller

import chess.models.Board
import chess.controller.Controller
import chess.view.TUI
import org.scalatest.wordspec.AnyWordSpec
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class MainSpec extends AnyWordSpec {

  "Main" when {
    "executed" should {
      "initialize the game correctly" in {
        // Redirect standard output to capture printed messages
        val outputStream = new ByteArrayOutputStream()
        Console.withOut(new PrintStream(outputStream)) {
          main()
        }

        // Get the printed message
        val consoleOutput = outputStream.toString.trim

        // Check if the expected message is printed
        assert(consoleOutput == "")
      }
    }
  }
}