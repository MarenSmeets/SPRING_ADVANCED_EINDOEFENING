package be.vdab.personeel.services;

import java.math.BigDecimal;

public interface WerknemerService {

    void opslag(long id, BigDecimal percentage);
}
