package com.ecommerce.project.Service;

import com.ecommerce.project.Repository.AddressRepository;
import com.ecommerce.project.Repository.UserRepository;
import com.ecommerce.project.Util.AuthUtil;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Address;
import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImp implements AddressService{
    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;
    @Override
    public AddressDTO addAddresses(AddressDTO addressDTO, User user) {
        Address address = modelMapper.map(addressDTO,Address.class);
        List<Address> addressList = user.getAddresses();
        addressList.add(address);
        user.setAddresses(addressList);
        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Override
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
    public AddressDTO getAddressByAddId(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(()->new ResourceNotFoundException("Address","addressId",addressId));
        AddressDTO addressDTO = modelMapper.map(address, AddressDTO.class);
        return addressDTO;
    }

    @Override
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
    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(()->new ResourceNotFoundException("Address","addressId",addressId));
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
    public String deleteAddress(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(()->new ResourceNotFoundException("Address","addressId",addressId));
        User user = address.getUser();
        user.getAddresses().removeIf(address1 -> address.getAddressId().equals(addressId));
        addressRepository.delete(address);
        return "Address deleted successfully with AddressId : "+addressId;
    }


}
