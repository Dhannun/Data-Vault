package ai.afrilab.datavault.datavault.metadata.dto;

import java.io.Serializable;

/**
 * DTO for {@link ai.afrilab.datavault.datavault.metadata.MetaData}
 */
public record CreateMetaData(
    String source,
    String location,
    String category,
    String author,
    String edition,
    String year
) implements Serializable {
}