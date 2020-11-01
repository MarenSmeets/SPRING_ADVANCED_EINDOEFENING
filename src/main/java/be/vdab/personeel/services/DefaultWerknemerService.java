package be.vdab.personeel.services;


import be.vdab.personeel.exceptions.WerknemerNietGevondenException;
import be.vdab.personeel.repositories.WerknemerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class DefaultWerknemerService implements WerknemerService {

    private final WerknemerRepository werknemerRepository;

    public DefaultWerknemerService(WerknemerRepository werknemerRepository) {
        this.werknemerRepository = werknemerRepository;
    }

//    @Override
//    public void opslagByPercentage(long id, BigDecimal percentage) {
//        werknemerRepository
//                .findById(id)
//                .orElseThrow(()-> new WerknemerNietGevondenException("Werknemer niet gevonden"))
//                .opslagByPercentage(percentage);
//    }

    @Override
    public void opslagByNumber(long id, BigDecimal bedrag) {
        werknemerRepository
                .findById(id)
                .orElseThrow(()-> new WerknemerNietGevondenException())
                .opslagByNumber(bedrag);
    }


}
