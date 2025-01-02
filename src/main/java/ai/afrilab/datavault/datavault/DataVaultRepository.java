package ai.afrilab.datavault.datavault;

import ai.afrilab.datavault.datavault.enums.Status;
import ai.afrilab.datavault.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface DataVaultRepository extends JpaRepository<DataVault, UUID> {
  @Query("select d from DataVault d where d.identifier = ?1")
  Optional<DataVault> findByIdentifier(String identifier);

  @Query("select (count(d) > 0) from DataVault d where d.identifier = ?1")
  boolean existsByIdentifier(String identifier);

  @Query("select d from DataVault d where d.createdBy = ?1 and d.status = ?2 order by d.createdDate DESC")
  Page<DataVault> findByCreatedByAndStatusOrderByCreatedDateDesc(User createdBy, Status status, Pageable pageable);

  @Query("select d from DataVault d where d.id = ?1 and d.createdBy = ?2")
  Optional<DataVault> findByIdAndCreatedBy(UUID id, User createdBy);

  @Query("select d from DataVault d where d.createdDate = ?1")
  Page<DataVault> findAllOrderByCreatedDate(Pageable pageable);

  @Query("select d from DataVault d where d.status = ?1 order by d.createdDate DESC")
  Page<DataVault> findByStatusOrderByCreatedDateDesc(Status status, Pageable pageable);

  @Query("select d from DataVault d where d.metaData.source = ?1 order by d.createdDate DESC")
  Page<DataVault> findByMetaDataSourceOrderByCreatedDateDesc(String name, Pageable pageable);
}