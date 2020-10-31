package be.vdab.personeel.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

class WerknemerTest {

    private Validator validator;

    private Werknemer chef1;
    private Werknemer chef2;
    private Werknemer werknemer1;
    private Werknemer werknemer2;
    private JobTitel titel1 = new JobTitel("titel1");
    private JobTitel titel2 = new JobTitel("titel2");
    private JobTitel titel3 = new JobTitel("titel3");
    private final static BigDecimal SALARIS = BigDecimal.valueOf(1000);

    @BeforeEach
    void beforeEach(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        chef1 = new Werknemer("chef1", "null", "test@test.com", null, titel1, SALARIS, "zorro", LocalDate.of(1965,01,01), 65010100180L);
        chef2 = new Werknemer("chef2", "chef1", "test@test.com", chef1, titel2, SALARIS, "zorro", LocalDate.of(1966,02,02), 66020200277L);
        werknemer1 = new Werknemer("wn1", "chef2", "test@test.com", chef2, titel3, SALARIS, "zorro", LocalDate.of(1967,03,03), 67030300176L);
        werknemer2 = new Werknemer("wn2", "chef2", "test@test.com", chef2, titel3, SALARIS, "zorro", LocalDate.of(1968,04,04), 68040400273L);

        chef2.addOndergeschikte(werknemer1);
        chef2.addOndergeschikte(werknemer2);
    }

    @Test
    void opslag(){
        werknemer1.opslag(BigDecimal.TEN);
        assertThat(werknemer1.getSalaris()).isEqualByComparingTo("1100");
    }

    @Test
    void opslagMetNullFails(){
        assertThatNullPointerException().isThrownBy(()->werknemer1.opslag(null));
    }

    @Test
    void opslagMet0Fails(){
        assertThatIllegalArgumentException().isThrownBy(()->werknemer1.opslag(BigDecimal.ZERO));
    }

    @Test
    void opslagMetNegatiefGetalFails(){
        assertThatIllegalArgumentException().isThrownBy(()->werknemer1.opslag(BigDecimal.valueOf(-1)));
    }

    @Test
    void eenChefKanMeerdereOndergeschiktenHebben(){
        assertThat(chef2.getOndergeschikten()).containsOnly(werknemer1, werknemer2);
        assertThat(werknemer1.getChef()).isEqualTo(chef2);
        assertThat(werknemer2.getChef()).isEqualTo(chef2);
    }

    @Test
    void ondergeschikteVerwijderen(){
        werknemer1.setChef(chef1);
        chef2.removeOndergeschikte(werknemer1);
    }

    @Test
    void ondergeschikteVerwijderenAlsChefVanWerknemerNietEerstGewijzigdFails(){
        assertThatIllegalArgumentException().isThrownBy(()->chef2.removeOndergeschikte(werknemer2));
    }

    @Test
    void ondergeschikteToevoegen(){
        assertThat(chef1.addOndergeschikte(werknemer2)).isTrue();
        assertThat(chef2.getOndergeschikten()).doesNotContain(werknemer2);
    }

    @Test
    void ondergeschikteNullToevoegenFails(){
        assertThatIllegalArgumentException().isThrownBy(()->chef1.addOndergeschikte(null));
    }

    @Test
    void equalsOpBasisVanRijksregisterNr(){
        // werknemer3 heeft hetzelfde rijksregisternummer als werknemer2
        var werknemer3 = new Werknemer("wn3", "chef2", "test5@test.com", chef2, titel2, SALARIS, "zorro", LocalDate.of(1965,01,01), 68040400273L);
        assertThat(werknemer3.equals(werknemer2)).isTrue();
        assertThat(werknemer3.equals(werknemer1)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(longs = {93051822361L})
    void correctRijksregisterNr(long rijksRegNr){
        var testWerknemer = new Werknemer("test", "test", "test@test.com", chef2, titel3, SALARIS, "test", LocalDate.of(1965,01,01), rijksRegNr);
        assertThat(validator.validate(testWerknemer)).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(longs = {93051822360L})
    void verkeerdRijksregisterNr(long rijksRegNr){
          var testWerknemer = new Werknemer("test", "test", "test@test.com", chef2, titel3, SALARIS, "test", LocalDate.of(1965,01,01), rijksRegNr);
          assertThat(validator.validate(testWerknemer)).isNotEmpty();
    }
}