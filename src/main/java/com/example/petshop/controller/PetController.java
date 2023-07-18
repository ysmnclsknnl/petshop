package com.example.petshop.controller;

import com.example.petshop.SuperPet;
import com.example.petshop.collection.Pet;
import com.example.petshop.service.PetService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/pets")
public class PetController {

    @Autowired //don't use autowired annotation in production code
    private PetService petService;

    @GetMapping
    public ModelAndView getAllPets() {
        ModelAndView petsModelAndView = new ModelAndView(); //code can be made simpler using constructor and directly returning the values

        try {
            petsModelAndView.addObject("pets", petService.allPets());
            petsModelAndView.setViewName("pets");
        } catch (Exception error) {
            //error does not describe whats wrong
            // use constants
            petsModelAndView.addObject("error", "An error occurred while retrieving pets.");
            petsModelAndView.setViewName("error");
        }

        return petsModelAndView;
    }

    @GetMapping("/add")
    public ModelAndView addPetForm() {
        ModelAndView mav = new ModelAndView("add-pet-form");
        Pet newPet = new Pet();
        mav.addObject("pet", newPet);

        return mav;
    }

    @PostMapping("/add")
    public ModelAndView createPet(@ModelAttribute SuperPet superPet, @RequestParam("photo") MultipartFile photo) {
        ModelAndView mav = new ModelAndView();

        try {
            Pet pet = new Pet(superPet, photo);
            String petId = petService.createPet(pet).toHexString();//consider keeping your controller clean, with only code specific to the controller
            mav.addObject("successMsg", "Pet is added successfully with id: " + petId);
            mav.setViewName("success");

        } catch (Exception e) {
            mav.addObject("errorMsg", e.getMessage());
            mav.setViewName("error");
        }

        return mav;
    }

    @PostMapping("/{id}")
    public ModelAndView adoptPet(@PathVariable ObjectId id) {
        ModelAndView mav = new ModelAndView();
        try {
            String petName = petService.adoptPet(id).getName();
            mav.addObject("successMsg", "You adopted " + petName + "!");
            mav.setViewName("success");
        } catch (Exception ex) {
            mav.addObject("errorMsg", ex.getMessage());
            mav.setViewName("error");
        }

        return mav;
    }
}
