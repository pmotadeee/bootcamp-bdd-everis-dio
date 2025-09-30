package com.everis.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.everis.util.Hooks;

/**
 * ResultadoPesquisaPage represents the product search results page.
 * It provides functionality to add a product to the shopping cart.
 *
 * Inherits from BasePage to reuse driver utilities, waits, and logging.
 */
public class ResultadoPesquisaPage extends BasePage {

    /** "Add to cart" button available in search results */
    @FindBy(xpath = "//*[text()='Add to cart']")
    protected WebElement addToCartButton;

    /** "Proceed to checkout" button shown after adding a product */
    @FindBy(css = "[title='Proceed to checkout']")
    protected WebElement proceedToCheckoutButton;

    /**
     * Constructor initializes the web elements on this page
     * using Selenium's PageFactory.
     */
    public ResultadoPesquisaPage() {
        PageFactory.initElements(Hooks.getDriver(), this);
    }

    /**
     * Adds the specified product to the shopping cart.
     * It first locates the product by its name, hovers over it,
     * then clicks on "Add to cart" and proceeds to checkout.
     *
     * @param productName the name of the product to be added to the cart
     */
    public void addProductToCart(String productName) {
        WebElement productElement = driver.findElement(
                By.xpath(".//*[@itemprop='name']/*[contains(text(), '" + productName + "')] "
                        + "| .//*[@itemprop='name'][text()='" + productName + "']"));

        moveToElement(productElement);
        addToCartButton.click();
        waitElement(proceedToCheckoutButton, 10).click();
        log("Added product [" + productName + "] to the cart.");
    }
}
