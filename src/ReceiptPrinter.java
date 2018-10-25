import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReceiptPrinter {
  private String path;

  public ReceiptPrinter(String path) {
    this.path = path;
  }

  public void printReceipt(String message) {
    BufferedWriter writer = null;
    try {
      LocalDateTime moment = LocalDateTime.now();
      String timeStamp = moment.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
      File logFile = new File(path + timeStamp + ".txt");

      writer = new BufferedWriter(new FileWriter(logFile));
      writer.write("SuperShine Inc.\nReceipt printed on " + moment.toLocalDate().
          format(DateTimeFormatter.ofPattern("dd LLLL yyyy")));
      writer.write(", at " + moment.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
      writer.write("\n------------------------------------------------------------\n\n");
      writer.write(message);
      writer.write("\n\n------------------------------------------------------------\nThank you for using SuperShine services. Have a nice day!");
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        writer.close();
      } catch (Exception e) {
      }
    }
  }
}
