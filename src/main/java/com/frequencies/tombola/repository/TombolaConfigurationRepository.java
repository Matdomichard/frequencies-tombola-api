package com.frequencies.tombola.repository;

import com.frequencies.tombola.entity.TombolaConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TombolaConfigurationRepository extends JpaRepository<TombolaConfiguration, Long> {
}
