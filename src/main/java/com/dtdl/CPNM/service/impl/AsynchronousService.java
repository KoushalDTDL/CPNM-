package com.dtdl.CPNM.service.impl;

import com.dtdl.CPNM.dao.TelekomDao;
import com.dtdl.CPNM.model.Telekom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("asynchronousService")
@Async
public class AsynchronousService {

    @Autowired
    private TelekomDao telekomDao;

    public void deleteNumbersById(String id){
        System.out.println("Async method called : delete Numbers");
        Telekom telekom = telekomDao.findById(id).orElse(null);
        if(telekom !=null && telekom.getMobileNumbers()!=null && !telekom.getMobileNumbers().isEmpty()) {
            telekomDao.deleteById(id);
            //send this number to kafka queue    customer.getMobileNumbers
        }
    }
}
