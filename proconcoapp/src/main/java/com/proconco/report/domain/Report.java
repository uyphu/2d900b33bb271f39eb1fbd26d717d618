package com.proconco.report.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.proconco.report.domain.util.CustomLocalDateSerializer;
import com.proconco.report.domain.util.ISO8601LocalDateDeserializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Report.
 */
@Entity
@Table(name = "REPORT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="report")
public class Report implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Min(value = 0)
    @Column(name = "amt", precision=10, scale=2, nullable = false)
    private BigDecimal amt;

    @NotNull
    @Column(name = "market_rpt", nullable = false)
    private String marketRpt;

    @Column(name = "monday")
    private String monday;

    @Column(name = "tuesday")
    private String tuesday;

    @Column(name = "wednesday")
    private String wednesday;

    @Column(name = "thursday")
    private String thursday;

    @Column(name = "friday")
    private String friday;

    @Column(name = "saturday")
    private String saturday;

    @Column(name = "sunday")
    private String sunday;

    @NotNull
    @Size(min = 1, max = 1)
    @Pattern(regexp = "[YN]")
    @Column(name = "del_flag", length = 1, nullable = false)
    private String delFlag;

    @Column(name = "crt_uid")
    private String crtUid;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    @Column(name = "crt_tms", nullable = false)
    private LocalDate crtTms;

    @Column(name = "upd_uid")
    private String updUid;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    @Column(name = "upd_tms", nullable = false)
    private LocalDate updTms;

    @OneToOne
    private ReportId reportId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }

    public String getMarketRpt() {
        return marketRpt;
    }

    public void setMarketRpt(String marketRpt) {
        this.marketRpt = marketRpt;
    }

    public String getMonday() {
        return monday;
    }

    public void setMonday(String monday) {
        this.monday = monday;
    }

    public String getTuesday() {
        return tuesday;
    }

    public void setTuesday(String tuesday) {
        this.tuesday = tuesday;
    }

    public String getWednesday() {
        return wednesday;
    }

    public void setWednesday(String wednesday) {
        this.wednesday = wednesday;
    }

    public String getThursday() {
        return thursday;
    }

    public void setThursday(String thursday) {
        this.thursday = thursday;
    }

    public String getFriday() {
        return friday;
    }

    public void setFriday(String friday) {
        this.friday = friday;
    }

    public String getSaturday() {
        return saturday;
    }

    public void setSaturday(String saturday) {
        this.saturday = saturday;
    }

    public String getSunday() {
        return sunday;
    }

    public void setSunday(String sunday) {
        this.sunday = sunday;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getCrtUid() {
        return crtUid;
    }

    public void setCrtUid(String crtUid) {
        this.crtUid = crtUid;
    }

    public LocalDate getCrtTms() {
        return crtTms;
    }

    public void setCrtTms(LocalDate crtTms) {
        this.crtTms = crtTms;
    }

    public String getUpdUid() {
        return updUid;
    }

    public void setUpdUid(String updUid) {
        this.updUid = updUid;
    }

    public LocalDate getUpdTms() {
        return updTms;
    }

    public void setUpdTms(LocalDate updTms) {
        this.updTms = updTms;
    }

    public ReportId getReportId() {
        return reportId;
    }

    public void setReportId(ReportId reportId) {
        this.reportId = reportId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Report report = (Report) o;

        if ( ! Objects.equals(id, report.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", amt='" + amt + "'" +
                ", marketRpt='" + marketRpt + "'" +
                ", monday='" + monday + "'" +
                ", tuesday='" + tuesday + "'" +
                ", wednesday='" + wednesday + "'" +
                ", thursday='" + thursday + "'" +
                ", friday='" + friday + "'" +
                ", saturday='" + saturday + "'" +
                ", sunday='" + sunday + "'" +
                ", delFlag='" + delFlag + "'" +
                ", crtUid='" + crtUid + "'" +
                ", crtTms='" + crtTms + "'" +
                ", updUid='" + updUid + "'" +
                ", updTms='" + updTms + "'" +
                '}';
    }
}
