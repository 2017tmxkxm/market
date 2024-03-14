package com.free.market.item.controller;

import com.free.market.auth.PrincipalDetails;
import com.free.market.common.dto.SearchDto;
import com.free.market.common.file.FileUtils;
import com.free.market.common.paging.PagingResponse;
import com.free.market.file.domain.FileRequest;
import com.free.market.file.domain.FileResponse;
import com.free.market.file.service.FileService;
import com.free.market.item.domain.Item;
import com.free.market.item.domain.ItemSaveForm;
import com.free.market.item.domain.ItemUpdateForm;
import com.free.market.item.service.ItemService;
import com.free.market.member.domain.MemberResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.lang.reflect.Member;
import java.util.HashMap;
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
    public String items(@ModelAttribute(name = "params") SearchDto params, Model model
                        , @AuthenticationPrincipal PrincipalDetails principalDetails) {
        PagingResponse<Item> response = itemService.findAll(params);
        model.addAttribute("response", response);

        if(principalDetails != null) {
            model.addAttribute("memberInfo", principalDetails.getUsername());
        }
        return "item/items";
    }

    @GetMapping("/add/form")
    public String addForm(Model model, @AuthenticationPrincipal MemberResponse memberResponse) {
        model.addAttribute("item", new Item());

        if(memberResponse != null){
            model.addAttribute("memberInfo", memberResponse.getLoginId());
        }

        return "item/addForm";
    }

    @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute("item") ItemSaveForm form, BindingResult bindingResult
            , RedirectAttributes redirectAttributes
            , @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {

        if(bindingResult.hasErrors()) {
            return "item/addForm";
        }

        Long itemId = itemService.save(form, fileUtils, principalDetails.getMemberResponse().getId());

        redirectAttributes.addAttribute("itemId", itemId);

        return "redirect:/item/{itemId}";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable(name = "itemId") Long itemId, Model model
            , @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Item item = itemService.findById(itemId);
        model.addAttribute("item", item);

        if(principalDetails != null) {
            model.addAttribute("loginId", principalDetails.getUsername());
            model.addAttribute("memberInfo", principalDetails.getUsername());
        } else {
            model.addAttribute("loginId", "anonymousUser");
        }

        return "/item/item";
    }

    @ResponseBody
    @GetMapping("/{itemId}/delete")
    public Map<String, Object> delete(@PathVariable(name = "itemId") Long itemId, @ModelAttribute(name = "params") SearchDto params) {

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("page", params.getPage());
        paramsMap.put("recordSize", params.getRecordSize());
        paramsMap.put("pageSize", params.getPageSize());
        paramsMap.put("itemName", params.getItemName());
        paramsMap.put("price", params.getPrice());

        Item item = itemService.findById(itemId);
        if(item != null) {
            fileService.deleteAllFileByIds(params.getFileId());
            itemService.delete(itemId);
            paramsMap.put("message", "삭제되었습니다");
        } else {
            paramsMap.put("message", "잘못된 요청입니다. 해당 게시물은 없습니다 " + itemId);
        }

        return paramsMap;
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable(name = "itemId") Long itemId, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Item item = itemService.findById(itemId);
        model.addAttribute("item", item);
        model.addAttribute("memberInfo", principalDetails.getUsername());
        return "item/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable(name = "itemId") String itemId, @Validated @ModelAttribute("item") ItemUpdateForm form
                        , HttpServletRequest request
                        , BindingResult bindingResult
                        , RedirectAttributes redirectAttributes
                        , @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {

        String url = request.getHeader("REFERER");

        String params = url.substring(url.indexOf("?") + 1);

        if(bindingResult.hasErrors()) {
            return "item/editForm";
        }

        Long updateItemId = itemService.update(form, fileUtils, principalDetails.getMemberResponse().getId());

        return "redirect:/item/" + updateItemId + "?" + params;
    }

}
