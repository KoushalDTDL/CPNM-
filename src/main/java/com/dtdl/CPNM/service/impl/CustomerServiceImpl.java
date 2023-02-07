package com.dtdl.CPNM.service.impl;

import com.dtdl.CPNM.dao.CustomerDao;
import com.dtdl.CPNM.model.Customer;
import com.dtdl.CPNM.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Service("customerServiceImpl")
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private AsynchronousService asynchronousService;

    public void init(){

    }

    @Override
    public void disableNumbers(List<String> numbers) {
        Customer customer=new Customer(LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY:MM:dd::HH:mm:ss")),numbers);
        customerDao.save(customer);
        try{
            Timer timer=new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    asynchronousService.deleteNumbers(numbers,customer.getId());
                }
            },15*1000);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
}
