const registerForm = document.getElementById('registerForm');
registerForm.addEventListener('submit', function(event) {
    if (!validateForm()) {
        event.preventDefault();
}
function validateForm() {
    const username = document.getElementById('username').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const passwordRepeat = document.getElementById('passwordRepeat').value;
    const birthday = document.getElementById('birthday').value;

    const usernameError = document.getElementById('usernameError');
    const emailError = document.getElementById('emailError');
    const passwordError = document.getElementById('passwordError');
    const passwordRepeatError = document.getElementById('passwordRepeatError');
    const birthdayError = document.getElementById('birthdayError');
    const generalError = document.getElementById('generalError');

    usernameError.innerHTML = '';
    emailError.innerHTML = '';
    passwordError.innerHTML = '';
    passwordRepeatError.innerHTML = '';
    birthdayError.innerHTML = '';
    generalError.innerHTML = '';

    if (username.length < 3 || username.length > 30) {
        usernameError.innerHTML = 'Имя пользователя должно содержать от 3 до 30 символов';
        return false;
    }
    if (!isValidEmail(email)) {
        emailError.innerHTML = 'Неверный формат email';
        return false;
    }

    if (password.length < 8 || password.length > 30) {
        passwordError.innerHTML = 'Пароль должен содержать от 8 до 30 символов';
        return false;
    }

    if (!isPasswordValid(password)) {
        passwordError.innerHTML = 'Пароль должен содержать заглавные и строчные буквы, а также цифры';
        return false;
    }

    if (password !== passwordRepeat) {
        passwordRepeatError.innerHTML = 'Пароли не совпадают';
        return false;
    }

    if (birthday === '') {
        birthdayError.innerHTML = 'Пожалуйста, укажите дату рождения';
        return false;
    }

    return true;
}
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

function isPasswordValid(password) {
    const lowercaseRegex = /[a-z]/;
    const uppercaseRegex = /[A-Z]/;
    const numberRegex = /[0-9]/;
    return lowercaseRegex.test(password) && uppercaseRegex.test(password) && numberRegex.test(password);
}
})