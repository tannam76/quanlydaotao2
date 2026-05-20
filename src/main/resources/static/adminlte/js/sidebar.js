/**
 * sidebar.js - Shared sidebar navigation for all pages
 * Hệ thống Quản lý Đào tạo
 */
(function () {
  function getSidebarHTML(base, activePage) {
    const pages = {
      dashboard: base + 'index.html',
      students: base + 'pages/students.html',
      employees: base + 'pages/employees.html',
      courses: base + 'pages/courses.html',
      departments: base + 'pages/departments.html',
      majors: base + 'pages/majors.html',
      academic_years: base + 'pages/academic-years.html',
      semesters: base + 'pages/semesters.html',
      school_years: base + 'pages/school-years.html',
      users: base + 'pages/users.html',
      roles: base + 'pages/roles.html',
      permissions: base + 'pages/permissions.html',
      notifications: base + 'pages/notifications.html',
      settings: base + 'pages/settings.html',
      logs: base + 'pages/logs.html',
    };

    function a(page, label, icon) {
      const isActive = activePage === page ? ' active' : '';
      return `<li class="nav-item">
        <a href="${pages[page]}" class="nav-link${isActive}">
          <i class="nav-icon bi ${icon}"></i><p>${label}</p>
        </a>
      </li>`;
    }

    function group(label, icon, children, groupId) {
      const isOpen = children.some(c => c.page === activePage);
      return `<li class="nav-item${isOpen ? ' menu-open' : ''}">
        <a href="#" class="nav-link${isOpen ? ' active' : ''}">
          <i class="nav-icon bi ${icon}"></i>
          <p>${label}<i class="nav-arrow bi bi-chevron-right"></i></p>
        </a>
        <ul class="nav nav-treeview">${children.map(c =>
          `<li class="nav-item">
            <a href="${pages[c.page]}" class="nav-link${activePage === c.page ? ' active' : ''}">
              <i class="nav-icon bi bi-circle"></i><p>${c.label}</p>
            </a>
          </li>`
        ).join('')}</ul>
      </li>`;
    }

    return `
      <li class="nav-header">TỔNG QUAN</li>
      ${a('dashboard', 'Dashboard', 'bi-speedometer2')}

      <li class="nav-header">ĐÀO TẠO</li>
      ${a('students', 'Sinh viên', 'bi-people-fill')}
      ${a('employees', 'Nhân viên / GV', 'bi-person-badge-fill')}
      ${a('courses', 'Học phần', 'bi-book-fill')}

      <li class="nav-header">DANH MỤC</li>
      ${a('departments', 'Phòng / Khoa', 'bi-building-fill')}
      ${a('majors', 'Chuyên ngành', 'bi-diagram-3-fill')}
      ${group('Thời gian học', 'bi-calendar3', [
        { page: 'academic_years', label: 'Năm học' },
        { page: 'school_years', label: 'Niên khóa' },
        { page: 'semesters', label: 'Học kỳ' },
      ])}

      <li class="nav-header">QUẢN TRỊ</li>
      ${a('users', 'Người dùng', 'bi-person-fill-gear')}
      ${a('roles', 'Vai trò', 'bi-shield-fill')}
      ${a('permissions', 'Phân quyền', 'bi-key-fill')}
      ${a('notifications', 'Thông báo', 'bi-bell-fill')}
      ${a('settings', 'Cài đặt', 'bi-gear-fill')}
      ${a('logs', 'Nhật ký', 'bi-journal-text')}
    `;
  }

  // Auto-inject sidebar when DOM ready
  document.addEventListener('DOMContentLoaded', function () {
    const navEl = document.getElementById('sidebar-nav');
    if (!navEl) return;

    const base = navEl.getAttribute('data-base') || '/';
    const active = navEl.getAttribute('data-active') || 'dashboard';
    navEl.innerHTML = getSidebarHTML(base, active);
  });
})();
