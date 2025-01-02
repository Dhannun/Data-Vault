package ai.afrilab.datavault.datavault.textchunks.dto;

import ai.afrilab.datavault.users.dto.AuditUserDto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link ai.afrilab.datavault.datavault.textchunks.TextChunk}
 */

public record NoContentTextChunkDto(
    UUID id,
    String title,
    String transcriber,
    String audioLink,
    String recorder,
    String translationLink,
    String translator,
    AuditUserDto createdBy,
    LocalDateTime createdDate,
    AuditUserDto lastModifiedBy,
    LocalDateTime lastModifiedDate
) implements Serializable {
}