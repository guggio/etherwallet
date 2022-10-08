package com.guggio.etherwallet.service.external.etherscan;

import com.guggio.etherwallet.core.address.ValidatedAddress;
import io.api.etherscan.core.impl.EtherScanApi;
import io.api.etherscan.manager.impl.QueueManager;
import io.api.etherscan.model.EthNetwork;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class EtherscanService {

  private EtherScanApi etherScanApi;

  public BigInteger getBalanceOf(ValidatedAddress address) {
    return getEtherScanApi()
        .account()
        .balance(address.getAddress())
        .getWei();
  }

  private EtherScanApi getEtherScanApi() {
    if (etherScanApi == null) {
      etherScanApi = new EtherScanApi(getApiKey(), EthNetwork.MAINNET, new QueueManager(5, 1500L, 1500L, 5));
    }
    return etherScanApi;
  }

  private String getApiKey() {
    final String key = System.getenv("API_KEY");
    return (key == null || key.isEmpty())
        ? EtherScanApi.DEFAULT_KEY
        : key;
  }
}
