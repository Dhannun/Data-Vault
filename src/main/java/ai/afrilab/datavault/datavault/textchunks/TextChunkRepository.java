package ai.afrilab.datavault.datavault.textchunks;

import ai.afrilab.datavault.datavault.DataVault;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface TextChunkRepository extends JpaRepository<TextChunk, UUID> {
  @Query("select t from TextChunk t where t.dataVault = ?1")
  Optional<TextChunk> findByDataVault(DataVault dataVault);

  @Query("select (count(t) > 0) from TextChunk t where t.dataVault = ?1")
  boolean existsByDataVault(DataVault dataVault);

  @Query("select t from TextChunk t where upper(t.title) = upper(?1)")
  Optional<TextChunk> findByTitleIgnoreCase(String title);

  @Query("select (count(t) > 0) from TextChunk t where upper(t.title) = upper(?1) and t.dataVault = ?2")
  boolean existsByTitleIgnoreCaseAndDataVault(String title, DataVault dataVault);

  @Query("select t from TextChunk t where upper(t.title) = upper(?1) and t.dataVault = ?2")
  Optional<TextChunk> findByTitleIgnoreCaseAndDataVault(String title, DataVault dataVault);

  @Query("select t from TextChunk t where t.id = ?1 and t.dataVault = ?2")
  Optional<TextChunk> findByIdAndDataVault(UUID id, DataVault dataVault);

  @Query("select t from TextChunk t where t.dataVault = ?1")
  Page<TextChunk> findByDataVault(DataVault dataVault, Pageable pageable);

}