package com.example.hotel.paylod;

import com.example.hotel.entity.Hotel;
import lombok.Data;

import javax.persistence.ManyToOne;

@Data
public class RoomDto {
    private String number;

    private Integer floor;

    private String size;

    private Integer hotelId;
}
