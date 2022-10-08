package com.guggio.etherwallet.core.asset;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class AssetBalance implements Balance {

  private final Asset asset;
  private final BigInteger valueInWei;

  AssetBalance(Asset asset, BigInteger valueInWei) {
    this.asset = asset;
    this.valueInWei = valueInWei;
  }

  @Override
  public String getAssetName() {
    return asset.getName();
  }

  @Override
  public String getAssetSymbol() {
    return asset.getSymbol();
  }

  @Override
  public BigInteger getValueInWei() {
    return valueInWei;
  }

  @Override
  public BigDecimal getNormalizedValue() {
    return new BigDecimal(valueInWei).divide(BigDecimal.valueOf(Math.pow(10, asset.getDecimalPlaces())), 5, RoundingMode.HALF_UP);
  }
}
