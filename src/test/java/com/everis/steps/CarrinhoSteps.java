package com.everis.steps;

import org.junit.Assert;

import com.everis.pages.CarrinhoPage;

import io.cucumber.java.pt.Entao;

/**
 * CarrinhoSteps defines the Cucumber step definitions related to the cart.
 * It validates whether the expected product is present in the shopping cart.
 */
public class CarrinhoSteps {

    /**
     * Step definition that asserts a product is present in the cart.
     * 
     * @param productName the name of the product that should be displayed in the cart
     */
    @Entao("^o produto \"(.*)\" deve estar presente no carrinho$")
    public void shouldDisplayExpectedProductInCart(String productName) {
        CarrinhoPage carrinhoPage = new CarrinhoPage();
        Assert.assertTrue(
                "The product [" + productName + "] should have been displayed in the cart.",
                carrinhoPage.apresentouProdutoEsperadoNoCarrinho(productName)
        );
    }
}
