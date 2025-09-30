package com.everis.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;

import com.everis.util.Hooks;

/**
 * CarrinhoPage represents the shopping cart page in the application.
 * It extends BasePage, inheriting common web interaction methods such as logging
 * and element handling.
 */
public class CarrinhoPage extends BasePage {

    /**
     * Constructor initializes the page elements using Selenium's PageFactory.
     * This links the current WebDriver instance to this page object.
     */
    public CarrinhoPage() {
        PageFactory.initElements(Hooks.getDriver(), this);
    }

    /**
     * Verifies if the expected product is displayed in the shopping cart.
     * 
     * @param nomeProduto the name of the product that should appear in the cart
     * @return true if the product is present in the cart, false otherwise
     */
    public boolean apresentouProdutoEsperadoNoCarrinho(String nomeProduto) {
        boolean isProductPresent = isElementDisplayed(
                By.xpath("//*[contains(@class,'cart_item')]//a[text()='" + nomeProduto + "']")
        );

        if (isProductPresent) {
            log("The product [" + nomeProduto + "] was correctly displayed in the cart.");
            return true;
        }

        logFail("The product [" + nomeProduto + "] should have been displayed in the cart, but it was not found.");
        return false;
    }
}
