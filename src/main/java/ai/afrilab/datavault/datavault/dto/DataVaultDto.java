package ai.afrilab.datavault.datavault.dto;

import ai.afrilab.datavault.datavault.enums.Status;
import ai.afrilab.datavault.datavault.metadata.dto.MetaDataDto;
import ai.afrilab.datavault.users.dto.AuditUserDto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link ai.afrilab.datavault.datavault.DataVault}
 */
public record DataVaultDto(
    UUID id,
    String identifier,
    String title,
    Status status,
    String audioSummary,
    String fileLink,
    MetaDataDto metaData,
    AuditUserDto createdBy,
    LocalDateTime createdDate,
    AuditUserDto lastModifiedBy,
    LocalDateTime lastModifiedDate
) implements Serializable {
}