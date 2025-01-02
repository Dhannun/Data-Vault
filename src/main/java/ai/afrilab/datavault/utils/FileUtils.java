package ai.afrilab.datavault.utils;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.ContentHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class FileUtils {

  private static final Tika tika = new Tika();

  public static String extractText(MultipartFile file) throws IOException, TikaException {
    try (InputStream stream = file.getInputStream()) {
      ContentHandler handler = new BodyContentHandler(-1); // -1 means no limit
      AutoDetectParser parser = new AutoDetectParser();
      parser.parse(stream, handler, new org.apache.tika.metadata.Metadata(), new ParseContext());
      return handler.toString();
    } catch (Exception e) {
      throw new TikaException("Error extracting text", e);
    }
  }

  public static boolean isValidTextFile(MultipartFile file) {
    return Objects.equals(file.getContentType(), "text/plain") ||
        Objects.equals(file.getContentType(), "application/pdf") ||
        Objects.equals(file.getContentType(), "application/msword");
  }
}