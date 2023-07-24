package com.example.petshop.controller;

import com.example.petshop.dto.PetDTO;
import com.example.petshop.service.PetService;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;

@RestController
@RequestMapping("/pets")
public class PetController {

    final private PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }
    
    @GetMapping
    public ModelAndView getAllPets() {
        try {
            return new ModelAndView("pets", Collections.singletonMap("pets", petService.allPets()));
        } catch (Exception e) {
            return new ModelAndView("error", Collections.singletonMap("errorMsg", e.getMessage()));
        }
    }

    @GetMapping("/add")
    public ModelAndView addPetForm() {
        return new ModelAndView("add-pet-form", Collections.singletonMap("pet", new PetDTO()));
    }

    @PostMapping("/add")
    public ModelAndView createPet(@ModelAttribute PetDTO petDTO, @RequestParam("petImage") MultipartFile image) {

        try {
            String petId = petService.createPet(petDTO, image);

            return new ModelAndView(
                    "success",
                    Collections.singletonMap("successMsg", "Pet is added successfully with id: " + petId )
            );
        } catch (Exception e) {

            return new ModelAndView("error", Collections.singletonMap("errorMsg", e.getMessage()));
        }
    }

    @PatchMapping("/{id}")
    public ModelAndView adoptPet(@PathVariable ObjectId id) {
        try {
            String petName = petService.adoptPet(id);

            return new ModelAndView(
                    "success",
                    Collections.singletonMap("successMsg", "You adopted " + petName + "!" )
            );
        } catch (Exception e) {
            return new ModelAndView("error", Collections.singletonMap("errorMsg", e.getMessage()));
        }
    }
}
