package com.shopme.admin.brand;

import com.shopme.common.entity.Brand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class BrandServiceTests {

    @MockBean
    private BrandRepository brandRepositoryMock;

    @InjectMocks
    private BrandServiceImpl sut;

    @Test
    public void testCheckUniqueInNewModeReturnDuplicate(){
        Integer id = null;
        String name = "Acer";
        Brand brand = new Brand(name);

        Mockito.when(brandRepositoryMock.findByName(name)).thenReturn(brand);

        String result = sut.checkUnique(id, name);
        assertThat(result).isEqualTo("Duplicate");

    }

    @Test
    public void testCheckUniqueInNewModeReturnOK(){
        Integer id = null;
        String name = "AMD";

        Mockito.when(brandRepositoryMock.findByName(name)).thenReturn(null);

        String result = sut.checkUnique(id, name);
        assertThat(result).isEqualTo("OK");
    }

    @Test
    public void testCheckUniqueInEditModeReturnDuplicate(){
        Integer id = null;
        String name = "Canon";
        Brand brand = new Brand(id, name);

        Mockito.when(brandRepositoryMock.findByName(name)).thenReturn(brand);

        String result = sut.checkUnique(id, name);
        assertThat(result).isEqualTo("Duplicate");

    }
}
