package com.ecommerce.project.Service;

import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.Address.AddressDTO;

import java.util.List;

public interface AddressService {
    AddressDTO addAddress(AddressDTO addressDTO, User user);

    List<AddressDTO> getAllAddresses();

    AddressDTO getAddressById(Long addressId);

    List<AddressDTO> getUserAddresses();

    AddressDTO updateAddress(Long addressId,AddressDTO addressDTO);

    String deleteAddress(Long addressId);
}
