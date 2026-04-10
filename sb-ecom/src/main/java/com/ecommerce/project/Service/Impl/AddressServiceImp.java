package com.ecommerce.project.Service.Impl;

import com.ecommerce.project.Repository.AddressRepository;
import com.ecommerce.project.Repository.UserRepository;
import com.ecommerce.project.Service.AddressService;
import com.ecommerce.project.Util.AuthUtil;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Address;
import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.Address.AddressDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImp implements AddressService {

    private final AuthUtil authUtil;
    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public AddressDTO addAddress(AddressDTO addressDTO, User user) {

        Address address = modelMapper.map(addressDTO,Address.class);
        user.addAddress(address);
        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressDTO> getAllAddresses() {
        List<Address> addresses = addressRepository.findAll();
        List<AddressDTO> addressDTOS = addresses.stream()
                .map(address -> {
                    AddressDTO addressDTO = modelMapper.map(address, AddressDTO.class);
                    return addressDTO;
                }).toList();
        return addressDTOS;
    }

    @Override
    @Transactional(readOnly = true)
    public AddressDTO getAddressById(Long addressId) {

        Address address = addressRepository.findById(addressId)
                .orElseThrow(()->new ResourceNotFoundException("Address","addressId",addressId));

        AddressDTO addressDTO = modelMapper.map(address, AddressDTO.class);
        return addressDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressDTO> getUserAddresses() {
        User user = authUtil.loggedInUser();
        List<Address> addresses = addressRepository.findAddressByUserId(user.getUserId());

        List<AddressDTO> addressDTOList = addresses.stream()
                .map(address -> {
                    AddressDTO addressDTO = modelMapper.map(address, AddressDTO.class);
                    return addressDTO;
                }).toList();

        return addressDTOList;
    }

    @Override
    @Transactional
    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO) {

        Address address = addressRepository.findById(addressId)
                .orElseThrow( ()-> new ResourceNotFoundException("Address","addressId",addressId));

        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setCountry(addressDTO.getCountry());
        address.setBuilding(addressDTO.getBuilding());
        address.setStreet(addressDTO.getStreet());
        address.setPincode(addressDTO.getPincode());

        Address updatedAddress = addressRepository.save(address);
        User user = address.getUser();
        user.getAddresses().removeIf(address1 -> address.getAddressId().equals(addressId));
        user.getAddresses().add(updatedAddress);
        userRepository.save(user);

        return modelMapper.map(updatedAddress, AddressDTO.class);
    }

    @Override
    @Transactional
    public String deleteAddress(Long addressId) {

        Address address = addressRepository.findById(addressId)
                .orElseThrow(()->new ResourceNotFoundException("Address","addressId",addressId));

        User user = address.getUser();
        user.getAddresses().remove(address);

        return "Address deleted successfully with AddressId : "+addressId;
    }


}
