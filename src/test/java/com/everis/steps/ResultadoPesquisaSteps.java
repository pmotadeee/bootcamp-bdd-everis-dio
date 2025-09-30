package com.everis.steps;

import com.everis.pages.ResultadoPesquisaPage;

import io.cucumber.java.pt.Quando;

/**
 * ResultadoPesquisaSteps defines the Cucumber step definitions
 * for actions performed on the search results page.
 * It covers adding a product from the results list to the shopping cart.
 */
public class ResultadoPesquisaSteps {

    /**
     * Step definition that adds a product to the shopping cart
     * from the search results page.
     *
     * @param productName the name of the product to be added to the cart
     */
    @Quando("^adiciona o produto \"(.*)\" ao carrinho$")
    public void addProductToCart(String productName) {
        ResultadoPesquisaPage resultadoPesquisaPage = new ResultadoPesquisaPage();
        resultadoPesquisaPage.addProductToCart(productName);
    }
}
