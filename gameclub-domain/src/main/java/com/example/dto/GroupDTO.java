package com.example.dto;

import com.example.domain.Group;
import lombok.Data;

import java.util.List;

@Data
public class GroupDTO implements TransformableToDomainObject<Group> {
    private Long id;
    private String name;
    private Long admin;
    private List<Long> members;

    @Override
    public Group toDomainObject() {
        return Group.builder()
                .id(id)
                .name(name)
                .build();
    }
}
