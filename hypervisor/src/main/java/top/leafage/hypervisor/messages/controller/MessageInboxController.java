/*
 * Copyright(c) 2019-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.leafage.hypervisor.messages.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import top.leafage.hypervisor.messages.domain.vo.MessageInboxVO;
import top.leafage.hypervisor.messages.service.MessageInboxService;

/**
 * Message inbox controller.
 *
 * @author wq li
 */
@RestController
@RequestMapping("/message-inbox")
public class MessageInboxController {

    private final MessageInboxService messageInboxService;

    /**
     * Constructor for MessageInboxController.
     *
     * @param messageInboxService a {@link MessageInboxService} object
     */
    public MessageInboxController(MessageInboxService messageInboxService) {
        this.messageInboxService = messageInboxService;
    }

    /**
     * Retrieves a paginated list of records.
     *
     * @param page       The page number.
     * @param size       The number of records per page.
     * @param sortBy     The field to sort by.
     * @param descending Whether sorting should be in descending order.
     * @param filters    The filters.
     * @return A paginated list of records, or 204 status code if an error occurs.
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<Page<MessageInboxVO>> retrieve(@RequestParam int page, @RequestParam int size,
                                                         String sortBy, boolean descending, String filters) {
        Page<MessageInboxVO> voPage = messageInboxService.retrieve(page, size, sortBy, descending, filters);
        return ResponseEntity.ok(voPage);
    }

    /**
     * Fetch.
     *
     * @param id the pk.
     * @return the result.
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<MessageInboxVO> fetch(@PathVariable Long id) {
        MessageInboxVO vo = messageInboxService.fetch(id);
        return ResponseEntity.ok(vo);
    }

    /**
     * Read.
     *
     * @param id the pk.
     * @return the result.
     */
    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/{id}")
    public ResponseEntity<Boolean> read(@PathVariable Long id) {
        boolean read = messageInboxService.read(id);
        return ResponseEntity.accepted().body(read);
    }

    /**
     * Read all.
     *
     * @return the result.
     */
    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/read")
    public ResponseEntity<Boolean> readAll() {
        boolean read = messageInboxService.readAll();
        return ResponseEntity.accepted().body(read);
    }

    /**
     * Remove.
     *
     * @param id the pk.
     */
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        messageInboxService.remove(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Clear.
     *
     */
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clear() {
        messageInboxService.clear();
        return ResponseEntity.noContent().build();
    }
}
