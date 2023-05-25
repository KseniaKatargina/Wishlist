function toggleEditImage(element) {
    const img = element.querySelector('img');
    const input = element.querySelector('input');
    if (img.style.display === 'none') {
        img.style.display = 'initial';
        input.style.display = 'none';
        img.src = input.value;
    } else {
        img.style.display = 'none';
        input.style.display = 'initial';
        input.focus();
    }
}

function removeCategory(element) {
    let listItem = element.parentNode;
    listItem.parentNode.removeChild(listItem);
}