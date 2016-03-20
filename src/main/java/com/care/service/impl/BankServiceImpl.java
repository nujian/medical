package com.care.service.impl;

import com.care.domain.Bank;
import com.care.service.BankService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by nujian on 16/3/6.
 */
@Service
public class BankServiceImpl implements BankService {

    @Override
    public List<Bank> getBankList() {
        String query = "from Bank b order by b.id";
        return Bank.entityManager().createQuery(query,Bank.class).getResultList();
    }
}
