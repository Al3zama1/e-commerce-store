package com.abranlezama.ecommerceservice.service.imp;

import com.abranlezama.ecommerceservice.mapstruct.mapper.CartMapper;
import com.abranlezama.ecommerceservice.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.BDDMockito.then;

@ExtendWith(SpringExtension.class)
class CartServiceImpTest {
    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartMapper cartMapper;

    @InjectMocks
    private CartServiceImp cut;

    @Test
    void shouldReturnCustomerCartItems() {
        // Given
        long userId = 1L;

        // When
        cut.getCartItems(userId);

        // When
        then(cartRepository).should().findByCustomer_User_Id(userId);
    }

}
