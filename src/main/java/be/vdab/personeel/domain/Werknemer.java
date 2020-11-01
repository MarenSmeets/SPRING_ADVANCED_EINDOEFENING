package be.vdab.personeel.domain;

import be.vdab.personeel.constraints.Rijksregister;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "werknemers")
public class Werknemer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private String familienaam;

    @NotBlank
    private String voornaam;

    @NotNull @Email
    private String email;

    @Valid
    @ManyToOne
    @JoinColumn(name = "chefid")
    private Werknemer chef;

    @OneToMany(mappedBy = "chef")
    @OrderBy("familienaam, voornaam")
    private Set<@Valid Werknemer> ondergeschikten;

    @NotNull @Valid
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "jobtitelid")
    private JobTitel jobtitel;

    @NotNull @PositiveOrZero
    @NumberFormat(pattern = "#,##0.00")
    private BigDecimal salaris;

    @NotBlank
    private String paswoord;

    @NotNull @Past
    private LocalDate geboorte;

    @NotNull @Rijksregister
    private Long rijksregisternr;

    @Version
    private long versie;


    protected Werknemer(){}

    public Werknemer(String familienaam, String voornaam, String email, Werknemer chef, JobTitel jobtitel, BigDecimal salaris, String paswoord, LocalDate geboorte, Long rijksregisternr) {
        this.familienaam = familienaam;
        this.voornaam = voornaam;
        this.email = email;
        setChef(chef);
        this.ondergeschikten = new LinkedHashSet<>();
        setJobtitel(jobtitel);
        this.salaris = salaris;
        setPaswoord(paswoord);
        this.geboorte = geboorte;
        this.rijksregisternr = rijksregisternr;
    }

    public long getId() {
        return id;
    }

    public String getFamilienaam() {
        return familienaam;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public String getFullName(){
        return voornaam + " " + familienaam;
    }

    public String getEmail() {
        return email;
    }

    public Werknemer getChef() {
        return chef;
    }

    public Set<Werknemer> getOndergeschikten() {
        return Collections.unmodifiableSet(ondergeschikten);
    }

    public JobTitel getJobtitel() {
        return jobtitel;
    }

    public BigDecimal getSalaris() {
        return salaris;
    }

    public LocalDate getGeboorte() {
        return geboorte;
    }

    public Long getRijksregisternr() {
        return rijksregisternr;
    }

    public long getVersie() {
        return versie;
    }

    public void setChef(Werknemer chef) {
        this.chef = chef;
    }

    public void setJobtitel(JobTitel jobtitel){
        this.jobtitel = jobtitel;
    }

    private void setPaswoord(String paswoord){
        this.paswoord = new BCryptPasswordEncoder().encode(paswoord);
    }

//    public void opslagByPercentage(BigDecimal percentage){
//        if (percentage.compareTo(BigDecimal.ZERO) <= 0) {
//            throw new IllegalArgumentException("Percentage voor opslag moet groter zijn dan nul.");
//        }
//        var opslagFactor = BigDecimal.ONE.add(percentage.divide(BigDecimal.valueOf(100)));
//        salaris = salaris.multiply(opslagFactor, new MathContext(2, RoundingMode.HALF_UP));
//    }

    public void opslagByNumber(BigDecimal bedrag){
        if (bedrag.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Bedrag voor opslag moet groter zijn dan nul.");
        }
        salaris = salaris.add(bedrag);
    }

    public boolean addOndergeschikte(Werknemer ondergeschikte){
        if(ondergeschikte == null){
            throw new IllegalArgumentException("Werknemer mag niet NULL zijn.");
        }
        if(!this.equals(ondergeschikte.chef)){
            if(ondergeschikte.chef == null) {
                ondergeschikte.setChef(this);
            } else {
                var oudeChef = ondergeschikte.chef;
                ondergeschikte.setChef(this);
                oudeChef.removeOndergeschikte(ondergeschikte);
            }
        }
        return ondergeschikten.add(ondergeschikte);
    }

    public boolean removeOndergeschikte(Werknemer ondergeschikte){
        if(this.equals(ondergeschikte.chef)){
            throw new IllegalArgumentException("Wijs werknemer eerst toe aan nieuwe chef.");
        }
        return ondergeschikten.remove(ondergeschikte);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Werknemer)) return false;
        Werknemer werknemer = (Werknemer) o;
        return Objects.equals(rijksregisternr, werknemer.rijksregisternr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rijksregisternr);
    }
}
