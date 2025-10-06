package org.aps.delta.eventapp.rest;

import lombok.RequiredArgsConstructor;
import org.aps.delta.eventapp.dto.EventDto;
import org.aps.delta.eventapp.dto.EventPageDto;
import org.aps.delta.eventapp.service.EventService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/event")
public class EventRestControllerV1 {

    private final EventService eventService;


    @GetMapping("/get_events")
    public EventPageDto getEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return eventService.getEvents(page, size);
    }

    @PostMapping("/create_event")
    public EventDto createEvent(@RequestBody EventDto dto) {
        return eventService.save(dto);
    }
}
