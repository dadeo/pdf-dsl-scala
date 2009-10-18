package pdfdsl.support


import java.io.{FileOutputStream, File, ByteArrayOutputStream, FileInputStream}

object FileUtility {
  def loadBytes(fileName: String) : Array[Byte] = loadBytes(new File(fileName))
  
  def loadBytes(f: File) = {
    val bos = new ByteArrayOutputStream
    val ba = new Array[Byte](2048)
    val is = new FileInputStream(f)
    def read {
      is.read(ba) match {
        case n if n < 0 =>
        case 0 => read
        case n => bos.write(ba, 0, n); read
      }
    }
    read
    bos.toByteArray
  }

  def write(fileName :String)(f: => Array[Byte]) = {
    val out = new FileOutputStream(fileName)
    out write f
    out.close
  }  

}