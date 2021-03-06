package be.vdab.personeel.services;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DataJpaTest
@Import(DefaultWerknemerService.class)
@Sql("/insertWerknemers.sql")

class DefaultWerknemerServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    private final DefaultWerknemerService service;
    private final EntityManager manager;


    DefaultWerknemerServiceTest(DefaultWerknemerService service, EntityManager manager) {
        this.service = service;
        this.manager = manager;
    }

    private long idVanTestWerknemer(){
        return super.jdbcTemplate.queryForObject("select id from werknemers where familienaam = 'test3'", Long.class);
    }

//    @Test
//    void opslagByPercentage() {
//        var id = idVanTestWerknemer();
//        service.opslagByPercentage(id, BigDecimal.TEN);
//        manager.flush();
//        assertThat(super.jdbcTemplate.queryForObject("select salaris from werknemers where id=?", BigDecimal.class, id)).isEqualByComparingTo("1100");
//    }

    @Test
    void opslagByNumber() {
        var id = idVanTestWerknemer();
        service.opslagByNumber(id, BigDecimal.TEN);
        manager.flush();
        assertThat(super.jdbcTemplate.queryForObject("select salaris from werknemers where id=?", BigDecimal.class, id)).isEqualByComparingTo("1010");
    }

    @Test
    void opslagByNumberMetNulFails() {
        var id = idVanTestWerknemer();
        assertThatIllegalArgumentException().isThrownBy(()->service.opslagByNumber(id, BigDecimal.ZERO));
    }

    @Test
    void opslagByNumberMetNegatiefGetalFails() {
        var id = idVanTestWerknemer();
        assertThatIllegalArgumentException().isThrownBy(()->service.opslagByNumber(id, BigDecimal.valueOf(-1)));
    }



}