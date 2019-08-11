package com.example.demo.repository;

import com.example.demo.domain.CodeSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by zm on 2019/8/10.
 */
@Repository
public interface CodeSequenceRepository extends JpaRepository<CodeSequence, Long>, JpaSpecificationExecutor<CodeSequence> {

    //@Lock(LockModeType.PESSIMISTIC_READ)
    Optional<CodeSequence> findByName(String name);
}
