package com.infor.assignment.carrentalservice.model.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DateRange {
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm")
    @JsonProperty(value = "from")
    private LocalDateTime from;
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm")
    @JsonProperty(value = "to")
    private LocalDateTime to;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateRange dateRange = (DateRange) o;
        return Objects.equals(from, dateRange.from) && Objects.equals(to, dateRange.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}
