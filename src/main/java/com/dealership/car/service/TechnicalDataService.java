package com.dealership.car.service;

import com.dealership.car.dynamic.DynamicFieldValue;
import com.dealership.car.model.TechnicalData;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TechnicalDataService {
    private final DynamicFieldValueService dynamicFieldValueService;

    public TechnicalDataService(DynamicFieldValueService dynamicFieldValueService) {
        this.dynamicFieldValueService = dynamicFieldValueService;
    }

    public Map<TechnicalData, List<DynamicFieldValue>> getDynamicFieldsForAllTechnicalData(List<TechnicalData> technicalDataList){
        Map<TechnicalData,List<DynamicFieldValue>>  technicalDataDynamicFieldsMap = new HashMap<>();
        for (TechnicalData technicalData : technicalDataList){
            List<DynamicFieldValue> dynamicFieldValueList =
                    dynamicFieldValueService.getAllDynamicValueForEntity(technicalData.getTechnicalId(),"TechnicalData");
            if (!dynamicFieldValueList.isEmpty()){
                technicalDataDynamicFieldsMap.put(technicalData,dynamicFieldValueList);
            } else {
                technicalDataDynamicFieldsMap.put(technicalData,new ArrayList<>());
            }
        }
        return technicalDataDynamicFieldsMap;
    }
}
