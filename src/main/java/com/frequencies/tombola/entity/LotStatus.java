package com.frequencies.tombola.entity;

public enum LotStatus {
    UNASSIGNED,   // not yet assigned to any player
    ASSIGNED,     // assigned but not yet collected
    COLLECTED,    // picked up by the winner
    CANCELLED     // assignment or prize cancelled
}
