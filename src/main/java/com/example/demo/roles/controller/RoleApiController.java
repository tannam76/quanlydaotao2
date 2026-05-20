package com.example.demo.roles.controller;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.roles.model.entity.Role;
import com.example.demo.roles.service.RoleService;

@Tag(name = "Roles", description = "API quản lý vai trò (Roles)")
@RestController
@RequestMapping("/api/roles")
public class RoleApiController {

    private final RoleService roleService;

    public RoleApiController(RoleService roleService) {
        this.roleService = roleService;
    }

    /** GET /api/roles – Lấy tất cả vai trò */
    @Operation(summary = "Lấy tất cả vai trò", description = "Trả về danh sách toàn bộ vai trò trong hệ thống")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Thành công")
    })
    @GetMapping
    public ResponseEntity<List<Role>> getAll() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    /** GET /api/roles/paged – Lấy danh sách có phân trang */
    @Operation(summary = "Lấy vai trò có phân trang", description = "Trả về danh sách vai trò theo trang")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Thành công")
    })
    @GetMapping("/paged")
    public ResponseEntity<Page<Role>> getPaged(
            @Parameter(description = "Số trang (bắt đầu từ 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Số bản ghi mỗi trang") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(roleService.getRolesPaged(pageable));
    }

    /** GET /api/roles/{id} – Lấy vai trò theo ID */
    @Operation(summary = "Lấy vai trò theo ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tìm thấy vai trò"),
        @ApiResponse(responseCode = "404", description = "Không tìm thấy vai trò")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Role> getById(
            @Parameter(description = "UUID của vai trò") @PathVariable UUID id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    /** POST /api/roles – Tạo vai trò mới */
    @Operation(summary = "Tạo vai trò mới")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tạo thành công"),
        @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ")
    })
    @PostMapping
    public ResponseEntity<Role> create(@RequestBody Role role) {
        return ResponseEntity.ok(roleService.createRole(role));
    }

    /** PUT /api/roles/{id} – Cập nhật vai trò */
    @Operation(summary = "Cập nhật vai trò theo ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
        @ApiResponse(responseCode = "404", description = "Không tìm thấy vai trò")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Role> update(
            @Parameter(description = "UUID của vai trò") @PathVariable UUID id,
            @RequestBody Role role) {
        return ResponseEntity.ok(roleService.updateRole(id, role));
    }

    /** DELETE /api/roles/{id} – Xoá mềm vai trò */
    @Operation(summary = "Xoá vai trò (soft delete)")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Xoá thành công"),
        @ApiResponse(responseCode = "404", description = "Không tìm thấy vai trò")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "UUID của vai trò") @PathVariable UUID id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    /** POST /api/roles/{roleId}/permissions/{permId} – Gán quyền cho vai trò */
    @Operation(summary = "Gán quyền cho vai trò")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Gán quyền thành công"),
        @ApiResponse(responseCode = "404", description = "Không tìm thấy vai trò hoặc quyền")
    })
    @PostMapping("/{roleId}/permissions/{permId}")
    public ResponseEntity<Role> addPermission(
            @Parameter(description = "UUID của vai trò") @PathVariable UUID roleId,
            @Parameter(description = "UUID của quyền") @PathVariable UUID permId) {
        return ResponseEntity.ok(roleService.addPermission(roleId, permId));
    }

    /** DELETE /api/roles/{roleId}/permissions/{permId} – Gỡ quyền khỏi vai trò */
    @Operation(summary = "Gỡ quyền khỏi vai trò")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Gỡ quyền thành công"),
        @ApiResponse(responseCode = "404", description = "Không tìm thấy vai trò")
    })
    @DeleteMapping("/{roleId}/permissions/{permId}")
    public ResponseEntity<Role> removePermission(
            @Parameter(description = "UUID của vai trò") @PathVariable UUID roleId,
            @Parameter(description = "UUID của quyền") @PathVariable UUID permId) {
        return ResponseEntity.ok(roleService.removePermission(roleId, permId));
    }
}