import java.io.File
import java.nio.file.{Files, Path, StandardCopyOption}
/**
  * Created by rhys on 24/10/2016.
  */
object fileOrganiser {
  def main(args: Array[String]): Unit = {

    val sourceDir = new File(args(0))
    val batchSize: Int = args(1).toInt

    val dateRegex = """(\d\d\d\d)-(\d\d)-(\d\d)""".r
    val files = if (sourceDir.exists && sourceDir.isDirectory) {
      sourceDir.listFiles.filter(_.isFile).toList
    } else {
      List[File]()
    }

    val fileMap = files.map(_.getName)
      .filter(dateRegex.findFirstIn(_).isDefined)
      .groupBy(_.substring(0, 10))

    fileMap.map{
      case (k, v) =>
        (s"${sourceDir.getAbsolutePath}/${k.substring(0, 4)}/$k", v)
    }.take(batchSize).foreach{
      case (targetDir, files) =>
        new File(targetDir).mkdirs()
        files.foreach {
          f =>
            val source: Path = new File(s"$sourceDir/$f").toPath
            val target: Path = new File(s"$targetDir/$f").toPath
            println(s"Moving from $source to $target")
            Files.move(source, target, StandardCopyOption.ATOMIC_MOVE)
        }
    }
  }
}
