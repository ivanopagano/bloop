package bloop.integrations.sbt

import java.io.File

object Compat {
  implicit def fileToRichFile(file: File): sbt.RichFile = new sbt.RichFile(file)
}
