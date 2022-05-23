package com.example.dto;

import com.example.entity.Event;
import com.example.validation.DateConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventFormDTO implements TransformableToDomainObject<Event> {
    @DateConstraint
    private String date;
    @NotEmpty(message = "*Location must be filled")
    private String location;
    @NotEmpty(message = "*Description must be filled")
    private String description;

    @Override
    public Event toDomainObject() {
        return Event.builder()
                .id(null)
                .date(LocalDateTime.of(LocalDate.parse(date), LocalTime.MIN))
                .place(location)
                .description(description)
                .participants(null)
                .build();
    }
}
