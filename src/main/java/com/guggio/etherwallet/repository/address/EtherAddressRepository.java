package com.guggio.etherwallet.repository.address;

import com.guggio.etherwallet.entity.address.EtherAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EtherAddressRepository extends JpaRepository<EtherAddress, String> {
}