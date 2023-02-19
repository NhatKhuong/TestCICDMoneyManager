package com.example.moneymanager2.service;

import com.example.moneymanager2.model.Basket;
import com.example.moneymanager2.model.Transaction;
import com.example.moneymanager2.model.User;
import com.example.moneymanager2.repository.TransactionRepositoty;
import com.example.moneymanager2.request.SearchTransactionFromDateToDate;
import com.example.moneymanager2.util.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepositoty transactionRepositoty;

    public boolean save(Transaction transaction){
        try{
            transactionRepositoty.save(transaction);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(Transaction transaction){
        try{
            transactionRepositoty.delete(transaction);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public Transaction findById(String id){
        Transaction transaction = transactionRepositoty.findById(id).orElse(null);
        return transaction;
    }

    public boolean update(Transaction transaction){
        try{
            transactionRepositoty.save(transaction);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public List<Transaction> findAll(){
        try{
            List<Transaction> users = transactionRepositoty.findAll();
            return users;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Transaction> findAllByUserIdAndType(String userId,int type){
        return transactionRepositoty.findAllByUserIdAndType(userId, type);
    }

    public List<Transaction> findAllByBasketId(String basketId) {
        return transactionRepositoty.findAllByBasketId(basketId);
    }

    public List<Transaction> findAllByUserIdAndBasketId(String userId, String basketId){
        return transactionRepositoty.findAllByUserIdAndBasketId(userId,basketId);
    }

    public List<Transaction> findAllByUserIdAndCreateDateBetween(String userId, SearchTransactionFromDateToDate searchTransactionFromDateToDate){
        return transactionRepositoty.findAllByUserIdAndCreateDateBetween(userId,searchTransactionFromDateToDate.getFromDate(),searchTransactionFromDateToDate.getToDate());
    }

    public List<Transaction> findAllByUserIdAndTypeAndCreateDateBetween(String userId, SearchTransactionFromDateToDate searchTransactionFromDateToDate){
        if(Objects.isNull(searchTransactionFromDateToDate)){
            return transactionRepositoty.findAllByUserIdAndCreateDateBetween(userId,searchTransactionFromDateToDate.getFromDate(),searchTransactionFromDateToDate.getToDate());
        } else {
            return transactionRepositoty.findAllByUserIdAndTypeAndCreateDateBetween(userId,searchTransactionFromDateToDate.getType(),searchTransactionFromDateToDate.getFromDate(),searchTransactionFromDateToDate.getToDate());
        }
    }

    public List<Double> getTotalTransactionIncomeByDateOfMonth(SearchTransactionFromDateToDate searchTransactionFromDateToDate){
        List<Double> lstResult = new ArrayList<>();
        if(!(searchTransactionFromDateToDate.getMonth() == 0)){
            LocalDate localDate = LocalDate.of(searchTransactionFromDateToDate.getYear(),searchTransactionFromDateToDate.getMonth(),1);
            Date dateStart = UtilService.atStartOfDay(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            LocalDate dateEndLocal = localDate.withDayOfMonth(localDate.getMonth().length(localDate.isLeapYear()));
            Date dateEnd = UtilService.setDateEndDay(Date.from(dateEndLocal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            List<Transaction> lst = new ArrayList<>();
            if(Objects.isNull(searchTransactionFromDateToDate.getBasketId())){
                lst = transactionRepositoty.findAllByUserIdAndTypeAndCreateDateBetween(searchTransactionFromDateToDate.getUserId(),searchTransactionFromDateToDate.getType(),dateStart,dateEnd);

            } else {
                lst = transactionRepositoty.findAllByUserIdAndTypeAndBasketIdAndCreateDateBetween(searchTransactionFromDateToDate.getUserId(),searchTransactionFromDateToDate.getType(),searchTransactionFromDateToDate.getBasketId(),dateStart,dateEnd);
            }

            for(int i = 1; i <= getNumberOfDaysInMonth(searchTransactionFromDateToDate.getYear(), searchTransactionFromDateToDate.getMonth()); i++){
                double money = 0.0;
                for (Transaction transaction : lst) {
                    if(transaction.getCreateDate().getDate() == i && transaction.getType() == searchTransactionFromDateToDate.getType()){
                        money = money + transaction.getMoneyTransaction();
                    }
                }
                lstResult.add(money);
            }
        } else {
            LocalDate localDate = LocalDate.of(searchTransactionFromDateToDate.getYear(), 1,1);
            LocalDate localDateEnd = LocalDate.of(searchTransactionFromDateToDate.getYear(), 12,31);
            Date dateStart = UtilService.atStartOfDay(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            Date dateEnd = UtilService.setDateEndDay(Date.from(localDateEnd.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            List<Transaction> lst = new ArrayList<>();
            if(Objects.isNull(searchTransactionFromDateToDate.getBasketId())){
                lst = transactionRepositoty.findAllByUserIdAndTypeAndCreateDateBetween(searchTransactionFromDateToDate.getUserId(),searchTransactionFromDateToDate.getType(),dateStart,dateEnd);

            } else {
                lst = transactionRepositoty.findAllByUserIdAndTypeAndBasketIdAndCreateDateBetween(searchTransactionFromDateToDate.getUserId(),searchTransactionFromDateToDate.getType(),searchTransactionFromDateToDate.getBasketId(),dateStart,dateEnd);
            }

            for(int i = 0; i < 12; i++){
                double money = 0.0;
                for (Transaction transaction : lst) {
                    if(transaction.getCreateDate().getMonth() == i && transaction.getType() == searchTransactionFromDateToDate.getType()){
                        money = money + transaction.getMoneyTransaction();
                    }
                }
                lstResult.add(money);
            }

        }
        return lstResult;

    }

    public static int getNumberOfDaysInMonth(int year,int month)
    {
        YearMonth yearMonthObject = YearMonth.of(year, month);
        int daysInMonth = yearMonthObject.lengthOfMonth();
        return daysInMonth;
    }

    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
