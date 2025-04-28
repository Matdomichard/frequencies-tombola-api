package com.frequencies.tombola.service.impl;

import com.frequencies.tombola.entity.TombolaConfiguration;
import com.frequencies.tombola.repository.TombolaConfigurationRepository;
import com.frequencies.tombola.service.TombolaConfigurationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TombolaConfigurationServiceImpl implements TombolaConfigurationService {

    private final TombolaConfigurationRepository repository;

    public TombolaConfigurationServiceImpl(TombolaConfigurationRepository repository) {
        this.repository = repository;
    }

    private TombolaConfiguration getConfig() {
        return repository.findAll().stream()
                .findFirst()
                .orElseGet(() -> repository.save(new TombolaConfiguration()));
    }

    @Override
    public void setTicketsPerParticipant(int count) {
        TombolaConfiguration config = getConfig();
        config.setTicketsPerParticipant(count);
        repository.save(config);
    }

    @Override
    public int getTicketsPerParticipant() {
        return getConfig().getTicketsPerParticipant();
    }

    @Override
    public void enableGuaranteeOneLotPerParticipant() {
        TombolaConfiguration config = getConfig();
        config.setGuaranteeOneLotPerParticipant(true);
        repository.save(config);
    }

    @Override
    public void disableGuaranteeOneLotPerParticipant() {
        TombolaConfiguration config = getConfig();
        config.setGuaranteeOneLotPerParticipant(false);
        repository.save(config);
    }

    @Override
    public boolean isGuaranteeEnabled() {
        return getConfig().getGuaranteeOneLotPerParticipant();
    }
}
