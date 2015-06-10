package com.proconco.report.web.rest.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.proconco.report.domain.util.CustomLocalDateSerializer;
import com.proconco.report.domain.util.ISO8601LocalDateDeserializer;
import org.joda.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Report entity.
 */
public class ReportDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 0)
    private BigDecimal amt;

    @NotNull
    private String marketRpt;

    private String monday;

    private String tuesday;

    private String wednesday;

    private String thursday;

    private String friday;

    private String saturday;

    private String sunday;

    @NotNull
    @Size(min = 1, max = 1)
    @Pattern(regexp = "[YN]")
    private String delFlag;

    private String crtUid;

    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    private LocalDate crtTms;

    private String updUid;

    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    private LocalDate updTms;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReportDTO reportDTO = (ReportDTO) o;

        if ( ! Objects.equals(id, reportDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ReportDTO{" +
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
