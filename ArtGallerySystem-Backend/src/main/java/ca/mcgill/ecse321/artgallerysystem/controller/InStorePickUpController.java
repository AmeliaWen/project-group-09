package ca.mcgill.ecse321.artgallerysystem.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.artgallerysystem.dao.AddressRepository;
import ca.mcgill.ecse321.artgallerysystem.dao.InStorePickUpRepository;
import ca.mcgill.ecse321.artgallerysystem.dao.PurchaseRepository;
import ca.mcgill.ecse321.artgallerysystem.dto.AddressDTO;
import ca.mcgill.ecse321.artgallerysystem.dto.InStorePickUpDTO;
import ca.mcgill.ecse321.artgallerysystem.model.Address;
import ca.mcgill.ecse321.artgallerysystem.model.InStorePickUp;
import ca.mcgill.ecse321.artgallerysystem.model.InStorePickUpStatus;
import ca.mcgill.ecse321.artgallerysystem.model.Purchase;
import ca.mcgill.ecse321.artgallerysystem.service.InStorePickUpService;

@CrossOrigin(origins="*")
@RestController
public class InStorePickUpController {
@Autowired 
private InStorePickUpService inStorePickUpService;
@Autowired
private AddressRepository addressRepository;
@Autowired
private PurchaseRepository purchaseRepository;
@GetMapping(value = {"/inStorePickUps", "/inStorePickUps/"})
public List<InStorePickUpDTO> getInStorePickUps(){
	
	List<InStorePickUp> inStorePickUps = inStorePickUpService.getAllInStorePickUps();
	return toList(inStorePickUps.stream().map(this::convertToDto).collect(Collectors.toList()));
	
}
@PostMapping(value = {"/inStorePickUp", "/inStorePickUp/"})

public InStorePickUpDTO createInStorePickUp(@RequestParam("deliveryid") String id,@RequestParam("pickUpReferenceNumber")String pickUpReferenceNumber,@RequestParam("inStorePickUpStatus")String status, @RequestParam("storeAddress")String storeAddress, @RequestParam("purchaseid")String purid) {

	//ArtGallerySystem system = systemservice.getSystemById(id);
	Purchase purchase = purchaseRepository.findPurchaseByOrderId(purid);
	Address address = addressRepository.findAddressByAddressId(storeAddress);
	InStorePickUpStatus inStorePickUpstatus = getStatus(status);
	InStorePickUp inStorePickUp = inStorePickUpService.createInStorePickUp(id,pickUpReferenceNumber, inStorePickUpstatus, address, purchase);
	return convertToDto(inStorePickUp);
}
@GetMapping(value = {"/inStorePickUps/{pickUpReferenceNumber}", "/inStorePickUps/{pickUpReferenceNumber}/"})
public InStorePickUpDTO getinStorePickUpById(@PathVariable("pickUpReferenceNumber")String pickUpReferenceNumber) {
	return convertToDto(inStorePickUpService.getInStorePickUp(pickUpReferenceNumber));
}
@DeleteMapping(value = {"/inStorePickUps/{pickUpReferenceNumber}", "/inStorePickUps/{pickUpReferenceNumber}/"})
public void deleteinStorePickUp(@PathVariable("pickUpReferenceNumber") String pickUpReferenceNumber) {
	inStorePickUpService.deleteInStorePickUp(pickUpReferenceNumber);
}
@PutMapping (value = {"/inStorePickUp/update/{pickUpReferenceNumber}", "/inStorePickUp/update/{pickUpReferenceNumber}/"})
public InStorePickUpDTO updateparcelDeliveryStatus(@PathVariable("pickUpReferenceNumber")String pickUpReferenceNumber, @RequestParam("inStorePickUp")String newinStorePickUp) {
	return convertToDto(inStorePickUpService.updateinStorePickUp(pickUpReferenceNumber,getStatus(newinStorePickUp)));
}
public InStorePickUpDTO convertToDto(InStorePickUp inStorePickUp) {
	InStorePickUpDTO inStorePickUpDto = new InStorePickUpDTO();
	inStorePickUpDto.setPickUpReferenceNumber(inStorePickUp.getPickUpReferenceNumber());
	inStorePickUpDto.setDeliveryId(inStorePickUp.getDeliveryId());
	inStorePickUpDto.setStoreAddress(convertToDto(inStorePickUp.getStoreAddress()));
    inStorePickUpDto.setInStorePickUpStatus(inStorePickUp.getInStorePickUpStatus());
    
    return inStorePickUpDto;
}
public InStorePickUpStatus getStatus (String status) {
	switch(status) {
	case "Pending":
		return InStorePickUpStatus.Pending;
	case "Available":
		return InStorePickUpStatus.AvailableForPickUp;
	case "PickedUp":
		return InStorePickUpStatus.PickedUp;
	
	
	}
	return null;
}
public AddressDTO convertToDto(Address address) {
    AddressDTO addressDTO = new AddressDTO();
    addressDTO.setAddressId(address.getAddressId());
    addressDTO.setCity(address.getCity());
    addressDTO.setCountry(address.getCountry());
    addressDTO.setName(address.getName());
    addressDTO.setPhoneNumber(address.getPhoneNumber());
    addressDTO.setPostalCode(address.getPostalCode());
    addressDTO.setProvince(address.getProvince());
    addressDTO.setStreetAddress(address.getStreetAddress());
    addressDTO.setArtGallerySystem(address.getArtGallerySystem());
    return addressDTO;
}
private <T> List<T> toList(Iterable<T> iterable) {
    List<T> resultList = new ArrayList<>();
    for (T t : iterable) {
        resultList.add(t);
    }
    return resultList;
}
}
