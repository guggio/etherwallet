package com.guggio.etherwallet.service.address;

import com.guggio.etherwallet.core.address.ValidatedAddress;
import com.guggio.etherwallet.entity.address.EtherAddress;
import com.guggio.etherwallet.repository.address.EtherAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EtherAddressService {

  private final EtherAddressRepository etherAddressRepository;

  public EtherAddress loadOrCreateNew(ValidatedAddress validatedAddress) {
    return etherAddressRepository.findById(validatedAddress.getAddress())
        .orElseGet(() -> createNew(validatedAddress));
  }

  private EtherAddress createNew(ValidatedAddress validatedAddress) {
    EtherAddress etherAddress = EtherAddress.of(validatedAddress);
    return etherAddressRepository.save(etherAddress);
  }
}
