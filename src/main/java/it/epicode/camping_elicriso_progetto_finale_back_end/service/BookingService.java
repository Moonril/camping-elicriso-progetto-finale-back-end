package it.epicode.camping_elicriso_progetto_finale_back_end.service;

import it.epicode.camping_elicriso_progetto_finale_back_end.dto.BookingDto;
import it.epicode.camping_elicriso_progetto_finale_back_end.enums.BookingStatus;
import it.epicode.camping_elicriso_progetto_finale_back_end.exceptions.NotFoundException;
import it.epicode.camping_elicriso_progetto_finale_back_end.models.Accommodation;
import it.epicode.camping_elicriso_progetto_finale_back_end.models.Customer;
import it.epicode.camping_elicriso_progetto_finale_back_end.models.Booking;
import it.epicode.camping_elicriso_progetto_finale_back_end.repository.CustomerRepository;
import it.epicode.camping_elicriso_progetto_finale_back_end.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class BookingService {
    
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PlotService plotService;

    @Autowired
    private GlampingService glampingService;

    @Autowired
    private MobileHomeService mobileHomeService;

    public Booking saveBooking(BookingDto bookingDto) throws NotFoundException {
        Customer customer = customerService.getCustomer(bookingDto.getCustomerId());

        Accommodation accommodation;
        switch (bookingDto.getAccommodationType()) {
            case PLOT:
                accommodation = plotService.getPlot(bookingDto.getAccommodationId());
                break;
            case GLAMPING:
                accommodation = glampingService.getGlamping(bookingDto.getAccommodationId());
                break;
            case MOBILEHOME:
                accommodation = mobileHomeService.getMobileHome(bookingDto.getAccommodationId());
                break;
            default:
                throw new IllegalArgumentException("Invalid accommodation type: " + bookingDto.getAccommodationType());
        }

        Booking booking = new Booking();
        booking.setCheckInDate(bookingDto.getCheckInDate());
        booking.setCheckOutDate(bookingDto.getCheckOutDate());
        booking.setNumberOfCustomers(bookingDto.getNumberOfCustomers());
        booking.setPreference(bookingDto.getPreference());
        booking.setCustomer(customer);
        booking.setAccommodations(Set.of(accommodation));

        List<Booking> overlappingBookings =
                bookingRepository.findOverlappingBookings(
                        bookingDto.getAccommodationId(),
                        bookingDto.getCheckInDate(),
                        bookingDto.getCheckOutDate()
                );

        if (!overlappingBookings.isEmpty()) {
            throw new IllegalStateException("Accommodation is already reserved during this period.");
        }


        return bookingRepository.save(booking);
    }

    public Page<Booking> getAllBookings(int page, int size, String sortBy){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return bookingRepository.findAll(pageable);
    }

    public Booking getBooking(int id) throws NotFoundException{
        return bookingRepository.findById(id).orElseThrow(() -> new NotFoundException("Booking with id: " + id + " not found"));
    }


    public Booking updateBooking(int id, BookingDto bookingDto) throws NotFoundException{
        Booking bookingToUpdate = getBooking(id);

        bookingToUpdate.setCheckInDate(bookingDto.getCheckInDate());
        bookingToUpdate.setCheckOutDate(bookingDto.getCheckOutDate());
        bookingToUpdate.setNumberOfCustomers(bookingDto.getNumberOfCustomers());
        bookingToUpdate.setPreference(bookingDto.getPreference());

        if(bookingToUpdate.getCustomer().getId()!= bookingDto.getCustomerId()){
            Customer customer = customerService.getCustomer(bookingDto.getCustomerId());
            bookingToUpdate.setCustomer(customer);
        }

        return bookingRepository.save(bookingToUpdate);
    }

    public Booking patchBooking(int id, BookingStatus newStatus) throws NotFoundException, IllegalArgumentException {
        Booking booking = getBooking(id);
        BookingStatus currentStatus = booking.getBookingStatus();

        Map<BookingStatus, List<BookingStatus>> validTransitions = Map.of(
                BookingStatus.PENDING, List.of(BookingStatus.CONFIRMED, BookingStatus.CANCELLED),
                BookingStatus.CONFIRMED, List.of(BookingStatus.CANCELLED, BookingStatus.COMPLETED),
                BookingStatus.CANCELLED, List.of(),
                BookingStatus.COMPLETED, List.of()
        );

        //non so se necessario
        if (!validTransitions.get(currentStatus).contains(newStatus)) {
            throw new IllegalArgumentException("Transition from " + currentStatus + " to " + newStatus + " not allowed.");
        }


        booking.setBookingStatus(newStatus);


        return bookingRepository.save(booking);
    }

    public void deleteBooking(int id) throws NotFoundException{
        Booking bookingToDelete = getBooking(id);

        bookingRepository.delete(bookingToDelete);
    }

    // per cambiare il booking status dopo il check out
    public void autoCompleteBookings() {
        List<Booking> confirmedBookings = bookingRepository.findByBookingStatus(BookingStatus.CONFIRMED);
        LocalDate today = LocalDate.now();

        for (Booking booking : confirmedBookings) {
            if (booking.getCheckOutDate().isBefore(today)) {
                booking.setBookingStatus(BookingStatus.COMPLETED);
                bookingRepository.save(booking);
            }
        }
    }
}
