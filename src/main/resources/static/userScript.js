// userScript.js

// Чтение CSRF (если понадобится)
const token = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
const header = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');

// Функция для загрузки текущего пользователя
async function loadCurrentUser() {
    try {
        // GET-запрос к /api/user
        const response = await fetch("/api/user");
        if (!response.ok) {
            throw new Error("Ошибка при загрузке текущего пользователя");
        }
        const user = await response.json();

        // Заполняем таблицу
        document.getElementById("currentUserId").textContent = user.id;
        document.getElementById("currentUserFirstName").textContent = user.firstName;
        document.getElementById("currentUserLastName").textContent = user.lastName;
        document.getElementById("currentUserAge").textContent = user.age;
        document.getElementById("currentUserEmail").textContent = user.email;
        document.getElementById("currentUserRoles").textContent = user.roles.map(r => r.name.replace("ROLE_", "")).join(", ");

        // Заодно отобразим в navbar (email with roles)
        const navbarInfo = document.getElementById("navbarUserInfo");
        if (navbarInfo) {
            const rolesString = user.roles.map(r => r.name.replace("ROLE_", "")).join(" ");
            navbarInfo.textContent = `${user.email} with roles: ${rolesString}`;
        }

    } catch (error) {
        console.error("Не удалось загрузить пользователя:", error);
    }
}

// Когда страница загружена, вызываем loadCurrentUser()
document.addEventListener("DOMContentLoaded", () => {
    loadCurrentUser();
});