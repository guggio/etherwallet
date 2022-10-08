package com.guggio.etherwallet.core.asset;

import com.guggio.etherwallet.entity.balance.Erc20Balance;
import com.guggio.etherwallet.entity.balance.EtherBalance;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface Balance {

  String getAssetName();

  String getAssetSymbol();

  BigInteger getValueInWei();

  BigDecimal getNormalizedValue();

  static Balance of(EtherBalance etherBalance) {
    return new AssetBalance(new EthereumAsset(), etherBalance.getValue());
  }

  static Balance of(Erc20Balance erc20Balance) {
    return new AssetBalance(erc20Balance.getErc20Token(), erc20Balance.getValue());
  }
}
