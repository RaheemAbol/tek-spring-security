package org.abol.springstarter.controllers;

import org.abol.springstarter.models.CartItem;
import org.abol.springstarter.models.BaseUser;
import org.abol.springstarter.services.CartService;
import org.abol.springstarter.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    @Autowired
    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public String viewCart(@PathVariable("userId") int userId, Model model) {
        BaseUser user = userService.getUserById(userId);
        model.addAttribute("user", user);
        model.addAttribute("cartItems", cartService.getItemsByUserId(userId));
        return "view-cart";
    }

    @GetMapping("/addItem/{userId}")
    public String showAddItemForm(@PathVariable("userId") int userId, Model model) {
        CartItem item = new CartItem();
        item.setUser(userService.getUserById(userId));
        model.addAttribute("item", item);
        return "add-item";
    }

    @PostMapping("/addItem")
    public String addItem(@ModelAttribute("item") CartItem item) {
        cartService.saveItem(item);
        return "redirect:/cart/" + item.getUser().getId();
    }

    @GetMapping("/edit/{id}")
    public String showEditItemForm(@PathVariable("id") int id, Model model) {
        CartItem item = cartService.getItemById(id);
        model.addAttribute("item", item);
        return "update-item";
    }

    @PostMapping("/edit")
    public String editItem(@ModelAttribute("item") CartItem item) {
        cartService.saveItem(item);
        return "redirect:/cart/" + item.getUser().getId();
    }

    @DeleteMapping("/delete/{id}")
    public String deleteItem(@PathVariable("id") int id) {
        CartItem item = cartService.getItemById(id);
        int userId = item.getUser().getId();
        cartService.deleteItem(id);
        return "redirect:/cart/" + userId;
    }
}
