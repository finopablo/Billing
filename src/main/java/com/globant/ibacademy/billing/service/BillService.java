package com.globant.ibacademy.billing.service;

import com.globant.ibacademy.billing.dao.BillDao;
import com.globant.ibacademy.billing.exceptions.DataAccessException;
import com.globant.ibacademy.billing.exceptions.EntityNotFoundException;
import com.globant.ibacademy.billing.exceptions.ValidationException;
import com.globant.ibacademy.billing.model.Bill;

import java.util.List;
import java.util.Objects;

public class BillService {

    private final BillDao billDao;
    private final ProductService productService;

    public BillService(BillDao billDao, ProductService productService) {
        this.billDao = billDao;
        this.productService = productService;
    }

    public Bill saveBill(Bill bill) throws ValidationException {
        if (Objects.isNull(bill)) throw new IllegalArgumentException("Bill can't be null");
        if (validate(bill)) {
            return billDao.save(bill);
        } else {
            throw new ValidationException("Bill contains invalid Product data", null);
        }

    }

    private boolean validate(Bill bill) {
        return bill.items().stream().allMatch(i -> {
            try {
                return productService.findProductById(i.product().id()) != null;
            } catch (EntityNotFoundException e) {
                return false;
            }
        });
    }

    public List<Bill> findAll() throws DataAccessException {
        return billDao.findAll();
    }

    public List<Bill> findByCustomer(String customer) {
        if (Objects.isNull(customer)) throw new IllegalArgumentException("Customer can't be null");
        return billDao.findAll().stream().filter(o -> customer.equals(o.customer())).toList();
    }

}
