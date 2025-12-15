package com.ecommerce.services;

import com.ecommerce.dto.AddressDto;

import java.util.List;

public interface AddressService {

    AddressDto createAddress(AddressDto addressDto, Long userId);

    AddressDto updateAddress(Long addressId, AddressDto addressDto, Long userId);

    void deleteAddress(Long addressId, Long userId);

    List<AddressDto> getUserAddresses(Long userId);

    AddressDto getAddressById(Long addressId);
}



