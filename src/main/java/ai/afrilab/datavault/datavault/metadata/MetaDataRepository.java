package ai.afrilab.datavault.datavault.metadata;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface MetaDataRepository extends JpaRepository<MetaData, UUID> {
  @Query("select distinct m.category from MetaData m order by m.category asc")
  String[] findDistinctCategories();
}