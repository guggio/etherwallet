package com.guggio.etherwallet.core.asset;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BalanceImpl implements Balance {

  private String assetName;
  private String assetSymbol;
  private BigInteger valueInWei;
  private BigDecimal normalizedValue;
}
