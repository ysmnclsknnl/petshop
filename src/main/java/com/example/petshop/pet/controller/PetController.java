package com.example.petshop.pet.controller;

import com.example.petshop.pet.service.PetService;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;

@RestController
@RequestMapping("/pets")
public class PetController {

    public static final String ERROR_VIEW_NAME = "error";

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }
    
    @GetMapping
    public ModelAndView getAllPets() {
        try {
            return new ModelAndView("pets", Collections.singletonMap("pets", petService.allPets()));
        } catch (Exception e) {
            return new ModelAndView(ERROR_VIEW_NAME, Collections.singletonMap("errorMsg", e.getMessage()));
        }
    }

    @PostMapping("/add")
    //org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException is not handled nicely
    public ModelAndView createPet(@RequestBody CreatePetDTO petDTO ) {

        try {
            String petId = petService.createPet(petDTO);

            return new ModelAndView(
                    "success",
                    Collections.singletonMap("successMsg", "Pet is added successfully with id: " + petId )
            );
        } catch (Exception e) {

            return new ModelAndView(ERROR_VIEW_NAME, Collections.singletonMap("errorMsg", e.getMessage()));
        }
    }

    @PatchMapping("/{id}")
    public ModelAndView adoptPet(@PathVariable ObjectId id) {
        try {
            return new ModelAndView(
                    "success",
                    Collections.singletonMap("successMsg", petService.adoptPet(id))
            );
        } catch (Exception e) {
            return new ModelAndView(ERROR_VIEW_NAME, Collections.singletonMap("errorMsg", e.getMessage()));
        }
    }
}
