package ai.afrilab.datavault.datavault.textchunks.dto;

import java.io.Serializable;

/**
 * DTO for {@link ai.afrilab.datavault.datavault.textchunks.TextChunk}
 */
public record TextChunkContentDto(String content) implements Serializable {
}