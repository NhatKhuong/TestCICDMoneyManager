package com.example.moneymanager2.service;

import com.example.moneymanager2.model.Basket;
import com.example.moneymanager2.model.Transaction;
import com.example.moneymanager2.model.User;
import com.example.moneymanager2.repository.BasketRepository;
import com.example.moneymanager2.repository.TransactionRepositoty;
import com.example.moneymanager2.request.SearchTransactionFromDateToDate;
import com.example.moneymanager2.util.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.ChronoField;
import java.util.*;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepositoty transactionRepositoty;
    @Autowired
    private BasketRepository basketRepository;

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

    public List<Transaction> findAllByUserIdAndTypeAndTypeBasketAndCreateDateBetween(SearchTransactionFromDateToDate request){
        Pageable pageable = UtilService.convertPageableAndSort(request.getPageNumber(), request.getPageSize(), request.getSort());
        LocalDate localDate = LocalDate.of(request.getYear(),request.getMonth(),1);
        Date dateStart = UtilService.atStartOfDay(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        LocalDate dateEndLocal = localDate.withDayOfMonth(localDate.getMonth().length(localDate.isLeapYear()));
        Date dateEnd = UtilService.setDateEndDay(Date.from(dateEndLocal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        if(Objects.isNull(request.getBasketId())){
            if(Objects.isNull(request.getType())){
                return transactionRepositoty.findAllByUserIdAndTypeInAndTypeBasketAndCreateDateBetween(request.getUserId(), Arrays.asList(1,-1), request.getTypeBasket(),dateStart, dateEnd, pageable);
            } else {
                return transactionRepositoty.findAllByUserIdAndTypeInAndTypeBasketAndCreateDateBetween(request.getUserId(), Arrays.asList(request.getType()), request.getTypeBasket(),dateStart, dateEnd, pageable);
            }
        } else {
            if(Objects.isNull(request.getType())){
                return transactionRepositoty.findAllByUserIdAndTypeInAndBasketIdAndCreateDateBetween(request.getUserId(), Arrays.asList(1,-1), request.getBasketId(),dateStart, dateEnd, pageable);
            } else {
                return transactionRepositoty.findAllByUserIdAndTypeInAndBasketIdAndCreateDateBetween(request.getUserId(), Arrays.asList(request.getType()), request.getBasketId(),dateStart, dateEnd, pageable);
            }
        }
    }

    public List<Transaction> findAllByBasketId(String basketId) {
        return transactionRepositoty.findAllByBasketId(basketId);
    }

    public List<Transaction> findAllByUserIdAndBasketId(String userId, String basketId){
        return transactionRepositoty.findAllByUserIdAndBasketId(userId,basketId);
    }


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
                LocalDate dateSta = LocalDate.of(searchTransactionFromDateToDate.getYear(), searchTransactionFromDateToDate.getMonth(),1);
                LocalDateTime dateStartOfWeek1 = LocalDate.of(searchTransactionFromDateToDate.getYear(), searchTransactionFromDateToDate.getMonth(),1).atStartOfDay();
                LocalDateTime dateEndOfWeek1 = LocalTime.MAX.atDate(LocalDate.of(searchTransactionFromDateToDate.getYear(), searchTransactionFromDateToDate.getMonth(),7));
                LocalDateTime dateStartOfWeek2 = LocalDate.of(searchTransactionFromDateToDate.getYear(), searchTransactionFromDateToDate.getMonth(),8).atStartOfDay();
                LocalDateTime dateEndOfWeek2 = LocalTime.MAX.atDate(LocalDate.of(searchTransactionFromDateToDate.getYear(), searchTransactionFromDateToDate.getMonth(),14));
                LocalDateTime dateStartOfWeek3 = LocalDate.of(searchTransactionFromDateToDate.getYear(), searchTransactionFromDateToDate.getMonth(),15).atStartOfDay();
                LocalDateTime dateEndOfWeek3 = LocalTime.MAX.atDate(LocalDate.of(searchTransactionFromDateToDate.getYear(), searchTransactionFromDateToDate.getMonth(),21));
                LocalDateTime dateStartOfWeek4 = LocalDate.of(searchTransactionFromDateToDate.getYear(), searchTransactionFromDateToDate.getMonth(),22).atStartOfDay();
                LocalDate lastDayOfMonthDate  = dateSta.withDayOfMonth(
                        dateSta.getMonth().length(dateSta.isLeapYear()));
                LocalDateTime dateEndOfWeek4 = LocalTime.MAX.atDate(lastDayOfMonthDate);
                lstResult = Arrays.asList(0.0,0.0,0.0,0.0);
                for (Transaction transaction : lst) {
                    LocalDateTime createDate = convertToLocalDateTimeViaInstant(transaction.getCreateDate());
                    boolean g = createDate.isAfter(dateStartOfWeek2);
                    boolean h = createDate.isBefore(dateEndOfWeek2);
                    if(createDate.isAfter(dateStartOfWeek1) && createDate.isBefore(dateEndOfWeek1)){
                        lstResult.set(0,lstResult.get(0)+transaction.getMoneyTransaction());
                    } else if(createDate.isAfter(dateStartOfWeek2) && createDate.isBefore(dateEndOfWeek2)){
                        lstResult.set(1,lstResult.get(1)+transaction.getMoneyTransaction());
                    } else if(createDate.isAfter(dateStartOfWeek3) && createDate.isBefore(dateEndOfWeek3)){
                        lstResult.set(2,lstResult.get(2)+transaction.getMoneyTransaction());
                    } else if(createDate.isAfter(dateStartOfWeek4) && createDate.isBefore(dateEndOfWeek4)){
                        lstResult.set(3,lstResult.get(3)+transaction.getMoneyTransaction());
                    }
                }
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

    public LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
    public void updateTransaction(){
        List<Transaction> lst = transactionRepositoty.findAll();
        for (Transaction item : lst){
            Basket basket = basketRepository.findById(item.getBasketId()).get();
            if(!Objects.isNull(basket)){
                item.setNameBasket(basket.getName());
                transactionRepositoty.save(item);
            }
        }
    }
}
