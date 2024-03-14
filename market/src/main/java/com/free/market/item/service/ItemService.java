package com.free.market.item.service;

import com.free.market.auth.PrincipalDetails;
import com.free.market.common.dto.SearchDto;
import com.free.market.common.file.FileUtils;
import com.free.market.common.paging.Pagination;
import com.free.market.common.paging.PagingResponse;
import com.free.market.file.domain.FileRequest;
import com.free.market.file.domain.FileResponse;
import com.free.market.file.mapper.FileMapper;
import com.free.market.file.service.FileService;
import com.free.market.item.domain.Item;
import com.free.market.item.domain.ItemSaveForm;
import com.free.market.item.domain.ItemUpdateForm;
import com.free.market.item.mapper.ItemMapper;
import com.free.market.member.domain.MemberResponse;
import com.free.market.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemMapper itemMapper;
    private final FileMapper fileMapper;

    /**
     * 상품 목록
     * @return List<Item>
     */
    public PagingResponse<Item> findAll(SearchDto params) {
        // 조건에 해당하는 데이터가 없는 경우, 응답 데이터에 비어있는 리스트와 null을 담아 반환
        int count = itemMapper.count(params);
        if(count < 1) {
            return new PagingResponse<>(Collections.emptyList(), null);
        }

        // pagination 객체를 생성해서 페이지 정보 계산 후 SearchDto 타입의 객체인 params에 계산된 페이지 정보 저장
        Pagination pagination = new Pagination(count, params);
        params.setPagination(pagination);

        // 계산된 페이지 정보의 일부(limitStart, recordSize)를 기준으로 리스트 데이터 조회 후 응답 데이터 반환
        List<Item> list = itemMapper.findAll(params);
        return new PagingResponse<>(list, pagination);
    }

    /**
     * 상품 등록
     * @param form
     * @return itemId
     */
    public Long save(ItemSaveForm form, Long memberId) {
        Item item = form.toItem();
        item.setMemberId(memberId);
        item.setCreateUser(memberId);
        itemMapper.save(item);
        return item.getId();
    }

    /**
     * 상품 등록
     * @param form
     * @param fileUtils
     * @param memberId - 현재 사용자 id
     * @return itemId
     * @throws IOException
     */
    @Transactional
    public Long save(ItemSaveForm form, FileUtils fileUtils, Long memberId) throws IOException {
        // item save 로직 호출
        Long itemId = save(form, memberId);

        // 첨부파일 업로드 로직 실행
        List<FileRequest> files = fileUtils.uploadFiles(form.getFiles());
        saveFile(itemId, files);

        return itemId;
    }

    /**
     * 상품 상세
     * @param id
     * @return Item
     */
    public Item findById(Long id) {
        return itemMapper.findById(id);
    }

    /**
     * 상품 삭제
     * @param id
     */
    public void delete(Long id) {
        itemMapper.delete(id);
    }

    /**
     * 상품 수정
     * @param form
     * @return itemId
     */
    public Long update(ItemUpdateForm form, Long id) {
        Item updateItem = form.toItem();
        updateItem.setUpdateUser(id);
        itemMapper.update(updateItem);
        return updateItem.getId();
    }

    /**
     * 상품 수정
     * @param form
     * @param fileUtils
     * @param id
     * @return updateItemId - 게시글 PK
     * @throws IOException
     */
    @Transactional
    public Long update(ItemUpdateForm form, FileUtils fileUtils, Long id) throws IOException {
        // 상품 수정 메서드 호출
        Long updateItemId = update(form, id);

        // 파일 업로드 (to disk)
        List<FileRequest> uploadFiles = fileUtils.uploadFiles(form.getFiles());
        
        // 파일 정보 저장 (to database)
        saveFile(form.getId(), uploadFiles);
        
        // 삭제할 파일 정보 조회 (from database)
        List<FileResponse> deleteFiles = findAllFileByIds(form.getRemoveFileIds());

        // 파일 삭제 (from disk)
        fileUtils.deleteFiles(deleteFiles);

        // 파일 삭제 (from database)
        deleteAllFileByIds(form.getRemoveFileIds());

        return updateItemId;
    }

    /**
     * 파일 저장
     * @param itemId - 게시글 PK
     * @param files
     */
    private void saveFile(Long itemId, List<FileRequest> files) {
        if(CollectionUtils.isEmpty(files)) {
            return;
        }

        for (FileRequest file : files) {
            file.setItemId(itemId);
        }

        fileMapper.saveAll(files);
    }

    /**
     * 파일 리스트 조회
     * @param ids - PK 리스트
     * @return 파일 리스트
     */
    private List<FileResponse> findAllFileByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }

        return fileMapper.findAllByIds(ids);
    }

    /**
     * 파일 삭제 (from DataBase)
     * @param ids - PK 리스트
     */
    private void deleteAllFileByIds(List<Long> ids) {
        if(CollectionUtils.isEmpty(ids)) {
            return;
        }
        fileMapper.deleteAllByIds(ids);
    }

}
