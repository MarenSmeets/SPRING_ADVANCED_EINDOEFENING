package be.vdab.personeel.forms;

import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class OpslagForm {

    @NotNull
    @Positive
    @NumberFormat(pattern = "#,##0.00")
    private final BigDecimal bedrag;

    public OpslagForm(BigDecimal bedrag) {
        this.bedrag = bedrag;
    }

    public BigDecimal getBedrag() {
        return bedrag;
    }
}
