package be.vdab.personeel.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;



class JobTitelTest {

    private Validator validator;

    @BeforeEach
    void beforeEach() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void correcteJobTitel(){
        var titel = new JobTitel("test");
        assertThat(validator.validate(titel)).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void verkeerdeTitel(String naam){
          var titel = new JobTitel(naam);
          assertThat(validator.validate(titel)).isNotEmpty();
    }
}