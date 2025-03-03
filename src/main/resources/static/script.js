const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

async function loadRoles() {
    try {
        const response = await fetch("/api/admin/roles");
        if (!response.ok) {
            throw new Error("Ошибка при загрузке ролей");
        }
        const roles = await response.json();

        const newRolesSelect = document.getElementById("newRoles");
        newRolesSelect.innerHTML = "";
        roles.forEach(role => {
            const option = document.createElement("option");
            option.value = role.id;      // ROLE's ID from DB
            option.textContent = role.name.replace("ROLE_", "");  // e.g. "ROLE_ADMIN"
            newRolesSelect.appendChild(option);
        });

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

async function loadCurrentUser() {
    try {
        const response = await fetch("/api/admin/user");
        if (!response.ok) {
            throw new Error("Ошибка при загрузке текущего пользователя");
        }
        const user = await response.json();

        document.getElementById("currentUserId").textContent = user.id;
        document.getElementById("currentUserFirstName").textContent = user.firstName;
        document.getElementById("currentUserLastName").textContent = user.lastName;
        document.getElementById("currentUserAge").textContent = user.age;
        document.getElementById("currentUserEmail").textContent = user.email;
        document.getElementById("currentUserRoles").textContent = user.roles.map(r => r.name.replace("ROLE_", "")).join(" ");

        const navbarInfo = document.getElementById("navbarUserInfo");
        if (navbarInfo) {
            const roles = user.roles.map(r => r.name.replace("ROLE_", "")).join(" ");
            navbarInfo.textContent = `${user.email} with roles: ${roles}`;
        }

    } catch (error) {
        console.error(error);
    }
}

async function loadAllUsers() {
    try {
        const response = await fetch("/api/admin/users");
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
                <td>${user.roles.map(r => r.name.replace("ROLE_", "")).join(" ")}</td>
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

document.getElementById("newUserForm").addEventListener("submit", async event => {
    event.preventDefault();

    const firstName = document.getElementById("newFirstName").value;
    const lastName = document.getElementById("newLastName").value;
    const age = document.getElementById("newAge").value;
    const email = document.getElementById("newEmail").value;
    const password = document.getElementById("newPassword").value;

    const rolesSelect = document.getElementById("newRoles");
    const selectedRoles = Array.from(rolesSelect.selectedOptions).map(option => ({
        id: option.value,
        name: option.text
    }));

    try {
        const response = await fetch("/api/admin/users", {
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

        await loadAllUsers();
        event.target.reset();
        document.getElementById("users-tab").click();

    } catch (error) {
        console.error(error);
    }
});
async function showEditModal(userId) {
    try {
        const response = await fetch("/api/admin/users");
        if (!response.ok) {
            throw new Error("Ошибка при получении списка пользователей");
        }
        const users = await response.json();
        const user = users.find(u => u.id === userId);
        if (!user) {
            throw new Error("Пользователь не найден");
        }

        document.getElementById("editUserId").value = user.id;
        document.getElementById("editFirstName").value = user.firstName;
        document.getElementById("editLastName").value = user.lastName;
        document.getElementById("editAge").value = user.age;
        document.getElementById("editEmail").value = user.email;
        document.getElementById("editPassword").value = "";

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

        const modal = new bootstrap.Modal(document.getElementById("editUserModal"));
        modal.show();
    } catch (error) {
        console.error(error);
    }
}

document.getElementById("editUserForm").addEventListener("submit", async event => {
    event.preventDefault();

    const userId = document.getElementById("editUserId").value;
    const firstName = document.getElementById("editFirstName").value;
    const lastName = document.getElementById("editLastName").value;
    const age = document.getElementById("editAge").value;
    const email = document.getElementById("editEmail").value;
    const password = document.getElementById("editPassword").value;

    const editRolesSelect = document.getElementById("editRoles");
    const selectedRoles = Array.from(editRolesSelect.selectedOptions).map(option => ({
        id: option.value,
        name: option.text
    }));

    try {
        const response = await fetch(`/api/admin/users/${userId}`, {
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

        const modal = bootstrap.Modal.getInstance(document.getElementById("editUserModal"));
        modal.hide();

        await loadAllUsers();

    } catch (error) {
        console.error(error);
    }
});


async function showDeleteModal(userId) {
    try {
        const response = await fetch("/api/admin/users");
        if (!response.ok) {
            throw new Error("Ошибка при получении списка пользователей");
        }
        const users = await response.json();
        const user = users.find(u => u.id === userId);
        if (!user) {
            throw new Error("Пользователь не найден");
        }
        document.getElementById("deleteUserId").value = user.id;
        document.getElementById("deleteFirstName").value = user.firstName;
        document.getElementById("deleteLastName").value = user.lastName;
        document.getElementById("deleteAge").value = user.age;
        document.getElementById("deleteEmail").value = user.email;


        const rolesStr = user.roles
            .map(role => role.name.replace("ROLE_", ""))
            .join(" ");
        document.getElementById("deleteRoles").value = rolesStr;

        const modal = new bootstrap.Modal(document.getElementById("deleteUserModal"));
        modal.show();
    } catch (error) {
        console.error(error);
    }
}

document.getElementById("deleteUserForm").addEventListener("submit", async event => {
    event.preventDefault(); // Остановить стандартную отправку формы

    const userId = document.getElementById("deleteUserId").value;

    const response = await fetch(`/api/admin/users/${userId}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            [header]: token
        }
    }).then(response => {
        if (response.ok) {
            const deleteModal = bootstrap.Modal.getInstance(document.getElementById('deleteUserModal'));
            deleteModal.hide();
            loadAllUsers();
        } else {
            console.error('Ошибка удаления пользователя');
        }
    }).catch(error => console.error('Ошибка сети:', error));
});
document.addEventListener("DOMContentLoaded", async () => {
    await loadRoles();
    await loadCurrentUser();
    await loadAllUsers();
});

