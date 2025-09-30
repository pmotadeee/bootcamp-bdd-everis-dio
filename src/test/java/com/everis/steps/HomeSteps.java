package com.everis.steps;

import com.everis.pages.HomePage;
import com.everis.util.Hooks;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;

/**
 * HomeSteps defines the Cucumber step definitions for the home page.
 * It handles actions such as navigating to the site and searching for products.
 */
public class HomeSteps {

    /**
     * Step definition that opens the given site URL in Chrome.
     *
     * @param url the website URL to be accessed
     */
    @Dado("^que um usuario acessa o site \"(.*)\"$")
    public void accessSite(String url) {
        Hooks.navigateToULRChrome(url);
    }

    /**
     * Step definition that searches for a product by its name
     * using the search bar on the home page.
     *
     * @param productName the name of the product to search for
     */
    @E("^pesquisa pelo produto \"(.*)\"$")
    public void searchProduct(String productName) {
        HomePage homePage = new HomePage();
        homePage.searchProduct(productName);
    }
}
