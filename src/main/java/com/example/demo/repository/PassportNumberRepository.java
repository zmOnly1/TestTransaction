package com.example.demo.repository;

import com.example.demo.domain.PassportNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by zm on 2019/8/11.
 */
@Repository
public interface PassportNumberRepository extends JpaRepository<PassportNumber, Long>, JpaSpecificationExecutor<PassportNumber> {

}
