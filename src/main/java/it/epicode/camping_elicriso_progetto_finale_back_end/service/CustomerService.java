package it.epicode.camping_elicriso_progetto_finale_back_end.service;

import it.epicode.camping_elicriso_progetto_finale_back_end.dto.CustomerDto;
import it.epicode.camping_elicriso_progetto_finale_back_end.exceptions.NotFoundException;
import it.epicode.camping_elicriso_progetto_finale_back_end.models.Customer;
import it.epicode.camping_elicriso_progetto_finale_back_end.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public Customer saveCustomer(CustomerDto customerDto) throws NotFoundException {
        Customer customer = new Customer();

        customer.setName(customerDto.getName());
        customer.setSurname(customerDto.getSurname());
        customer.setEmail(customerDto.getEmail());
        customer.setPhoneNumber(customerDto.getPhoneNumber());

        return customerRepository.save(customer);
    }

    public Page<Customer> getAllCustomers(int page, int size, String sortBy){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return customerRepository.findAll(pageable);
    }

    public Customer getCustomer(int id) throws NotFoundException{
        return customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Customer with id: " + id + " not found"));
    }

    public List<Customer> getCustomersByName(String name) throws NotFoundException {
        List<Customer> customers = customerRepository.findByNameContainingIgnoreCase(name);

        if (customers.isEmpty()) {
            throw new NotFoundException("Nessun cliente trovato con nome contenente: " + name);
        }

        return customers;
    }


    public Customer updateCustomer(int id, CustomerDto customerDto) throws NotFoundException{
        Customer customerToUpdate = getCustomer(id);

        customerToUpdate.setName(customerDto.getName());
        customerToUpdate.setSurname(customerDto.getSurname());
        customerToUpdate.setEmail(customerDto.getEmail());
        customerToUpdate.setPhoneNumber(customerDto.getPhoneNumber());

        return customerRepository.save(customerToUpdate);
    }

    public void deleteCustomer(int id) throws NotFoundException{
        Customer customerToDelete = getCustomer(id);

        customerRepository.delete(customerToDelete);
    }
}
