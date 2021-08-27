package com.shopme.admin.setting.state.controller;

import com.shopme.common.dto.StateDTO;
import com.shopme.admin.setting.state.StateRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class StateRestController {

    private StateRepository stateRepository;

    @Autowired
    public StateRestController(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    @GetMapping("/states/list_by_country/{id}")
    public List<StateDTO> listByCountry(@PathVariable("id") Integer countryId) {
        List<State> listStates = stateRepository.findByCountryOrderByNameAsc(new Country(countryId));

        List<StateDTO> result = listStates.stream()
                .map(state -> new StateDTO(state.getId(), state.getName()))
                .collect(Collectors.toList());

        return result;
    }

    @PostMapping("/states/save")
    public String save(@RequestBody State state){
        State savedState = stateRepository.save(state);
        return String.valueOf(savedState.getId());
    }

    @DeleteMapping("/states/delete/{id}")
    public void delete(@PathVariable(name = "id") Integer id){
        stateRepository.deleteById(id);
    }



}
