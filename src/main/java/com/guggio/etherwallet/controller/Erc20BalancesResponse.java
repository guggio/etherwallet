package com.guggio.etherwallet.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.guggio.etherwallet.core.asset.Balance;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class Erc20BalancesResponse {
  private String message;
  private List<Balance> balances;
}
