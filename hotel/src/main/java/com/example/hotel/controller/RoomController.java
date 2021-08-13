package com.example.hotel.controller;

import com.example.hotel.entity.Hotel;
import com.example.hotel.entity.Room;
import com.example.hotel.paylod.RoomDto;
import com.example.hotel.repository.HotelRepository;
import com.example.hotel.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/room")
public class RoomController {
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    HotelRepository hotelRepository;

//    CREATE
    @PostMapping
    public String addRoom(@RequestBody RoomDto roomDto){
        Optional<Hotel> optionalHotel = hotelRepository.findById(roomDto.getHotelId());
        if (!optionalHotel.isPresent()){
            return "Hotel is not found";
        }
        boolean exists = roomRepository.existsByNumberAndHotelId(roomDto.getNumber(), roomDto.getHotelId());
        if (exists){
            return "This room is exist in this hotel";
        }
        Room room = new Room();
        room.setFloor(roomDto.getFloor());
        room.setHotel(optionalHotel.get());
        room.setNumber(roomDto.getNumber());
        room.setSize(roomDto.getSize());
        roomRepository.save(room);
        return "Room is added";
    }

//    READ
    @GetMapping()
    public Page<Room> getRooms(@RequestParam int page){
        Pageable pageable = PageRequest.of(page, 10);
        return roomRepository.findAll(pageable);
    }

    @GetMapping("/byHotelId/{hotelId}")
    public Page<Room> getRoomsByHotelId(@PathVariable Integer hotelId, @RequestParam int page){
        Pageable pageable = PageRequest.of(page, 10);
        return  roomRepository.findAllByHotelId(hotelId, pageable);
    }

    @GetMapping("/{id}")
    public Room getRoomById(@PathVariable Integer id){
        Optional<Room> optionalRoom = roomRepository.findById(id);
        if (!optionalRoom.isPresent()){
            return new Room();
        }
        return optionalRoom.get();
    }

//    UPDATE
    @PutMapping("/{id}")
    public String editRoomById(@PathVariable Integer id, @RequestBody RoomDto roomDto){
        Optional<Room> optionalRoom = roomRepository.findById(id);
        if (!optionalRoom.isPresent()){
            return "Room is not found";
        }
        Optional<Hotel> optionalHotel = hotelRepository.findById(roomDto.getHotelId());
        if (!optionalHotel.isPresent()){
            return "Hotel is not found";
        }
        boolean exists = roomRepository.existsByNumberAndHotelId(roomDto.getNumber(), roomDto.getHotelId());
        if (exists){
            return "This room is exist in this hotel";
        }
        Room room = optionalRoom.get();
        room.setFloor(roomDto.getFloor());
        room.setHotel(optionalHotel.get());
        room.setNumber(roomDto.getNumber());
        room.setSize(roomDto.getSize());
        roomRepository.save(room);
        return "Room is edited";
    }

//    DELETE
    @DeleteMapping("/{id}")
    public String deleteRoomById(@PathVariable Integer id){
        try {
            roomRepository.deleteById(id);
            return "Room is deleted";
        }catch (Exception e){
            return "Error";
        }
    }
}
