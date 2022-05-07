package com.example.repository;

import com.example.entity.JoinRequest;
import com.example.entity.JoinRequestId;
import org.springframework.data.repository.CrudRepository;

public interface JoinRequestRepository extends CrudRepository<JoinRequest, JoinRequestId> {

}
