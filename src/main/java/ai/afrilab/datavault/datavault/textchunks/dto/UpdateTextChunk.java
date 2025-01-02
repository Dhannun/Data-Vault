package ai.afrilab.datavault.datavault.textchunks.dto;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * DTO specifically designed for partial updates of text chunks.
 * Uses Optional to explicitly handle the presence/absence of fields,
 * allowing for flexible updates where only some fields need to be modified.
 */
public record UpdateTextChunk(
    String title,
    MultipartFile content,
    String transcriber
) implements Serializable {
}
