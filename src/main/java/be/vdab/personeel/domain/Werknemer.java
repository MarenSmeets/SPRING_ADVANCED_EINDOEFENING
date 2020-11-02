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

    // many employees can have the same chef
    @Valid
    @ManyToOne
    @JoinColumn(name = "chefid")
    private Werknemer chef;

    // 1 chef can have zero or more subordinates
    @OneToMany(mappedBy = "chef")
    @OrderBy("familienaam, voornaam")
    private Set<@Valid Werknemer> ondergeschikten;

    // many employees can have the same job title
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

    // custom validation annotation
    @NotNull @Rijksregister
    private Long rijksregisternr;

    @Version
    private long versie;

    protected Werknemer(){}

    public Werknemer(String familienaam, String voornaam, String email,
                     Werknemer chef, JobTitel jobtitel, BigDecimal salaris,
                     String paswoord, LocalDate geboorte, Long rijksregisternr) {
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

    // an employee can get a different chef
    public void setChef(Werknemer chef) {
        this.chef = chef;
    }

    // an employee can get a different title
    public void setJobtitel(JobTitel jobtitel){
        this.jobtitel = jobtitel;
    }

    // paswoord setting is private method, called upon only by the constructor --> hide which encryption is being used
    private void setPaswoord(String paswoord){
        this.paswoord = new BCryptPasswordEncoder().encode(paswoord);
    }

    // unused method for giving a raise by percentage rather than a fixed number
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

    // since an employee can get a new chef, a chef obviously can get a new subordinate
    public boolean addOndergeschikte(Werknemer ondergeschikte){
        if(ondergeschikte == null){
            throw new IllegalArgumentException("Werknemer mag niet NULL zijn.");
        }
        //if the subordinate's chef does not match the current employee (new chef)
        // first check if subordinate has a chef or not (f.i. it could be a new hire, in that case chef might be null)
            // if not (chef == null) --> set current employee as the chef : subordinate.setChef(this)
            // if yes -->
                // 1. get the previous chef of subordinate
                // 2. set the current employee as the new chef of subordinate
                // 3. remove subordinate from the collection of subordinates of previous chef
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

    // an employee can not be removed form a collection of subordinates, without first being assigned to a new chef (or in case he gets fired, then subordinate.setChef(null))
    public boolean removeOndergeschikte(Werknemer ondergeschikte){
        if(this.equals(ondergeschikte.chef)){
            throw new IllegalArgumentException("Wijs werknemer eerst toe aan nieuwe chef.");
        }
        return ondergeschikten.remove(ondergeschikte);
    }

    // equality based on national registration number, which is unique + has a custom annotation to check whether it's valid
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

    // toString returns the full name of the employee and the national registration number in its most common format
    @Override
    public String toString(){
        var rijksregFormat = new StringBuilder(String.valueOf(rijksregisternr));
        rijksregFormat.insert(2,'.');
        rijksregFormat.insert(5,'.');
        rijksregFormat.insert(8,'-');
        rijksregFormat.insert(12, '.');
        return getFullName() + " ; " + rijksregFormat;
    }
}
