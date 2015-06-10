package com.proconco.report.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the ReportId entity.
 */
public class ReportIdDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 1)
    @Max(value = 52)
    private Integer week;

    @NotNull
    @Min(value = 1970)
    @Max(value = 9999)
    private Integer year;

    private Long userId;

    private String userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }


    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReportIdDTO reportIdDTO = (ReportIdDTO) o;

        if ( ! Objects.equals(id, reportIdDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ReportIdDTO{" +
                "id=" + id +
                ", week='" + week + "'" +
                ", year='" + year + "'" +
                '}';
    }
}
