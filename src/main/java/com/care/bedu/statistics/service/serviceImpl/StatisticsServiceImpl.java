package com.care.bedu.statistics.service.serviceImpl;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.care.bedu.statistics.dao.StatisticsDAO;
import com.care.bedu.statistics.service.StatisticsService;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Override
    public List<StatisticsDAO> getStatistics() {
        List<StatisticsDAO> list = new ArrayList<StatisticsDAO>();

        return list;
    }



}