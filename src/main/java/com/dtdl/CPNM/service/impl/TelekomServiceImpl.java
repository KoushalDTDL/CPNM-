package com.dtdl.CPNM.service.impl;

import com.dtdl.CPNM.dao.TelekomDao;
import com.dtdl.CPNM.model.Telekom;
import com.dtdl.CPNM.service.TelekomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service("telekomServiceImpl")
@Transactional
public class TelekomServiceImpl implements TelekomService {

    @Autowired
    private TelekomDao telekomDao;

    @Autowired
    private AsynchronousService asynchronousService;

    @Value("${disable.number.delay.queue}")
    private Integer delay;

    Map<String,List<String>> telekomIdNumbersLookup=new ConcurrentHashMap<>();

    @Override
    public void disableNumbers(List<String> numbers,Boolean swapNumber) {
        String iv="xyz";  //different for every telekom
        List<String> encryptNumbers=hashWithSHA512(iv,numbers);
        if(swapNumber){
            //send encrypt numbers to kafka queue due to termination state
            System.out.println("encrypted numbers sent to kafka queue");
            return;
        }
        //save customer phone number for specific amount of time
        Telekom telekom =new Telekom(LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY:MM:dd::HH:mm:ss")),encryptNumbers);
        telekomDao.save(telekom);
        //schedule to fix time acc to configuration
        telekomIdNumbersLookup.putIfAbsent(telekom.getId(),encryptNumbers);
        try{
            Timer timer=new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    asynchronousService.deleteNumbersById(telekom.getId());
                }
            },delay*1000L);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    @Override
    public void enableNumbers(List<String> numbers) {
        String iv="xyz"; //different for every telekom
        List<String> encryptNumbers=hashWithSHA512(iv,numbers);
        String telekomId=null;
        for(Map.Entry<String,List<String>> telekomIdNumbersEntry:telekomIdNumbersLookup.entrySet()){
            for(String number:encryptNumbers){
                if (telekomIdNumbersEntry.getValue().contains(number)) {
                    telekomId = telekomIdNumbersEntry.getKey();
                    break;
                }
            }
            if(telekomId!=null)
                break;
        }
        if(telekomId!=null) {
            Telekom telekom = telekomDao.findById(telekomId).orElse(null);
            if (telekom != null) {
                List<String> mobileNumbers = telekom.getMobileNumbers();
                mobileNumbers.removeAll(encryptNumbers);
                telekom.setMobileNumbers(mobileNumbers);
                telekomDao.save(telekom);
            }
        }

    }

    public static List<String> hashWithSHA512(String iv, List<String> numbers) {
        List<String> encryptNumbers=new ArrayList<>();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            for(String number:numbers) {
                byte[] message = (iv + number).getBytes();
                byte[] hash = md.digest(message);
                StringBuilder hexString = new StringBuilder();
                for (byte b : hash) {
                    String hex = Integer.toHexString(0xff & b);
                    if (hex.length() == 1)
                        hexString.append('0');
                    hexString.append(hex);
                }
                encryptNumbers.add(hexString.toString());
            }
            return encryptNumbers;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
