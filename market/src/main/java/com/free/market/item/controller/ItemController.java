package com.free.market.item.controller;

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
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
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
    public String items(@ModelAttribute(name = "params") SearchDto params, Model model) {
        PagingResponse<Item> response = itemService.findAll(params);
        log.info("params={}", params);
        model.addAttribute("response", response);
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
    @Transactional
    public Map<String, Object> delete(@PathVariable(name = "itemId") Long itemId, @ModelAttribute(name = "params") SearchDto params) {

        log.info("fileId={}", params.getFileId());

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
    public String editForm(@PathVariable(name = "itemId") Long itemId, Model model) {
        Item item = itemService.findById(itemId);
        model.addAttribute("item", item);
        return "item/editForm";
    }

    @PostMapping("/{itemId}/edit")
    @Transactional
    public String edit(@PathVariable(name = "itemId") String itemId, @Validated @ModelAttribute("item") ItemUpdateForm form
                        , HttpServletRequest request
                        , BindingResult bindingResult
                        , RedirectAttributes redirectAttributes) throws IOException {

        String url = request.getHeader("REFERER");

        String params = url.substring(url.indexOf("?") + 1);

        if(bindingResult.hasErrors()) {
            return "item/editForm";
        }

        log.info("form={}", form.toString());
        Long updateItemId = itemService.update(form);

        // 파일 업로드 (to disk)
        List<FileRequest> uploadFiles = fileUtils.uploadFiles(form.getFiles());

        // 파일 정보 저장 (to database)
        fileService.saveFile(form.getId(), uploadFiles);

        // 삭제할 파일 정보 조회 (from database)
        List<FileResponse> deleteFiles = fileService.findAllFileByIds(form.getRemoveFileIds());

        // 파일 삭제 (from disk)
        fileUtils.deleteFiles(deleteFiles);

        // 파일 삭제 (from database)
        fileService.deleteAllFileByIds(form.getRemoveFileIds());

        return "redirect:/item/" + updateItemId + "?" + params;
    }

    @ResponseBody
    //@PostMapping("/{itemId}/edit")
    public String edit(@PathVariable(name = "itemId") Long itemId, @Validated @ModelAttribute("item") ItemUpdateForm form
            , HttpServletRequest request
            , BindingResult bindingResult) throws Exception {

        String url = request.getHeader("REFERER");

        String params = url.substring(url.indexOf("?") + 1);

        if(bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "item/editForm";
        }

        // 파일 업로드 (to disk)
        List<FileRequest> uploadFiles = fileUtils.uploadFiles(form.getFiles());

        // 파일 정보 저장 (to database)
        fileService.saveFile(form.getId(), uploadFiles);

        // 삭제할 파일 정보 조회 (from database)
        List<FileResponse> deleteFiles = fileService.findAllFileByIds(form.getRemoveFileIds());

        // 파일 삭제 (from disk)
        fileUtils.deleteFiles(deleteFiles);

        // 파일 삭제 (from database)
        fileService.deleteAllFileByIds(form.getRemoveFileIds());

        Long updatedItemId = itemService.update(form);
        log.info("form={}", form.toString());


        return updatedItemId + "?" + params;
    }
}
