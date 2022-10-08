package com.guggio.etherwallet.controller;

import com.guggio.etherwallet.core.address.ValidatedAddress;
import com.guggio.etherwallet.service.balance.EtherBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/balance")
@RequiredArgsConstructor
public class BalanceController {

  private final EtherBalanceService etherBalanceService;

  @GetMapping("/ether/{address}")
  public ResponseEntity<EtherBalanceResponse> getEtherBalanceOf(@PathVariable("address") String address) {
    return ValidatedAddress.of(address)
        .map(this::createEthBalanceRetrievedResponse)
        .orElseGet(this::createInvalidAddressResponse);
  }

  private ResponseEntity<EtherBalanceResponse> createEthBalanceRetrievedResponse(ValidatedAddress validatedAddress) {
    return ResponseEntity.ok(
        EtherBalanceResponse.builder()
            .message("ETH balance retrieved")
            .balance(etherBalanceService.getBalanceOf(validatedAddress))
            .build()
    );
  }

  private ResponseEntity<EtherBalanceResponse> createInvalidAddressResponse() {
    return new ResponseEntity<>(
        EtherBalanceResponse.builder()
            .message("Invalid address format")
            .build(),
        HttpStatus.BAD_REQUEST);
  }

}
