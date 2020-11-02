package be.vdab.personeel.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Entity
@Table(name = "jobtitels")
public class JobTitel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private String naam;

    @Version
    private long versie;

    protected JobTitel(){}

    public JobTitel(String naam){
        this.naam = naam;
    }

    public long getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }

    public long getVersie() {
        return versie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JobTitel)) return false;
        JobTitel jobTitel = (JobTitel) o;
        return Objects.equals(naam.toUpperCase(), jobTitel.naam.toUpperCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(naam.toUpperCase());
    }

    @Override
    public String toString() {
        return naam;
    }
}
