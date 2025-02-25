// ============================
// 1. Считывание CSRF-токена и названия заголовка из <meta>
const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

// ============================
// 2. Функция для подгрузки ролей из /api/roles
async function loadRoles() {
    try {
        // Для GET-запроса CSRF обычно не требуется, но можно указать headers: { [header]: token }
        const response = await fetch("/api/roles");
        if (!response.ok) {
            throw new Error("Ошибка при загрузке ролей");
        }
        const roles = await response.json();

        // Заполняем select для New User
        const newRolesSelect = document.getElementById("newRoles");
        newRolesSelect.innerHTML = "";
        roles.forEach(role => {
            const option = document.createElement("option");
            option.value = role.id;      // ROLE's ID from DB
            option.textContent = role.name.replace("ROLE_", "");  // e.g. "ROLE_ADMIN"
            newRolesSelect.appendChild(option);
        });

        // Заполняем select для Edit User
        const editRolesSelect = document.getElementById("editRoles");
        editRolesSelect.innerHTML = "";
        roles.forEach(role => {
            const option = document.createElement("option");
            option.value = role.id;
            option.textContent = role.name.replace("ROLE_", "");
            editRolesSelect.appendChild(option);
        });

    } catch (error) {
        console.error(error);
    }
}

// ============================
// 3. Загрузка текущего пользователя (GET /api/user)
async function loadCurrentUser() {
    try {
        const response = await fetch("/api/user");
        if (!response.ok) {
            throw new Error("Ошибка при загрузке текущего пользователя");
        }
        const user = await response.json();

        // Заполняем секцию "User" (tab User)
        document.getElementById("currentUserId").textContent = user.id;
        document.getElementById("currentUserFirstName").textContent = user.firstName;
        document.getElementById("currentUserLastName").textContent = user.lastName;
        document.getElementById("currentUserAge").textContent = user.age;
        document.getElementById("currentUserEmail").textContent = user.email;
        document.getElementById("currentUserRoles").textContent = user.roles.map(r => r.name.replace("ROLE_", "")).join(", ");

        // Одновременно обновим navbarUserInfo, если нужно
        const navbarInfo = document.getElementById("navbarUserInfo");
        if (navbarInfo) {
            const roles = user.roles.map(r => r.name.replace("ROLE_", "")).join(" ");
            navbarInfo.textContent = `${user.email} with roles: ${roles}`;
        }

    } catch (error) {
        console.error(error);
    }
}

