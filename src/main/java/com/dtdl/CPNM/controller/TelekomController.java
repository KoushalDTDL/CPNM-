package com.dtdl.CPNM.controller;

import com.dtdl.CPNM.dto.TelekomRequest;
import com.dtdl.CPNM.service.TelekomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/telekom")
public class TelekomController {

    @Autowired
    private TelekomService telekomService;

    @PostMapping(value = "/disable/numbers",consumes = {"application/json"})
    public void disableNumbers(@RequestBody @Valid TelekomRequest telekomRequest){
        telekomService.disableNumbers(telekomRequest.getNumbers(), telekomRequest.getSwapNumber());
    }

    @PutMapping(value = "/enable/numbers")
    public void enableNumbers(@RequestBody @Valid TelekomRequest telekomRequest){
        telekomService.enableNumbers(telekomRequest.getNumbers());
    }


}
