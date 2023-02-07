package com.dtdl.CPNM.dto;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class CustomerRequest {

    @NotEmpty
    private List<String> numbers;

    public List<String> getNumbers() {
        return numbers;
    }
}
