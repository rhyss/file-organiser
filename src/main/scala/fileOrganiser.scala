import java.io.File
import java.nio.file.{Files, Path, StandardCopyOption}
/**
  * Created by rhys on 24/10/2016.
  */
object fileOrganiser {
  def main(args: Array[String]): Unit = {

    val sd = new File("/Users/rhys/Dropbox/Camera Uploads/")
    val dateRegex = """(\d\d\d\d)-(\d\d)-(\d\d)""".r
    val files = if (sd.exists && sd.isDirectory) {
      sd.listFiles.filter(_.isFile).toList
    } else {
      List[File]()
    }

    val fm = files.map(_.getName)
      .filter(dateRegex.findFirstIn(_).isDefined)
      .groupBy(_.substring(0, 10))

    fm.map{
      case (k, v) =>
        (s"${sd.getAbsolutePath}/${k.substring(0, 4)}/$k", v)
    }.take(1).foreach{
      case (td, fs) =>
        val folder: File = new File(td)
        folder.mkdirs()
        fs.foreach {
          f =>
            val source: Path = new File(s"$sd/$f").toPath
            val target: Path = new File(s"$td/$f").toPath
            println(s"Moving from $source to $target")
            Files.move(source, target, StandardCopyOption.ATOMIC_MOVE)
        }
    }
  }
}
