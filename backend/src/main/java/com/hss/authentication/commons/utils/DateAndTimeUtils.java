package com.hss.authentication.commons.utils;

import java.time.Instant;

public final class DateAndTimeUtils {

    private DateAndTimeUtils(){}

    public static Instant generateExpirationInstant() {
        return Instant.now().plusSeconds(7200L);
    }
}