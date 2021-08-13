package com.example.hotel.controller;

import com.example.hotel.entity.Hotel;
import com.example.hotel.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/hotel")
public class HotelController {
    @Autowired
    HotelRepository hotelRepository;

//    CREATE
    @PostMapping
    public String addHolet(@RequestBody Hotel hotel){
        boolean exists = hotelRepository.existsByName(hotel.getName());
        if (exists){
            return "This hotel is added already";
        }
        hotelRepository.save(hotel);
        return "Hotel is added";
    }

//    READ
    @GetMapping
    public Page<Hotel> getHotels(@RequestParam int page){
        Pageable pageable = PageRequest.of(page, 10);
       return  hotelRepository.findAll(pageable);
    }

    @GetMapping("/{id}")
    public Hotel getHotelById(@PathVariable Integer id){
        Optional<Hotel> optionalHotel = hotelRepository.findById(id);
        if (!optionalHotel.isPresent()){
            return new Hotel();
        }
        return optionalHotel.get();
    }

//    UPDATE
    @PutMapping("/{id}")
    public String editHotelBuId(@PathVariable Integer id, @RequestBody Hotel hotel){
        Optional<Hotel> optionalHotel = hotelRepository.findById(id);
        if (!optionalHotel.isPresent()){
            return "Hotel is not found";
        }
        boolean exists = hotelRepository.existsByName(hotel.getName());
        if (exists){
            return "This hotel is added already";
        }
        hotel.setId(id);
        hotelRepository.save(hotel);
        return "Hotel is edited";
    }

//    DELETE
    @DeleteMapping("/{id}")
    public String deleteHotelById(@PathVariable Integer id){
        try {
            hotelRepository.deleteById(id);
            return "Hotel is deleted";
        } catch (Exception e) {
            return "Error in deleting";
        }
    }
}
