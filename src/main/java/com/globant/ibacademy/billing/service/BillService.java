package com.globant.ibacademy.billing.service;

import com.globant.ibacademy.billing.dao.BillDao;
import com.globant.ibacademy.billing.exceptions.EntityAlreadyExistsException;
import com.globant.ibacademy.billing.exceptions.EntityNotFoundException;
import com.globant.ibacademy.billing.exceptions.ValidationException;
import com.globant.ibacademy.billing.model.Bill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class BillService {

    private final BillDao billDao;
    private final ProductService productService;

    public BillService(BillDao billDao, ProductService productService) {
        this.billDao = billDao;
        this.productService = productService;
    }

    public Bill saveBill(Bill bill) throws ValidationException {
        if (Objects.isNull(bill)) throw new IllegalArgumentException("Bill can't be null");
        if (billExists(bill.getNumber())) throw new EntityAlreadyExistsException(String.format("Bill number %s already exists", bill.getNumber()));
        if (validate(bill)) {
            return billDao.save(bill);
        } else {
            throw new ValidationException("Bill contains invalid Product data", null);
        }

    }

    private boolean validate(Bill bill) {
        return bill.getItems().stream().allMatch(i -> {
            try {
                return productService.findProductById(i.getProduct().getId()) != null;
            } catch (EntityNotFoundException e) {
                return false;
            }
        });
    }

    public List<Bill> findAll()  {
        return  billDao.findAll();
    }

    public List<Bill> findByCustomer(String customer) {
        if (Objects.isNull(customer)) throw new IllegalArgumentException("Customer can't be null");
        return billDao.findAll().stream().filter(o -> customer.equals(o.getCustomer())).toList();
    }

    public Bill findById(Integer id) {
        return  billDao.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Bill Id %s not found", id)));
    }

    public boolean billExists(Integer number) {
        return  billDao.findByNumber(number).isPresent();
    }
}
