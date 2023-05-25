const a = document.querySelector(".showPass");
const input = document.querySelector(".password_input");
const icon = document.getElementById("i");
a.addEventListener('click', ()=> {
    if ( input.getAttribute('type') === 'password'){
        input.removeAttribute('type');
        input.setAttribute('type', 'text');
        icon.className = 'far fa-eye-slash';
    } else {
        input.removeAttribute('type');
        input.setAttribute('type', 'password');
        icon.className = 'far fa-eye';
    }
})
const re = document.querySelector(".showRePass");
const re_input = document.querySelector(".re_password_input");
const ic = document.getElementById("i2");

re.addEventListener('click', ()=> {
    if ( re_input.getAttribute('type') === 'password'){
        re_input.removeAttribute('type');
        re_input.setAttribute('type', 'text');
        ic.className = 'far fa-eye-slash';
    } else {
        re_input.removeAttribute('type');
        re_input.setAttribute('type', 'password');
        ic.className = 'far fa-eye';
    }
})

const newPass = document.querySelector(".showNewPass");
const new_input = document.querySelector(".new_password_input");
const ico = document.getElementById("i3") ;

newPass.addEventListener('click', ()=> {
    if ( new_input.getAttribute('type') === 'password'){
        new_input.removeAttribute('type');
        new_input.setAttribute('type', 'text');
        ico.className = 'far fa-eye-slash';
    } else {
        new_input.removeAttribute('type');
        new_input.setAttribute('type', 'password');
        ico.className = 'far fa-eye';
    }
})