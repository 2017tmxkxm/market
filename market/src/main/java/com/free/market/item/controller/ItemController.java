package com.free.market.item.controller;

import com.free.market.common.file.FileUtils;
import com.free.market.file.domain.FileRequest;
import com.free.market.file.service.FileService;
import com.free.market.item.domain.Item;
import com.free.market.item.domain.ItemSaveForm;
import com.free.market.item.domain.ItemUpdateForm;
import com.free.market.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final FileService fileService;
    private final FileUtils fileUtils;

    @ModelAttribute("openYn")
    public Map<String, String> openYn() {
        Map<String, String> openYn = new LinkedHashMap<>();
        openYn.put("Y", "판매가능");
        openYn.put("N", "판매불가");
        return openYn;
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemService.findAll();
        model.addAttribute("items", items);
        return "item/items";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "item/addForm";
    }

    @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute("item") ItemSaveForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws IOException {

        if(bindingResult.hasErrors()) {
            return "item/addForm";
        }

        Long itemId = itemService.save(form);

        List<FileRequest> files = fileUtils.uploadFiles(form.getFiles());
        fileService.saveFile(itemId, files);

        redirectAttributes.addAttribute("itemId", itemId);

        return "redirect:/item/{itemId}";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable(name = "itemId") Long itemId, Model model) {
        Item item = itemService.findById(itemId);
        model.addAttribute("item", item);
        return "/item/item";
    }

    @ResponseBody
    @GetMapping("/{itemId}/delete")
    public String delete(@PathVariable(name = "itemId") Long itemId) {
        Item item = itemService.findById(itemId);
        if(item != null) {
            itemService.delete(itemId);
        } else {
            return "잘못된 요청입니다. 해당 게시물은 없습니다 " + itemId;
        }

        return "삭제되었습니다.";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable(name = "itemId") Long itemId, Model model) {
        Item item = itemService.findById(itemId);
        model.addAttribute("item", item);
        return "item/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable(name = "itemId") Long itemId, @Validated @ModelAttribute("item")ItemUpdateForm form
                        , BindingResult bindingResult
                        , RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            return "item/editForm";
        }

        Long updateItemId = itemService.update(form);

        redirectAttributes.addAttribute("itemId", updateItemId);
        return "redirect:/item/{itemId}";
    }
}
