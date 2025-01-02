package ai.afrilab.datavault.datavault.dto;

import ai.afrilab.datavault.datavault.DataVault;
import ai.afrilab.datavault.datavault.metadata.dto.CreateMetaData;

import java.io.Serializable;

/**
 * DTO for {@link DataVault}
 */
public record CreateData(
    String identifier,
    String title,
    CreateMetaData metaData
) implements Serializable {
}