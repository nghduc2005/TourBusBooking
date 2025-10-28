package com.csdl.tourbusbooking.dto;

import lombok.Data;

import java.util.List;
@Data
public class DeleteSelectedRequest {
    private List<Long> ids;
}
