package com.joker.mmsbackendserviceclient.service;


import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "mms-backend-reserve-service", path = "/api/reserve/inner")
public interface ReserveFeignClient {
}
