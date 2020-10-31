package be.vdab.personeel.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RijksregisterValidator implements ConstraintValidator<Rijksregister,Long> {

    @Override
    public void initialize(Rijksregister constraintAnnotation) {
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext constraintValidatorContext) {
        if(value == null) {
            return true;
        }
        var eersteCijfer = value / 10_000_000_000L;
        var eerste9Cijfers = value / 100;
        var controleGetal = value % 100;

        if(eersteCijfer == 0){
            eerste9Cijfers+= 2_000_000_000;
        }

        return 97 - eerste9Cijfers % 97 == controleGetal;
    }
}
