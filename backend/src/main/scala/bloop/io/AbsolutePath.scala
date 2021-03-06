// Slight modification of scalameta io utils
package bloop.io

import java.io.File
import java.nio.file.{Files, Path, Paths => NioPaths}

final class AbsolutePath private (val underlying: Path) extends AnyVal {
  def syntax: String = toString
  def structure: String = s"""AbsolutePath("$syntax")"""
  override def toString: String = underlying.toString

  def toRelative(prefix: AbsolutePath): RelativePath =
    RelativePath(prefix.underlying.relativize(underlying))

  def resolve(other: RelativePath): AbsolutePath =
    AbsolutePath(underlying.resolve(other.underlying))(this)
  def resolve(other: String): AbsolutePath = AbsolutePath(underlying.resolve(other))(this)
  def getParent: AbsolutePath = AbsolutePath(underlying.getParent)

  def isFile: Boolean = Files.isRegularFile(underlying)
  def isDirectory: Boolean = Files.isDirectory(underlying)
  def readAllBytes: Array[Byte] = Files.readAllBytes(underlying)
  def toFile: File = underlying.toFile()
}

object AbsolutePath {
  implicit def workingDirectory: AbsolutePath =
    new AbsolutePath(NioPaths.get(sys.props("user.dir")))
  def apply(file: File)(implicit cwd: AbsolutePath): AbsolutePath = apply(file.toPath)(cwd)
  def apply(path: String)(implicit cwd: AbsolutePath): AbsolutePath = apply(NioPaths.get(path))(cwd)
  def apply(path: Path)(implicit cwd: AbsolutePath): AbsolutePath =
    if (path.isAbsolute) new AbsolutePath(path) else cwd.resolve(path.toString)
}
