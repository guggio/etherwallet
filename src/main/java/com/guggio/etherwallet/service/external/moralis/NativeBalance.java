package com.guggio.etherwallet.service.external.moralis;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
public class NativeBalance {

  private BigInteger balance;

}
