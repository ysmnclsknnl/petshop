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

    @Autowired
    private PetService petService;

    @GetMapping
    public ModelAndView getAllPets() {
        ModelAndView petsModelAndView = new ModelAndView();

        try {
            petsModelAndView.addObject("pets", petService.allPets());
        } catch (Exception error) {
            petsModelAndView.addObject("error", "An error occurred while retrieving pets.");
        } finally {
            return petsModelAndView;
        }
    }

    @GetMapping("/add")
    public ModelAndView addPetForm() {
        ModelAndView mav = new ModelAndView("add-pet-form");
        Pet newPet = new Pet();
        mav.addObject("pet", newPet);
        return mav;
    }

    @PostMapping("/add")
    public String createPet(@ModelAttribute SuperPet superPet, @RequestParam("photo") MultipartFile photo) {

        try {
            Pet pet = new Pet(superPet, photo);
            return petService.createPet(pet).toHexString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Error";
    }

    @PostMapping("/{id}")
    public String adoptPet(@PathVariable ObjectId id) throws Exception {
        try {
            return  "You adopted " + petService.adoptPet(id).getName() +"!";
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex);
        }
    }
}