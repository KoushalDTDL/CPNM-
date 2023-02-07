package com.dtdl.CPNM.controller;

import com.dtdl.CPNM.dto.CustomerRequest;
import com.dtdl.CPNM.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping(value = "/disable/numbers",consumes = {"application/json"})
    public void disableNumbers(@RequestBody @Valid CustomerRequest customerRequest){
        customerService.disableNumbers(customerRequest.getNumbers());
    }


}
