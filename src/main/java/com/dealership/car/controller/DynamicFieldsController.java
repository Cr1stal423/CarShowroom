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

/**
 * Controller for managing dynamic fields in an entity.
 * It allows displaying forms for dynamic fields, editing, adding, updating, and deleting dynamic fields and their values.
 */
@Controller
@RequestMapping("/dynamic-fields")
@AllArgsConstructor
public class DynamicFieldsController {

    private FieldMetadataRepository fieldMetadataRepository;
    @Autowired
    private DynamicFieldValueService dynamicFieldValueService;




    /**
     * Displays the form for dynamic fields.
     *
     * @param id the ID of the entity
     * @param httpSession the current HTTP session
     * @param model the model to add attributes to
     * @return the name of the HTML template to render
     */
    @GetMapping("/showForm")
    public String showForm(@RequestParam(value = "id")Integer id,HttpSession httpSession,Model model) {
        String entityType = dynamicFieldValueService.getEntityType(id, (String) httpSession.getAttribute("entityType"));
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

    /**
     * Handles the request to show the edit form for a dynamic field.
     *
     * @param model the model to which the dynamicFieldDto attribute will be added
     * @param id the ID of the dynamic field to be edited
     * @param httpSession the HTTP session to store the dynamic value ID
     * @return the name of the view for editing the dynamic field
     */
    @GetMapping("/edit")
    public String editForm(Model model, @RequestParam("id")Integer id, HttpSession httpSession){
        DynamicFieldDto dynamicFieldDto = dynamicFieldValueService.setDataToDto(id);
        model.addAttribute("dynamicFieldDto", dynamicFieldDto);
        httpSession.setAttribute("dynamicValueId",id);
        return "dynamic-field-edit-form.html";
    }

    /**
     * Handles HTTP POST requests to add a new dynamic field to the system.
     *
     * @param fieldName the name of the field to be added
     * @param fieldType the type of the field to be added
     * @param httpSession the current HTTP session to retrieve the session attributes
     * @return the redirect URL to the form showing the dynamic fields
     */
    @PostMapping("/addField")
    public String addFields(@RequestParam String fieldName, @RequestParam String fieldType,HttpSession httpSession) {
        FieldsMetadata fieldsMetadata = new FieldsMetadata();
        fieldsMetadata.setFieldName(fieldName);
        fieldsMetadata.setFieldType(fieldType);
        dynamicFieldValueService.addFieldMetadata(fieldsMetadata);
        var id = httpSession.getAttribute("id");
        return "redirect:/dynamic-fields/showForm?id="+id;
    }

    /**
     * Adds a dynamic field value to the entity specified by the parameters.
     *
     * @param entityId ID of the entity to which the dynamic field value is to be added.
     * @param entityType Type of the entity to which the dynamic field value is to be added.
     * @param fieldId ID of the field metadata for the dynamic field value.
     * @param value The value to be added for the dynamic field.
     * @param httpSession HTTP session object to retrieve session attributes.
     * @return A redirection URL to the form displaying the dynamic fields of the entity.
     */
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
    /**
     * Handles the update of dynamic fields based on the provided DTO and current session information.
     *
     * @param dynamicFieldDto The data transfer object containing dynamic field data to be updated.
     * @param httpSession The current HTTP session containing user-specific data.
     * @return A redirection string to the edit page, with or without an error query parameter based on the success of the update.
     */
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
    /**
     * Deletes a dynamic value identified by the provided ID and processes the user's session accordingly.
     *
     * @param id the ID of the dynamic value to be deleted
     * @param httpSession the current HTTP session containing session attributes
     * @return a redirection string to navigate to the appropriate page based on the operation's success or failure
     */
    @GetMapping("/delete")
    public String deleteDynamicValue(@RequestParam("id") Integer id, HttpSession httpSession){
        Integer dynamicValueId = (Integer) httpSession.getAttribute("dynamicValueId");
        Boolean isDeleted = dynamicFieldValueService.deleteDynamicValue(dynamicValueId);
        String redirect = "";
        if (isDeleted){
            redirect = String.format("redirect:/dashboard");
        } else {
            redirect = String.format("redirect:/dynamic-fields/edit?id=%s&error=1",dynamicValueId);
        }
        return redirect;
    }
}
