package com.example.petshop.controller;

import com.example.petshop.collection.Pet;
import com.example.petshop.service.PetService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.NoSuchElementException;


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
            petsModelAndView.addObject("error","An error occurred while retrieving pets.");
        }
        finally {
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
      public String createPet(@ModelAttribute Pet pet) {
        createPet(pet);

        return "redirect:/pets";
    }

    @PatchMapping("/{id}")
    public String adoptPet(@PathVariable ObjectId id) {
        petService.adoptPet(id);
        return "redirect:/pets";
    }
}

