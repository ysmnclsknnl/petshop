package com.example.petshop.controller;

import com.example.petshop.Pet.SuperPet;
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

    @Autowired
    private PetService petService;

    @GetMapping
    public ModelAndView getAllPets() {
        ModelAndView petsModelAndView = new ModelAndView();

        try {
            petsModelAndView.addObject("pets", petService.allPets());
            petsModelAndView.setViewName("pets");
        } catch (Exception error) {
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
            String petId = petService.createPet(pet).toHexString();
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