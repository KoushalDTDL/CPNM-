package com.dtdl.CPNM.dao;

import com.dtdl.CPNM.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerDao extends MongoRepository<Customer,String> {
}
