package com.shopme.admin.setting.state;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class StateRepositoryTests {

    @Autowired
    private StateRepository sut;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateState(){
        Integer countryId = 1;
        Country country = entityManager.find(Country.class, countryId);

//		State state = sut.save(new State("Karnataka", country));
//		State state = sut.save(new State("Punjab", country));
//		State state = sut.save(new State("Uttar Pradesh", country));
        State state = sut.save(new State("West Bengal", country));

        assertThat(state).isNotNull();
        assertThat(state.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateStatesInUS(){
        Integer countryId = 2;
        Country country = entityManager.find(Country.class, countryId);

//		State state = sut.save(new State("California", country));
//		State state = sut.save(new State("Texas", country));
//		State state = sut.save(new State("New York", country));
        State state = sut.save(new State("Washington", country));

        assertThat(state).isNotNull();
        assertThat(state.getId()).isGreaterThan(0);
    }

    @Test
    public void testListStatesByCountry() {
        Integer countryId = 2;
        Country country = entityManager.find(Country.class, countryId);
        List<State> listStates = sut.findByCountryOrderByNameAsc(country);

        listStates.forEach(System.out::println);

        assertThat(listStates.size()).isGreaterThan(0);
    }

    @Test
    public void testUpdateState() {
        Integer stateId = 3;
        String stateName = "Tamil Nadu";
        State state = sut.findById(stateId).get();

        state.setName(stateName);
        State updatedState = sut.save(state);

        assertThat(updatedState.getName()).isEqualTo(stateName);
    }

    @Test
    public void testGetState() {
        Integer stateId = 1;
        Optional<State> findById = sut.findById(stateId);
        assertThat(findById.isPresent());
    }

    @Test
    public void testDeleteState() {
        Integer stateId = 8;
        sut.deleteById(stateId);

        Optional<State> findById = sut.findById(stateId);
        assertThat(findById.isEmpty());
    }

}
