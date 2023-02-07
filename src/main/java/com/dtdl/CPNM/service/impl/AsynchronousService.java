package com.dtdl.CPNM.service.impl;

import com.dtdl.CPNM.dao.CustomerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("asynchronousService")
@Async
public class AsynchronousService {

    @Autowired
    private CustomerDao customerDao;

    public void deleteNumbers(List<String> numbers,String id){
        System.out.println("Async method called : delete Numbers");
       customerDao.deleteById(id);
      //send this number to kafka queue
    }
}
