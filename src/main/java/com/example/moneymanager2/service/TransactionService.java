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
import java.time.temporal.ChronoField;
import java.util.*;

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

    public List<Transaction> findAllByUserIdAndTypeAndTypeBasket(String userId,int type, int typeBasket){
        return transactionRepositoty.findAllByUserIdAndTypeAndTypeBasket(userId, type, typeBasket);
    }

    public List<Transaction> findAllByBasketId(String basketId) {
        return transactionRepositoty.findAllByBasketId(basketId);
    }

    public List<Transaction> findAllByUserIdAndBasketId(String userId, String basketId){
        return transactionRepositoty.findAllByUserIdAndBasketId(userId,basketId);
    }

//    public List<Transaction> findAllByUserIdAndCreateDateBetween(String userId, SearchTransactionFromDateToDate searchTransactionFromDateToDate){
//        return transactionRepositoty.findAllByUserIdAndCreateDateBetween(userId,searchTransactionFromDateToDate.getFromDate(),searchTransactionFromDateToDate.getToDate());
//    }

    public List<Transaction> findAllByUserIdAndTypeAndTypeBasketAndCreateDateBetween(String userId, SearchTransactionFromDateToDate searchTransactionFromDateToDate){
        if(Objects.isNull(searchTransactionFromDateToDate)){
            return transactionRepositoty.findAllByUserIdAndTypeBasketAndCreateDateBetween(userId,searchTransactionFromDateToDate.getTypeBasket(),searchTransactionFromDateToDate.getFromDate(),searchTransactionFromDateToDate.getToDate());
        } else {
            return transactionRepositoty.findAllByUserIdAndTypeAndTypeBasketAndCreateDateBetween(userId,searchTransactionFromDateToDate.getType(),searchTransactionFromDateToDate.getTypeBasket(),searchTransactionFromDateToDate.getFromDate(),searchTransactionFromDateToDate.getToDate());
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
                lst = transactionRepositoty.findAllByUserIdAndTypeAndTypeBasketAndCreateDateBetween(searchTransactionFromDateToDate.getUserId(),searchTransactionFromDateToDate.getType(),searchTransactionFromDateToDate.getTypeBasket(),dateStart,dateEnd);

            } else {
                lst = transactionRepositoty.findAllByUserIdAndTypeAndBasketIdAndCreateDateBetween(searchTransactionFromDateToDate.getUserId(),searchTransactionFromDateToDate.getType(),searchTransactionFromDateToDate.getBasketId(),dateStart,dateEnd);
            }

            Map<Integer,List<Transaction>> map = new HashMap<>();
            if(searchTransactionFromDateToDate.getIsDay()){
                for(int i = 1; i <= getNumberOfDaysInMonth(searchTransactionFromDateToDate.getYear(), searchTransactionFromDateToDate.getMonth()); i++){
                    double money = 0.0;
                    for (Transaction transaction : lst) {
                        if(transaction.getCreateDate().getDate() == i && transaction.getType() == searchTransactionFromDateToDate.getType()){
                            money = money + transaction.getMoneyTransaction();
                        }
                    }
                    lstResult.add(money);
                }
            } else if(searchTransactionFromDateToDate.getIsWeek()){
                Map<Integer,Double> mapWeek = new HashMap<>();
                List<Integer> weekInMonths = new ArrayList<>();
                weekInMonths.add(LocalDate.of(searchTransactionFromDateToDate.getYear(), searchTransactionFromDateToDate.getMonth(), 1).get(ChronoField.ALIGNED_WEEK_OF_YEAR));
                weekInMonths.add(LocalDate.of(searchTransactionFromDateToDate.getYear(), searchTransactionFromDateToDate.getMonth(), 8).get(ChronoField.ALIGNED_WEEK_OF_YEAR));
                weekInMonths.add(LocalDate.of(searchTransactionFromDateToDate.getYear(), searchTransactionFromDateToDate.getMonth(), 15).get(ChronoField.ALIGNED_WEEK_OF_YEAR));
                weekInMonths.add(LocalDate.of(searchTransactionFromDateToDate.getYear(), searchTransactionFromDateToDate.getMonth(), 22).get(ChronoField.ALIGNED_WEEK_OF_YEAR));
                Collections.sort(weekInMonths);
                weekInMonths.add(weekInMonths.get(weekInMonths.size()-1)+1);
                for (Transaction transaction : lst) {
                    int weekOfYear = getNumberWeekOfYear(transaction.getCreateDate());
                    if(!mapWeek.containsKey(weekOfYear)){
                        mapWeek.put(weekOfYear,transaction.getMoneyTransaction());

                    } else if(mapWeek.containsKey(weekOfYear)){
                        mapWeek.put(weekOfYear,mapWeek.get(weekOfYear) + transaction.getMoneyTransaction());
                    }
                }
                for (Integer numWeek : weekInMonths ) {
                    lstResult.add(Objects.isNull(mapWeek.get(numWeek)) ? 0.0 : mapWeek.get(numWeek));
                }
                lstResult.set(lstResult.size()-2,lstResult.get(lstResult.size()-1)+lstResult.get(lstResult.size()-2));
                lstResult.remove(lstResult.size()-1);
            }
        } else {
            LocalDate localDate = LocalDate.of(searchTransactionFromDateToDate.getYear(), 1,1);
            LocalDate localDateEnd = LocalDate.of(searchTransactionFromDateToDate.getYear(), 12,31);
            Date dateStart = UtilService.atStartOfDay(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            Date dateEnd = UtilService.setDateEndDay(Date.from(localDateEnd.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            List<Transaction> lst = new ArrayList<>();
            if(Objects.isNull(searchTransactionFromDateToDate.getBasketId())){
                lst = transactionRepositoty.findAllByUserIdAndTypeAndTypeBasketAndCreateDateBetween(searchTransactionFromDateToDate.getUserId(),searchTransactionFromDateToDate.getType(), searchTransactionFromDateToDate.getTypeBasket(), dateStart,dateEnd);

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

    public int getNumberWeekOfYear(Date date){
        LocalDate localDate = convertToLocalDateViaInstant(date);
        int weekOfYear = localDate.get(ChronoField.ALIGNED_WEEK_OF_YEAR);
        return weekOfYear;
    }
}
