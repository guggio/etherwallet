package com.guggio.etherwallet.service.external.moralis;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
public class WalletTokenBalance {

  private String token_address;
  private String name;
  private String symbol;
  private String logo;
  private String thumbnail;
  private Integer decimals;
  private BigInteger balance;
}
