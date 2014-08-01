import java.io.PrintWriter
import scala.io.Source

object makeexe {
  def align(size: Int) {
    var align = ""
    for (i <- 0 to size) {
      align += "\0"
    }
    align
  }

  def main(args: Array[String]) {
    val out = new PrintWriter("test.exe")

    var dosh = Array("MZ", 90, 3, 4, 0xffff, 0xb8, 0x40, 0x80)
    dosh.foreach( { i => out.print(i) })
    var dos_stub = Array( 0x0e, 0x1f, 0xba, 0x0e, 0x00, 0xb4, 0x09, 0xcd, 0x21, 0xb8, 0x01, 0x4c, 0xcd, 0x21 )
    dos_stub.foreach( { i => out.print(i) })

    out.print("This program cannot be run in DOS mode.\r\r\n$")

    out.print(align(8))

    var nthOptionalHeader = Array(0x010b, 0x0a, 0x0200, 0x1000, 0x1000, 0x2000, 0x400000, 0x1000, 0x0200, 5, 1, 5, 1, 0x3000, 0x0200, 3, 0x100000, 0x001000, 0x100000, 0x001000, 16, 0x00002000, 0x00001000)
    var nthFileHeader = Array("PE", 0x014c, 2, 0x4da65f9b, nthOptionalHeader.length, 0x0102)
    nthFileHeader.foreach( { i => out.print(i) })
    nthOptionalHeader.foreach( { i => out.print(i) })
    var sects = Array(".text", 0x34, 0x1000, 0x0200, 0x0200, 0x60000020, ".idata", 0x54, 0x2000, 0x0200, 0x0400, 0xc0300040)
    sects.foreach( { i => out.print(i) })
    out.print(align(nthOptionalHeader(8)))
    var text = Array( 0x6a, 0x41, 0xff, 0x15, 0x30, 0x20, 0x40, 0x00, 0x58, 0xc3 )
    text.foreach( { i => out.print(i) })

    out.print(align(nthOptionalHeader(8)))

    var idt = Array(0x2028, 0x2048, 0x2030)
    idt.foreach( { i => out.print(i) })

    var ilt = Array(0x2028, 0)
    ilt.foreach( { i => out.print(i) })

    var iat = Array(0x2028, 0)
    iat.foreach( { i => out.print(i) })

    out.print(0)
    out.print("putchar")
    out.print(0)

    out.print(0x2000)

    out.print("msvcrt.dll")

    out.print(align(nthOptionalHeader(8)))

    out.close
  }
}
