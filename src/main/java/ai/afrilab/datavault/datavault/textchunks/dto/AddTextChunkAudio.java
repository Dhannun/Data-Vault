package ai.afrilab.datavault.datavault.textchunks.dto;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * DTO for {@link ai.afrilab.datavault.datavault.textchunks.TextChunk}
 */
public record AddTextChunkAudio(MultipartFile audio, String recorder) implements Serializable {
  }