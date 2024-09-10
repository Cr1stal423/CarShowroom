package com.dealership.car.controller;

import com.dealership.car.DTO.SupplierDto;
import com.dealership.car.dynamic.DynamicFieldValue;
import com.dealership.car.model.OrderEntity;
import com.dealership.car.model.Supplier;
import com.dealership.car.repository.OrderEntityRepository;
import com.dealership.car.repository.ProductRepository;
import com.dealership.car.repository.SupplierRepository;
import com.dealership.car.service.AnalyticsService;
import com.dealership.car.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/supplier")
public class SupplierController {

    private final SupplierRepository supplierRepository;
    private final OrderEntityRepository orderEntityRepository;
    private final SupplierService supplierService;
    private final AnalyticsService analyticsService;
    private final ProductRepository productRepository;

    public SupplierController(SupplierRepository supplierRepository, OrderEntityRepository orderEntityRepository, SupplierService supplierService, AnalyticsService analyticsService, ProductRepository productRepository) {
        this.supplierRepository = supplierRepository;
        this.orderEntityRepository = orderEntityRepository;
        this.supplierService = supplierService;
        this.analyticsService = analyticsService;
        this.productRepository = productRepository;
    }

    @GetMapping("/supplierForm")
    public String supplierForm(Model model){
        model.addAttribute("supplierDto", new SupplierDto());
        return "supplierForm.html";
    }
    @PostMapping("/addSupplier")
    public String addSupplier(@Valid @ModelAttribute SupplierDto supplierDto, Errors errors){
        supplierService.createSupplier(supplierDto);
        if (errors.hasErrors()){
            return "supplierForm.html";
        }
        return "redirect:/supplier/supplierForm";
    }

    @GetMapping("/supplierOrderForm")
    public String addSupplierToOrder(Model model){
        List<OrderEntity> orderList = orderEntityRepository.findAll();
        List<Supplier> supplierList = supplierRepository.findAll();
        model.addAttribute("orderList", orderList);
        model.addAttribute("supplierList", supplierList);
        return "supplierOrdersForm.html";
    }
    @PostMapping("/addSupplierToOrder")
    @Transactional
    public String addSupplierToOrder(@RequestParam("orderId") int orderId, @RequestParam("supplierId") int supplierId){
        Optional<OrderEntity> optionalOrder = orderEntityRepository.findById(orderId);
        Optional<Supplier> optionalSupplier = supplierRepository.findById(supplierId);
        if (optionalOrder.isPresent() && optionalSupplier.isPresent()){
            OrderEntity order = optionalOrder.get();
            Supplier supplier = optionalSupplier.get();
            supplier.addOrder(order);
            order.setSupplier(supplier);
            supplierRepository.save(supplier);
            orderEntityRepository.save(order);
        }
        return "redirect:/supplier/supplierOrderForm";
    }
    @GetMapping("/listSuppliersSoon")
    public String showListSoon(Model model){
        List<Supplier> supplierList = supplierService.getAllComingSoonSuppliers();
        Map<Supplier,List<DynamicFieldValue>> supplierMap = supplierService.getAllDynamicFieldsForSupplier(supplierList);
        model.addAttribute("supplierMap", supplierMap);
        return "suppliersSoon.html";
    }
    @GetMapping("/setDelay")
    public String setDelay(@RequestParam("id") Integer id, Model model){
        Supplier supplier = supplierRepository.findById(id).get();
        supplier.setIsDelayed(true);
        supplierRepository.save(supplier);
        return "redirect:/supplier/listSuppliersSoon";
    }
    @PostMapping("/updateSupplier")
    public String updateSupplier(@RequestParam("id") Integer id, @RequestParam("name") String name,
                                 @RequestParam("isDelayed")boolean isDelayed, Errors errors){
        Supplier supplier = supplierRepository.findById(id).get();
        supplier.setName(name);
        supplier.setIsDelayed(isDelayed);
        supplierRepository.save(supplier);
        return "redirect:/supplier/listSuppliersSoon";
    }
}
