package com.example.hotel.repository;

import com.example.hotel.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Integer> {
    List<Room> findAllByHotelId(Integer hotel_id);

    boolean existsByNumberAndHotelId(String number, Integer hotel_id);

    Page<Room> findAllByHotelId(Integer hotel_id, Pageable pageable);
}
