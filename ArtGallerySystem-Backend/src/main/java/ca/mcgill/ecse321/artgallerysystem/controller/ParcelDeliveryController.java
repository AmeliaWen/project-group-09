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

import ca.mcgill.ecse321.artgallerysystem.dao.ArtGallerySystemRepository;
import ca.mcgill.ecse321.artgallerysystem.dto.ParcelDeliveryDTO;
import ca.mcgill.ecse321.artgallerysystem.model.ParcelDelivery;
import ca.mcgill.ecse321.artgallerysystem.service.ArtGallerySystemService;
import ca.mcgill.ecse321.artgallerysystem.service.ParcelDeliveryService;

@CrossOrigin(origins="*")
@RestController
public class ParcelDeliveryController {
@Autowired 
private ParcelDeliveryService parcelDeliveryService;
@Autowired 
private ArtGallerySystemService systemservice;
@Autowired
private ArtGallerySystemRepository artgallerySystemRepository;
@GetMapping(value = {"/parcelDeliveries", "/parcelDeliveries/"})
public List<ParcelDeliveryDTO> getAllParcelDeliveries(){
	
	List<ParcelDelivery> parcelDeliveries = parcelDeliveryService.getAllParcelDeliveris();
	return toList(parcelDeliveries.stream().map(this::convertToDto).collect(Collectors.toList()));
	
}
@PostMapping(value = {"/parcelDelivery", "/parcelDelivery/"})
public ParcelDeliveryDTO createParcelDelivery(@RequestParam("trackingNumber")String trackingNumber, @RequestParam("carrier")String carrier, @RequestParam("parcelDeliveryStatus")String parcelDeliveryStatus, @RequestParam("deliveryAddress")String deliveryAddress) {
	//ArtGallerySystem system = systemservice.getSystemById(id);
	ParcelDelivery parcelDelivery = ParcelDeliveryService.createParcelDelivery(trackingNumber, carrier, parcelDeliveryStatus, deliveryAddress);
	return convertToDto(parcelDelivery);
}
@GetMapping(value = {"/parcelDeliveryes/{id}", "/parcelDeliveryes/{id}/"})
public ParcelDeliveryDTO getparcelDeliveryById(@PathVariable("id")String id) {
	return convertToDto(ParcelDeliveryService.getParcelDelivery(id));
}
@DeleteMapping(value = {"/parcelDeliveryes/{id}", "/parcelDeliveryes/{id}/"})
public void deleteparcelDelivery(@PathVariable("id") String deliveryid) {
	parcelDeliveryService.deleteParcelDelivery(deliveryid);
}
@PutMapping (value = {"/parcelDelivery/update/{id}", "/parcelDelivery/update/{id}/"})
public ParcelDeliveryDTO updateparcelDelivery(@PathVariable("id")String id, @RequestParam("parcelDelivery")String newparcelDelivery) {
	return convertToDto(parcelDeliveryService.updateparcelDelivery(id, newparcelDelivery));
}
public ParcelDeliveryDTO convertToDto(ParcelDelivery parcelDelivery) {
    ParcelDeliveryDTO parcelDeliveryDTO = new ParcelDeliveryDTO();
    parcelDeliveryDTO.setTrackingNumber(parcelDelivery.getTrackingNumber());
    
    
    return parcelDeliveryDTO;
}

private <T> List<T> toList(Iterable<T> iterable) {
    List<T> resultList = new ArrayList<>();
    for (T t : iterable) {
        resultList.add(t);
    }
    return resultList;
}
}