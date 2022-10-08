package com.guggio.etherwallet.core.asset;

public final class EthereumAsset implements Asset {

  @Override
  public String getName() {
    return "Ethereum";
  }

  @Override
  public String getSymbol() {
    return "ETH";
  }

  @Override
  public Integer getDecimalPlaces() {
    return 18;
  }
}
