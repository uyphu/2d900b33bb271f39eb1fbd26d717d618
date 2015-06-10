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
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A PlanningWeek.
 */
@Entity
@Table(name = "PLANNINGWEEK")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="planningweek")
public class PlanningWeek implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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

    @Column(name = "upd_tms")
    private String updTms;

    @OneToOne
    private PlanningWeekId planningWeekId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getUpdTms() {
        return updTms;
    }

    public void setUpdTms(String updTms) {
        this.updTms = updTms;
    }

    public PlanningWeekId getPlanningWeekId() {
        return planningWeekId;
    }

    public void setPlanningWeekId(PlanningWeekId planningWeekId) {
        this.planningWeekId = planningWeekId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PlanningWeek planningWeek = (PlanningWeek) o;

        if ( ! Objects.equals(id, planningWeek.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PlanningWeek{" +
                "id=" + id +
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
