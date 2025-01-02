package ai.afrilab.datavault.datavault.metadata.dto;

import ai.afrilab.datavault.datavault.metadata.MetaData;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link MetaData}
 */
public record MetaDataDto(
    UUID id,
    String source,
    String location,
    String category,
    String author,
    String edition,
    String year
) implements Serializable {
}