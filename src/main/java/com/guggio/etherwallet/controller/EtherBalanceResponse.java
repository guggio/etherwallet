package com.guggio.etherwallet.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.guggio.etherwallet.core.asset.Balance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EtherBalanceResponse {
  private String message;
  private Balance balance;
}
