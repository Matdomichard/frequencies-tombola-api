package com.frequencies.tombola.service;

public interface TombolaConfigurationService {

    void setTicketsPerParticipant(int count);
    int getTicketsPerParticipant();

    void enableGuaranteeOneLotPerParticipant();
    void disableGuaranteeOneLotPerParticipant();
    boolean isGuaranteeEnabled();
}

