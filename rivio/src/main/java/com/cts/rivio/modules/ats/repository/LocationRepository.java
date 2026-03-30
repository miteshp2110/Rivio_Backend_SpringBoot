// LocationRepository.java
package com.cts.rivio.modules.ats.repository;
import com.cts.rivio.modules.company.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {}

