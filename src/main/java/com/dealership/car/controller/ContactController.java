package com.dealership.car.controller;

import com.dealership.car.constants.Constants;
import com.dealership.car.dynamic.DynamicFieldValue;
import com.dealership.car.model.Contact;
import com.dealership.car.repository.ContactRepository;
import com.dealership.car.service.ContactService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * The ContactController class handles HTTP requests for managing contact-related operations.
 * It provides endpoints to display contact form, save messages, show saved messages, and close messages.
 *
 * Request mappings handled:
 * - GET /contact: Displays the contact form.
 * - POST /contact/saveMsg: Saves a contact message.
 * - GET /contact/showMessages: Displays saved contact messages.
 * - GET /contact/closeMsg: Closes a specific contact message.
 */
@Controller
@AllArgsConstructor
@RequestMapping("/contact")
public class ContactController {

    private ContactService contactService;

    private ContactRepository contactRepository;

    /**
     * Displays the contact form page.
     *
     * @param model The Model object used to pass attributes to the view.
     * @return The name of the HTML template for the contact form.
     */
    @GetMapping()
    public String showContactPage(Model model){
        model.addAttribute("contact", new Contact());
        return "contact.html";
    }

    /**
     * Handles the saving of a contact message. This endpoint is responsible for validating
     * the contact input, checking if the user already exists, and then saving the contact message.
     * Based on the results of these operations, it returns a view name or a redirection string.
     *
     * @param contact The contact entity that contains the details of the message to be saved. It is validated for required fields.
     * @param errors  BindingResult object that holds the validation errors, if any.
     * @return A string representing the view name to be rendered or a redirection path.
     */
    @PostMapping("/saveMsg")
    public String saveMsg(@Valid @ModelAttribute Contact contact, Errors errors){
        String returnResult = "";
        if (errors.hasErrors()){
            returnResult = "contact.html";
        }
        boolean isExist = contactService.isUserAlreadyExist(contact);
        boolean isSaved = contactService.saveMsg(contact);
        if (isSaved && isExist){
            returnResult = "redirect:/home";
        } else if (isSaved) {
            returnResult = "redirect:/public/register";

        }
        return returnResult;
    }
    /**
     * Displays the saved contact messages on the "messages.html" page.
     *
     * @param httpSession the HTTP session in which the entity type is set
     * @return a ModelAndView object containing the view name and the messages map with their dynamic field values
     */
    @GetMapping("/showMessages")
    public ModelAndView showMsg(HttpSession httpSession){
        ModelAndView modelAndView = new ModelAndView("messages.html");
        List<Contact> messages = contactRepository.findByStatusEqualsOrStatus(Constants.NEW_USER,Constants.USER_EXIST);
        Map<Contact,List<DynamicFieldValue>> messagesMap = contactService.getAllDynamicFieldsForContact(messages);
        modelAndView.addObject("messagesMap", messagesMap);
        httpSession.setAttribute("entityType", "Contact");
        return modelAndView;
    }
    /**
     * Closes a contact message with the given identifier and redirects to the show messages page.
     *
     * @param id the identifier of the contact message to be closed
     * @return a redirection string to the contact messages display page
     */
    @GetMapping("/closeMsg")
    public String closeMsg(@RequestParam("id") int id){
        contactService.closeMsg(id);
        return "redirect:/contact/showMessages";
    }
}
