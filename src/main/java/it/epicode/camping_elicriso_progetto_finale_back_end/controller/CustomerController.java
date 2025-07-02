package it.epicode.camping_elicriso_progetto_finale_back_end.controller;

import it.epicode.camping_elicriso_progetto_finale_back_end.dto.CustomerDto;
import it.epicode.camping_elicriso_progetto_finale_back_end.exceptions.NotFoundException;
import it.epicode.camping_elicriso_progetto_finale_back_end.exceptions.ValidationException;
import it.epicode.camping_elicriso_progetto_finale_back_end.models.Customer;
import it.epicode.camping_elicriso_progetto_finale_back_end.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/customers")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;
    
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public Page<Customer> getAllCustomers(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(defaultValue = "id") String sortBy) {
        return customerService.getAllCustomers(page, size, sortBy);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public Customer getCustomerById(@PathVariable int id) throws NotFoundException {
        return customerService.getCustomer(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Customer saveCustomer(@RequestBody @Validated CustomerDto customerDto, BindingResult bindingResult) throws NotFoundException, ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage()).reduce("", (e, s) -> e + s));
        }
        return customerService.saveCustomer(customerDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Customer updateCustomer(@PathVariable int id, @RequestBody @Validated CustomerDto customerDto, BindingResult bindingResult) throws NotFoundException, ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage()).reduce("", (e, s) -> e + s));
        }
        return customerService.updateCustomer(id, customerDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteCustomer(@PathVariable int id) throws NotFoundException {
        customerService.deleteCustomer(id);
    }
    
}
