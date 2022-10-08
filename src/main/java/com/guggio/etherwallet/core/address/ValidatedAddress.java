package com.guggio.etherwallet.core.address;

import java.util.Optional;
import java.util.regex.Pattern;

public class ValidatedAddress {

  private static final Pattern ADDRESS_PATTERN = Pattern.compile("0x[a-zA-Z0-9]{40}");

  private final String address;

  private ValidatedAddress(String address) {
    this.address = address;
  }

  public static Optional<ValidatedAddress> of(String address) {
    if (address != null && ADDRESS_PATTERN.matcher(address).matches()) {
      return Optional.of(new ValidatedAddress(address));
    } else {
      return Optional.empty();
    }
  }

  public String getAddress() {
    return address;
  }
}