// ============================
// 4. Загрузка всех пользователей (GET /api/users)
async function loadAllUsers() {
    try {
        const response = await fetch("/api/users");
        if (!response.ok) {
            throw new Error("Ошибка при загрузке списка пользователей");
        }
        const users = await response.json();

        const tableBody = document.getElementById("usersTableBody");
        tableBody.innerHTML = "";
        users.forEach(user => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${user.id}</td>
                <td>${user.firstName}</td>
                <td>${user.lastName}</td>
                <td>${user.age}</td>
                <td>${user.email}</td>
                <td>${user.roles.map(r => r.name.replace("ROLE_", "")).join(", ")}</td>
                <td>
                    <button class="btn btn-primary" onclick="showEditModal(${user.id})">Edit</button>
                </td>
                <td>
                    <button class="btn btn-danger" onclick="showDeleteModal(${user.id}, '${user.firstName}')">Delete</button>
                </td>
            `;
            tableBody.appendChild(tr);
        });
    } catch (error) {
        console.error(error);
    }
}

// ============================
// 5. Создание нового пользователя (POST /api/users)
document.getElementById("newUserForm").addEventListener("submit", async event => {
    event.preventDefault();

    // Считываем данные из формы
    const firstName = document.getElementById("newFirstName").value;
    const lastName = document.getElementById("newLastName").value;
    const age = document.getElementById("newAge").value;
    const email = document.getElementById("newEmail").value;
    const password = document.getElementById("newPassword").value;

    // Роли из multiple-select
    const rolesSelect = document.getElementById("newRoles");
    const selectedRoles = Array.from(rolesSelect.selectedOptions).map(option => ({
        id: option.value,
        name: option.text
    }));

    try {
        const response = await fetch("/api/users", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                // CSRF-заголовок
                [header]: token
            },
            body: JSON.stringify({
                firstName,
                lastName,
                age,
                email,
                password,
                roles: selectedRoles
            })
        });

        if (!response.ok) {
            throw new Error("Ошибка при создании пользователя");
        }

        // Успешное создание — обновляем таблицу
        await loadAllUsers();
        // Сбрасываем форму
        event.target.reset();
        // Переключаемся на вкладку "Users"
        document.getElementById("users-tab").click();

    } catch (error) {
        console.error(error);
    }
});

// ============================
// 6. Edit user (PATCH /api/users/{id})

// Функция, открывающая модалку Edit
async function showEditModal(userId) {
    try {
        // Можно вызвать /api/users/{id}, но для простоты загрузим всех:
        const response = await fetch("/api/users");
        if (!response.ok) {
            throw new Error("Ошибка при получении списка пользователей");
        }
        const users = await response.json();
        const user = users.find(u => u.id === userId);
        if (!user) {
            throw new Error("Пользователь не найден");
        }

        // Заполняем форму
        document.getElementById("editUserId").value = user.id;
        document.getElementById("editFirstName").value = user.firstName;
        document.getElementById("editLastName").value = user.lastName;
        document.getElementById("editAge").value = user.age;
        document.getElementById("editEmail").value = user.email;
        document.getElementById("editPassword").value = "";

        // Сбрасываем все выбранные роли, затем выделяем те, что у пользователя
        const editRolesSelect = document.getElementById("editRoles");
        for (let i = 0; i < editRolesSelect.options.length; i++) {
            editRolesSelect.options[i].selected = false;
        }
        user.roles.forEach(userRole => {
            for (let i = 0; i < editRolesSelect.options.length; i++) {
                if (editRolesSelect.options[i].value == userRole.id) {
                    editRolesSelect.options[i].selected = true;
                }
            }
        });

        // Открываем модалку
        const modal = new bootstrap.Modal(document.getElementById("editUserModal"));
        modal.show();
    } catch (error) {
        console.error(error);
    }
}

// Сабмит формы Edit
document.getElementById("editUserForm").addEventListener("submit", async event => {
    event.preventDefault();

    const userId = document.getElementById("editUserId").value;
    const firstName = document.getElementById("editFirstName").value;
    const lastName = document.getElementById("editLastName").value;
    const age = document.getElementById("editAge").value;
    const email = document.getElementById("editEmail").value;
    const password = document.getElementById("editPassword").value;

    // Собираем выбранные роли
    const editRolesSelect = document.getElementById("editRoles");
    const selectedRoles = Array.from(editRolesSelect.selectedOptions).map(option => ({
        id: option.value,
        name: option.text
    }));

    try {
        const response = await fetch(`/api/users/${userId}`, {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json",
                [header]: token
            },
            body: JSON.stringify({
                firstName,
                lastName,
                age,
                email,
                password,
                roles: selectedRoles
            })
        });

        if (!response.ok) {
            throw new Error("Ошибка при обновлении пользователя");
        }

        // Закрываем модалку
        const modal = bootstrap.Modal.getInstance(document.getElementById("editUserModal"));
        modal.hide();

        // Обновляем таблицу
        await loadAllUsers();

    } catch (error) {
        console.error(error);
    }
});


// Функция, вызываемая при нажатии "Delete" в таблице
async function showDeleteModal(userId) {
    try {
        // Можно вызвать /api/users/{id}, но для простоты загрузим всех:
        const response = await fetch("/api/users");
        if (!response.ok) {
            throw new Error("Ошибка при получении списка пользователей");
        }
        const users = await response.json();
        const user = users.find(u => u.id === userId);
        if (!user) {
            throw new Error("Пользователь не найден");
        }

        // Заполняем форму
        document.getElementById("deleteId").value = user.id;
        document.getElementById("deleteFirstName").value = user.firstName;
        document.getElementById("deleteLastName").value = user.lastName;
        document.getElementById("deleteAge").value = user.age;
        document.getElementById("deleteEmail").value = user.email;


        const rolesStr = user.roles
            .map(role => role.name.replace("ROLE_", ""))
            .join(" ");
        document.getElementById("deleteRoles").value = rolesStr;

        // Открываем модалку
        const modal = new bootstrap.Modal(document.getElementById("deleteUserModal"));
        modal.show();
    } catch (error) {
        console.error(error);
    }
}

document.getElementById("deleteUserForm").addEventListener("submit", async event => {
    event.preventDefault(); // Остановить стандартную отправку формы

    const userId = document.getElementById("deleteId").value;

    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    const response = await fetch(`/api/users/${userId}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken // Добавление CSRF-токена, если он включен в Spring Security
        }
    }).then(response => {
        if (response.ok) {
            // Закрываем модальное окно
            const deleteModal = bootstrap.Modal.getInstance(document.getElementById('deleteUserModal'));
            deleteModal.hide();
            // Обновляем список пользователей
            loadAllUsers();
        } else {
            console.error('Ошибка удаления пользователя');
        }
    }).catch(error => console.error('Ошибка сети:', error));
});
// ============================
// 8. Инициализация при загрузке страницы
document.addEventListener("DOMContentLoaded", async () => {
    // Сначала подгружаем роли
    await loadRoles();
    // Текущего юзера
    await loadCurrentUser();
    // Список всех пользователей
    await loadAllUsers();
});

