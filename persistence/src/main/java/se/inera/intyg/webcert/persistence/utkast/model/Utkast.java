/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.inera.intyg.webcert.persistence.utkast.model;

import java.io.UnsupportedEncodingException;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;

/**
 * A draft of a certificate.
 *
 * @author marced
 *
 */
@Entity
@Table(name = "INTYG")
public class Utkast {

    private static final String UTF_8 = "UTF-8";

    /**
     * The unique id for this entity (UUID - not autogenerated).
     */
    @Id
    @Column(name = "INTYGS_ID")
    private String intygsId;

    @Column(name = "INTYGS_TYP")
    private String intygsTyp;

    @Column(name = "ENHETS_ID")
    private String enhetsId;

    @Column(name = "ENHETS_NAMN")
    private String enhetsNamn;

    @Column(name = "VARDGIVAR_ID")
    private String vardgivarId;

    @Column(name = "VARDGIVAR_NAMN")
    private String vardgivarNamn;

    @Column(name = "PATIENT_PERSONNUMMER")
    private String patientPersonnummer;

    @Column(name = "PATIENT_FORNAMN")
    private String patientFornamn;

    @Column(name = "PATIENT_MELLANNAMN")
    private String patientMellannamn;

    @Column(name = "PATIENT_EFTERNAMN")
    private String patientEfternamn;

    @Version
    private long version;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "hsaId", column = @Column(name = "SKAPAD_AV_HSAID")),
            @AttributeOverride(name = "namn", column = @Column(name = "SKAPAD_AV_NAMN")) })
    private VardpersonReferens skapadAv;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "hsaId", column = @Column(name = "SENAST_SPARAD_AV_HSAID")),
            @AttributeOverride(name = "namn", column = @Column(name = "SENAST_SPARAD_AV_NAMN")) })
    private VardpersonReferens senastSparadAv;

    @Column(name = "SENAST_SPARAD_DATUM")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime senastSparadDatum;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "MODEL")
    private byte[] model;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private UtkastStatus status;

    @Column(name = "VIDAREBEFORDRAD", columnDefinition = "TINYINT(1)")
    private Boolean vidarebefordrad = Boolean.FALSE;

    @Column(name = "SKICKAD_TILL_MOTTAGARE")
    private String skickadTillMottagare;

    @Column(name = "SKICKAD_TILL_MOTTAGARE_DATUM")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime skickadTillMottagareDatum;

    @Column(name = "ATERKALLAD_DATUM")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime aterkalladDatum;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Signatur signatur;

    @PrePersist
    void onPrePersist() {
        if (senastSparadDatum == null) {
            senastSparadDatum = new LocalDateTime();
        }
    }

    @PreUpdate
    void onPreUpdate() {
        senastSparadDatum = new LocalDateTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Utkast otherUtkast = (Utkast) o;

        if (intygsId != null ? !intygsId.equals(otherUtkast.intygsId) : otherUtkast.intygsId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return intygsId != null ? intygsId.hashCode() : 0;
    }

    public String getIntygsId() {
        return intygsId;
    }

    public void setIntygsId(String intygsId) {
        this.intygsId = intygsId;
    }

    public String getIntygsTyp() {
        return intygsTyp;
    }

    public void setIntygsTyp(String intygsTyp) {
        this.intygsTyp = intygsTyp;
    }

    public String getEnhetsId() {
        return enhetsId;
    }

    public void setEnhetsId(String enhetsId) {
        this.enhetsId = enhetsId;
    }

    public String getEnhetsNamn() {
        return enhetsNamn;
    }

    public void setEnhetsNamn(String enhetsNamn) {
        this.enhetsNamn = enhetsNamn;
    }

    public String getVardgivarId() {
        return vardgivarId;
    }

    public void setVardgivarId(String vardgivarId) {
        this.vardgivarId = vardgivarId;
    }

    public String getVardgivarNamn() {
        return vardgivarNamn;
    }

    public void setVardgivarNamn(String vardgivarNamn) {
        this.vardgivarNamn = vardgivarNamn;
    }

    public Personnummer getPatientPersonnummer() {
        return new Personnummer(patientPersonnummer);
    }

    public void setPatientPersonnummer(Personnummer patientPersonnummer) {
        this.patientPersonnummer = patientPersonnummer.getPersonnummer();
    }

    public String getPatientFornamn() {
        return patientFornamn;
    }

    public void setPatientFornamn(String patientFornamn) {
        this.patientFornamn = patientFornamn;
    }

    public String getPatientMellannamn() {
        return patientMellannamn;
    }

    public void setPatientMellannamn(String patientMellannamn) {
        this.patientMellannamn = patientMellannamn;
    }

    public String getPatientEfternamn() {
        return patientEfternamn;
    }

    public void setPatientEfternamn(String patientEfternamn) {
        this.patientEfternamn = patientEfternamn;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public VardpersonReferens getSkapadAv() {
        return skapadAv;
    }

    public void setSkapadAv(VardpersonReferens skapadAv) {
        this.skapadAv = skapadAv;
    }

    public VardpersonReferens getSenastSparadAv() {
        return senastSparadAv;
    }

    public void setSenastSparadAv(VardpersonReferens senastSparadAv) {
        this.senastSparadAv = senastSparadAv;
    }

    public String getModel() {
        return fromBytes(this.model);
    }

    public void setModel(String model) {
        this.model = toBytes(model);
    }

    public UtkastStatus getStatus() {
        return status;
    }

    public void setStatus(UtkastStatus status) {
        this.status = status;
    }

    public Boolean getVidarebefordrad() {
        return vidarebefordrad;
    }

    public void setVidarebefordrad(Boolean vidarebefordrad) {
        this.vidarebefordrad = vidarebefordrad;
    }

    public void setSenastSparadDatum(LocalDateTime senastSparadDatum) {
        this.senastSparadDatum = senastSparadDatum;
    }

    public LocalDateTime getSenastSparadDatum() {
        return senastSparadDatum;
    }

    public Signatur getSignatur() {
        return signatur;
    }

    public void setSignatur(Signatur signatur) {
        this.signatur = signatur;
    }

    public String getSkickadTillMottagare() {
        return skickadTillMottagare;
    }

    public void setSkickadTillMottagare(String skickadTillMottagare) {
        this.skickadTillMottagare = skickadTillMottagare;
    }

    public LocalDateTime getSkickadTillMottagareDatum() {
        return skickadTillMottagareDatum;
    }

    public void setSkickadTillMottagareDatum(LocalDateTime skickadTillMottagareDatum) {
        this.skickadTillMottagareDatum = skickadTillMottagareDatum;
    }

    public LocalDateTime getAterkalladDatum() {
        return aterkalladDatum;
    }

    public void setAterkalladDatum(LocalDateTime aterkalladDatum) {
        this.aterkalladDatum = aterkalladDatum;
    }

    private byte[] toBytes(String data) {
        if (data == null) {
            return new byte[0];
        }
        try {
            return data.getBytes(UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to convert String to bytes!", e);
        }
    }

    private String fromBytes(byte[] bytes) {
        try {
            return new String(bytes, UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to convert bytes to String!", e);
        }
    }
}
