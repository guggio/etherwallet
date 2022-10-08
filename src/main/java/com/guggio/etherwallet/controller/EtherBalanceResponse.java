package com.guggio.etherwallet.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.guggio.etherwallet.core.asset.Balance;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class EtherBalanceResponse {
  private String message;
  private Balance balance;
}
