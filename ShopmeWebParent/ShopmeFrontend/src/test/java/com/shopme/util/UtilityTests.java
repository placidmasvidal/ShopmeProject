package com.shopme.util;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.*;

public class UtilityTests {

    @Test
    void testFormatCurrency_whenMocked_thenReturnsMockSuccessfully() {

        try (MockedStatic<Utility> utilityMockedStatic = Mockito.mockStatic(Utility.class)) {
            utilityMockedStatic.when(() -> Utility.formatCurrency(1000.99f)).thenReturn("€ 1.000,99");
            assertThat(Utility.formatCurrency(1000.99f)).isEqualTo("€ 1.000,99");
        }

        assertThat(Utility.formatCurrency(1000.99f)).isEqualTo("€ 1.000,99");
    }

}
