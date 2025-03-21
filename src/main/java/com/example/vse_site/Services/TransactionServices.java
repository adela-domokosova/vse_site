package com.example.vse_site.Services;

import com.example.vse_site.Entity.Transaction;
import com.example.vse_site.Repository.TransactionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServices {

    @Autowired
    private TransactionRepository transactionRepository;

    public String getTransactionsJsonByUserId(Long userId) {
        List<Transaction> transOpt = transactionRepository.findByUserId(userId);
        return convertTransactionsToJson(transOpt);
    }

    private String convertTransactionsToJson(List<Transaction> transactions) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(transactions);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
