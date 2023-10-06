package aau.project.drinkingfountainbackend.api.controller;

import aau.project.drinkingfountainbackend.api.dto.DrinkingFountainRequestDTO;
import aau.project.drinkingfountainbackend.api.dto.DrinkingFountainDTO;
import aau.project.drinkingfountainbackend.service.DrinkingFountainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/fountain")
public class DrinkingFountainController {

    DrinkingFountainService drinkingFountainService;

    @Autowired
    public DrinkingFountainController(DrinkingFountainService drinkingFountainService) {
        this.drinkingFountainService = drinkingFountainService;
    }

    @GetMapping("{id}")
    public ResponseEntity<DrinkingFountainDTO> getDrinkingFountain(@PathVariable int id){
        return drinkingFountainService.getDrinkingFountain(id)
                .map(drinkingFountainDTO -> new ResponseEntity<>(drinkingFountainDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/request")
    public void createNewFountainRequest(@RequestBody DrinkingFountainRequestDTO drinkingFountainRequestDTO){
        drinkingFountainService.saveDrinkingFountainRequest(drinkingFountainRequestDTO);
    }

}