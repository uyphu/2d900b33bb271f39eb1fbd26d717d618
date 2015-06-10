package com.proconco.report.web.rest.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.proconco.report.domain.util.CustomLocalDateSerializer;
import com.proconco.report.domain.util.ISO8601LocalDateDeserializer;
import org.joda.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Position entity.
 */
public class PositionDTO implements Serializable {

    private Long id;

    @NotNull
    private String postName;

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

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
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

        PositionDTO positionDTO = (PositionDTO) o;

        if ( ! Objects.equals(id, positionDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PositionDTO{" +
                "id=" + id +
                ", postName='" + postName + "'" +
                ", delFlag='" + delFlag + "'" +
                ", crtUid='" + crtUid + "'" +
                ", crtTms='" + crtTms + "'" +
                ", updUid='" + updUid + "'" +
                ", updTms='" + updTms + "'" +
                '}';
    }
}
