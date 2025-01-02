package ai.afrilab.datavault.datavault.textchunks.dto;

import ai.afrilab.datavault.datavault.textchunks.TextChunk;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * DTO for {@link TextChunk}
 */
public record AddTextChunk(
    String title,
    MultipartFile content,
    String transcriber
) implements Serializable {
}