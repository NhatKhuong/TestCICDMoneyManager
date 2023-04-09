package com.example.moneymanager2.util;

import com.example.moneymanager2.request.BaseSort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UtilService {

    public static Date atStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date setDateEndDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);

        Date d = cal.getTime();

        return d;
    }
    public static Pageable convertPageableAndSort(Integer pageNumber, Integer pageSize, List<BaseSort> sorts) {
        Sort sort = null;
        Pageable pageable;
        if (sorts != null && sorts.size() > 0) {
            for (BaseSort item : sorts) {
                if (!item.getAsc()) {
                    sort = (sort == null) ? Sort.by(item.getKey()).descending() : sort.and(Sort.by(item.getKey()).descending());
                } else {
                    sort = (sort == null) ? Sort.by(item.getKey()) : sort.and(Sort.by(item.getKey()));
                }
            }
            pageable = PageRequest.of(pageNumber, pageSize, sort);
        } else {
            pageable = PageRequest.of(pageNumber, pageSize);
        }
        return pageable;
    }
}
