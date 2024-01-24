package com.whl.hotelService.controller;

import com.whl.hotelService.domain.common.dto.PaymentDto;
import com.whl.hotelService.domain.common.dto.ReservationDto;
import com.whl.hotelService.domain.common.entity.Hotel;
import com.whl.hotelService.domain.common.entity.Reservation;
import com.whl.hotelService.domain.common.entity.Room;
import com.whl.hotelService.domain.common.repository.RoomRepository;
import com.whl.hotelService.domain.common.service.HotelService;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.whl.hotelService.controller.days.getDaysOfWeekBetweenDates;

@Slf4j
@Controller
@RequestMapping(value = "hotel")
public class HotelController {
    @Autowired
    private HotelService hotelService;

    @GetMapping(value = "find")
    public void getFind() {
        log.info("getFind()");
    }

    @GetMapping(value = "findMap")
    public void getFindMap() {
        log.info("getFindMap()");
    }

    @GetMapping(value = "info")
    public void getInfo() {
        log.info("getInfo()");
    }

    @GetMapping(value = "reservationStep1")
    public void getReservationStep1(@RequestParam(value = "hotelname") String hotelname,
                                    @RequestParam(value = "checkin")
                                    String checkin,
                                    @RequestParam(value = "checkout")
                                    String checkout,
                                    @RequestParam(value = "adultCount") int adultCount,
                                    @RequestParam(value = "childCount") int childCount,
                                    Model model) {
        int people = adultCount + childCount;
        List<Room> roomList = hotelService.getHotelsRoom(hotelname, people);

        for(Room room : roomList) {
            int reservedRoomCount = hotelService.getReservedRoomCount(checkin, room.getId());
            hotelService.addReservedRoomCount(room.getId(), reservedRoomCount);
        }
        model.addAttribute("roomList", roomList);


        List<Hotel> hotelList = hotelService.getAllHotel();
        model.addAttribute("hotelList", hotelList);
        List<String> region = hotelService.getDistinctRegion();
        model.addAttribute("region", region);

        model.addAttribute("hotelname", hotelname);
        model.addAttribute("checkin", checkin);
        model.addAttribute("checkout", checkout);
        model.addAttribute("adultCount", adultCount);
        model.addAttribute("childCount", childCount);

        // 주중, 주말 가격 계산을 위한 요일 카운트 리스트 생성 코드
        // [금, 토, 주중]
        LocalDate startDate = LocalDate.parse(checkin, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endDate = LocalDate.parse(checkout, DateTimeFormatter.ofPattern("yyyy-MM-dd")).minusDays(1);

        DayOfWeek[] daysOfWeekBetweenDates = getDaysOfWeekBetweenDates(startDate, endDate);

        long fridayCount = Arrays.stream(daysOfWeekBetweenDates)
                .filter(dayOfWeek -> dayOfWeek == DayOfWeek.FRIDAY)
                .count();
        long saturdayCount = Arrays.stream(daysOfWeekBetweenDates)
                .filter(dayOfWeek -> dayOfWeek == DayOfWeek.SATURDAY)
                .count();
        long weekdayCount = Arrays.stream(daysOfWeekBetweenDates)
                .filter(dayOfWeek -> dayOfWeek != DayOfWeek.FRIDAY && dayOfWeek != DayOfWeek.SATURDAY)
                .count();

        List<Long> dayCountList = new ArrayList<>();

        dayCountList.add(fridayCount);
        dayCountList.add(saturdayCount);
        dayCountList.add(weekdayCount);

        model.addAttribute("dayLength", daysOfWeekBetweenDates.length); // n박
        model.addAttribute("dayCountList", dayCountList);

    }

    @PostMapping(value = "reservationStep1")
    public ResponseEntity<String> postReservationStep1(ReservationDto reservationDto) {
        boolean isInserted = hotelService.insertReservation(reservationDto);

        if (isInserted) {
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("FAILURE", HttpStatus.BAD_GATEWAY);
        }
    }

    @GetMapping(value = "reservationStep2")
    public void getReservationStep2(Model model) {
        Reservation reservation = hotelService.getReservationList();

        model.addAttribute("reservation", reservation);
    }


    @PostMapping(value = "reservationStep2")
    public ResponseEntity<String> getPayment(PaymentDto paymentDto) throws UnsupportedEncodingException {
        paymentDto.setAddress(URLDecoder.decode(paymentDto.getAddress(), "UTF-8"));
        paymentDto.setName(URLDecoder.decode(paymentDto.getName(), "UTF-8"));

        String addReservedRoomCount = hotelService.addReservedRoomCount(paymentDto.getReservationId());
        if (addReservedRoomCount == "FAILURE_NOVACANCY") {
            return new ResponseEntity<>("FAILURE_NOVACANCY", HttpStatus.CONFLICT);
        }

        boolean isAdded = hotelService.addPayment(paymentDto);

        if(isAdded && (addReservedRoomCount == "SUCCESS")) {
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("FAILURE", HttpStatus.CONFLICT);
        }
    }
}

class days {
    // 주중, 주말 가격 계산을 위한 리스트 생성 메서드
    public static DayOfWeek[] getDaysOfWeekBetweenDates(LocalDate startDate, LocalDate endDate) {
        List<DayOfWeek> daysOfWeekList = new ArrayList<>();

        // Iterate through the dates and add the day of the week to the list
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            daysOfWeekList.add(currentDate.getDayOfWeek());
            currentDate = currentDate.plusDays(1);
        }

        return daysOfWeekList.toArray(new DayOfWeek[0]);
    }
}