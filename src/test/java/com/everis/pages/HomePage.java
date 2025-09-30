package com.everis.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.everis.util.Hooks;

/**
 * HomePage represents the application's main page.
 * It provides interactions with the search bar to look up products.
 * 
 * Inherits from BasePage to reuse logging and Selenium helper methods.
 */
public class HomePage extends BasePage {

    /** Search input field on the home page */
    @FindBy(css = "#search_query_top")
    protected WebElement searchField;

    /** Search button (magnifying glass icon) */
    @FindBy(name = "submit_search")
    protected WebElement searchButton;

    /**
     * Constructor initializes the web elements on this page
     * using Selenium's PageFactory.
     */
    public HomePage() {
        PageFactory.initElements(Hooks.getDriver(), this);
    }

    /**
     * Searches for a product using the search bar and search button.
     *
     * @param productName the product name to be searched
     */
    public void searchProduct(String productName) {
        searchField.sendKeys(productName);
        searchButton.click();
        log("Searched for the product: " + productName);
    }
}
