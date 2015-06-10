package com.proconco.report.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A PlanningWeekId.
 */
@Entity
@Table(name = "PLANNINGWEEKID")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="planningweekid")
public class PlanningWeekId implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Min(value = 1)
    @Max(value = 52)
    @Column(name = "week", nullable = false)
    private Integer week;

    @NotNull
    @Min(value = 1970)
    @Max(value = 9999)
    @Column(name = "year", nullable = false)
    private Integer year;

    @ManyToOne
    private User user;

    @OneToOne
    private PlanningWeek planningWeek;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PlanningWeek getPlanningWeek() {
        return planningWeek;
    }

    public void setPlanningWeek(PlanningWeek planningWeek) {
        this.planningWeek = planningWeek;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PlanningWeekId planningWeekId = (PlanningWeekId) o;

        if ( ! Objects.equals(id, planningWeekId.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PlanningWeekId{" +
                "id=" + id +
                ", week='" + week + "'" +
                ", year='" + year + "'" +
                '}';
    }
}
