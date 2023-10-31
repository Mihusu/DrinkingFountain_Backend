package aau.project.drinkingfountainbackend.api.controller;

import aau.project.drinkingfountainbackend.api.dto.DrinkingFountainMapDTO;
import aau.project.drinkingfountainbackend.api.dto.DrinkingFountainRequestDTO;
import aau.project.drinkingfountainbackend.api.dto.DrinkingFountainDTO;
import aau.project.drinkingfountainbackend.api.dto.FountainListViewDTO;
import aau.project.drinkingfountainbackend.service.DrinkingFountainService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/fountain")
public class DrinkingFountainController {

    private final DrinkingFountainService drinkingFountainService;

    @Autowired
    public DrinkingFountainController(DrinkingFountainService drinkingFountainService) {
        this.drinkingFountainService = drinkingFountainService;
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<DrinkingFountainDTO> getDrinkingFountain(@PathVariable int id){
        return drinkingFountainService.getDrinkingFountain(id)
                .map(drinkingFountainDTO -> new ResponseEntity<>(drinkingFountainDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/map")
    public ResponseEntity<List<DrinkingFountainMapDTO>> getDrinkingFountainMapData(@RequestParam double latitude, @RequestParam double longitude){
        return new ResponseEntity<>(drinkingFountainService.getDrinkingFountainMapData(latitude, longitude), HttpStatus.OK);
    }

    @GetMapping("/unapproved")
    public ResponseEntity<List<DrinkingFountainDTO>> getUnapprovedDrinkingFountains(){
        return new ResponseEntity<>(drinkingFountainService.getUnapprovedDrinkingFountains(), HttpStatus.OK);
    }

    @PostMapping("/request")
    public void createNewFountainRequest(@RequestBody DrinkingFountainRequestDTO drinkingFountainRequestDTO, HttpServletRequest httpServletRequest){
        drinkingFountainService.saveDrinkingFountainRequest(drinkingFountainRequestDTO, httpServletRequest);
    }

    @PostMapping("approve/{id}")
    public void approveDrinkingFountain(@PathVariable int id){
        drinkingFountainService.approveDrinkingFountain(id);
    }

    @GetMapping("/nearest/list")
    public ResponseEntity<List<FountainListViewDTO>> getNearestFountains(@RequestParam double latitude, @RequestParam double longitude){
        List<FountainListViewDTO> fountains = drinkingFountainService.getNearestDrinkingFountains(latitude, longitude);
        return new ResponseEntity<>(fountains, HttpStatus.OK);
    }
}