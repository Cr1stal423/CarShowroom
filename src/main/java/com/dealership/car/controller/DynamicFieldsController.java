package com.dealership.car.controller;

import com.dealership.car.DTO.DynamicFieldDto;
import com.dealership.car.dynamic.DynamicFieldValue;
import com.dealership.car.dynamic.FieldsMetadata;
import com.dealership.car.mapper.DynamicFieldMapper;
import com.dealership.car.repository.FieldMetadataRepository;
import com.dealership.car.service.DynamicFieldValueService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/dynamic-fields")
@AllArgsConstructor
public class DynamicFieldsController {

    private FieldMetadataRepository fieldMetadataRepository;
    @Autowired
    private DynamicFieldValueService dynamicFieldValueService;




    @GetMapping("/showForm")
    public String showForm(Model model, @RequestParam(value = "id")Integer id,HttpSession httpSession) {
        String entityType = dynamicFieldValueService.getEntityType(id);
        List<FieldsMetadata> fields = fieldMetadataRepository.findAll();
        if (id !=null && (entityType != null && !entityType.equals(""))){
            model.addAttribute("entityId", id);
            model.addAttribute("entityType", entityType);
        }
        httpSession.setAttribute("id",id);
        model.addAttribute("field", new FieldsMetadata());
        model.addAttribute("value", new DynamicFieldValue());
        model.addAttribute("fields", fields);
        return "dynamic-fields-form.html";
    }

    @GetMapping("/edit")
    public String editForm(Model model, @RequestParam("id")Integer id, HttpSession httpSession){
        DynamicFieldDto dynamicFieldDto = dynamicFieldValueService.setDataToDto(id);
        model.addAttribute("dynamicFieldDto", dynamicFieldDto);
        httpSession.setAttribute("dynamicValueId",id);
        return "dynamic-field-edit-form.html";
    }

    @PostMapping("/addField")
    public String addFields(@RequestParam String fieldName, @RequestParam String fieldType,HttpSession httpSession) {
        FieldsMetadata fieldsMetadata = new FieldsMetadata();
        fieldsMetadata.setFieldName(fieldName);
        fieldsMetadata.setFieldType(fieldType);
        dynamicFieldValueService.addFieldMetadata(fieldsMetadata);
        var id = httpSession.getAttribute("id");
        return "redirect:/dynamic-fields/showForm?id="+id;
    }

    @PostMapping("/addValue")
    public String addValue(@RequestParam Integer entityId,
                           @RequestParam String entityType,
                           @RequestParam Integer fieldId,
                           @RequestParam String value, HttpSession httpSession) {

        Optional<FieldsMetadata> optionalField = fieldMetadataRepository.findById(fieldId);
        if (optionalField.isPresent()) {
            DynamicFieldValue dynamicFieldValue = new DynamicFieldValue();
            dynamicFieldValue.setField(optionalField.get());
            dynamicFieldValue.setEntityId(entityId);
            dynamicFieldValue.setEntityType(entityType);
            dynamicFieldValue.setValue(value);


            dynamicFieldValueService.saveDynamicFieldValue(dynamicFieldValue);
        }
        var id = httpSession.getAttribute("id");
        return "redirect:/dynamic-fields/showForm?id=" + id;
    }
    @PostMapping("/update")
    public String updateDynamicField(@ModelAttribute("dynamicFieldDto") DynamicFieldDto dynamicFieldDto, HttpSession httpSession){
        Boolean isSaved = dynamicFieldValueService.saveDynamicFieldsValueFromDto(dynamicFieldDto);
        String redirect = "";
        var id = httpSession.getAttribute("dynamicValueId");
        if (isSaved){
            redirect = String.format("redirect:/dynamic-fields/edit?id=%s",id);
        } else {
            redirect = String.format("redirect:/dynamic-fields/edit?id=%s&error=1",id);
        }
        return redirect;
    }
    @GetMapping("/delete")
    public String deleteDynamicValue(@RequestParam("id") Integer id, HttpSession httpSession){
        Integer dynamicValueId = (Integer) httpSession.getAttribute("dynamicValueId");
        Boolean isDeleted = dynamicFieldValueService.deleteDynamicValue(dynamicValueId);
        String redirect = "";
        if (isDeleted){
            redirect = String.format("redirect:/product/showAllProduct");
        } else {
            redirect = String.format("redirect:/dynamic-fields/edit?id=%s&error=1",dynamicValueId);
        }
        return redirect;
    }
}
