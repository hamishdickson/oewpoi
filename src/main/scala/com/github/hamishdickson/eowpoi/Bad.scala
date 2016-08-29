
// http://howtodoinjava.com/apache-commons/readingwriting-excel-files-in-java-poi-tutorial/
object Bad {
  def start = {
    val file = new FileInputStream(new File("example.xlsx"))
    readCells(file)
  }

  def readCells(file: FileInputStream) = {
    val workbook = new XSSFWorkbook(file)
    val sheet = wookbook.getSheetAt(0)
  }
}
