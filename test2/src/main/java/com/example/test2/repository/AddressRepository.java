package com.example.test2.repository;

import com.example.test2.entity.Address;
import com.example.test2.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    Address findByAddress(String address);
}
