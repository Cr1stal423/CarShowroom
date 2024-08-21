package com.dealership.car.controller;

import com.dealership.car.dynamic.DynamicFieldValue;
import com.dealership.car.dynamic.FieldsMetadata;
import com.dealership.car.repository.FieldMetadataRepository;
import com.dealership.car.service.DynamicFieldValueService;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/dynamic-fields")
@AllArgsConstructor
public class DynamicFieldsController {

    private FieldMetadataRepository fieldMetadataRepository;

    private DynamicFieldValueService dynamicFieldValueService;

    @GetMapping("/showForm")
    public String showForm(Model model) {
        model.addAttribute("field", new FieldsMetadata());
        model.addAttribute("value", new DynamicFieldValue());
        return "dynamic-fields-form.html";
    }


    @PostMapping("/addField")
    public String addFields(@RequestParam String fieldName, @RequestParam String fieldType) {
        FieldsMetadata fieldsMetadata = new FieldsMetadata();
        fieldsMetadata.setFieldName(fieldName);
        fieldsMetadata.setFieldType(fieldType);
        dynamicFieldValueService.addFieldMetadata(fieldsMetadata);

        return "redirect:/dynamic-fields/showForm";
    }

    @PostMapping("/addValue")
    public String addValue(@RequestParam Integer entityId,
                           @RequestParam String entityType,
                           @RequestParam Integer fieldId,
                           @RequestParam String value) {

        Optional<FieldsMetadata> optionalField = fieldMetadataRepository.findById(fieldId);
        if (optionalField.isPresent()) {
            DynamicFieldValue dynamicFieldValue = new DynamicFieldValue();
            dynamicFieldValue.setField(optionalField.get());
            dynamicFieldValue.setEntityId(entityId);
            dynamicFieldValue.setEntityType(entityType);
            dynamicFieldValue.setValue(value);

            dynamicFieldValueService.saveDynamicFieldValue(dynamicFieldValue);
        }
        return "redirect:/dynamic-fields/showForm";
    }
}
