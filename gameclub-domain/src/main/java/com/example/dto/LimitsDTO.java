package com.example.dto;

import com.example.domain.Limits;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LimitsDTO implements TransformableToDomainObject<Limits> {
    private Integer from;
    private Integer to;

    @Override
    public Limits toDomainObject() {
        return new Limits(from, to);
    }
}
