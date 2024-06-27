package com.hss.authentication.commons.dto;

public record RestResponseDTO(
    String type,
    String title,
    Integer status,
    String detail,
    String instance) {
}