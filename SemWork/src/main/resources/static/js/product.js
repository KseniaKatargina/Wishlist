document.querySelectorAll('.wish').forEach(button => {
    button.addEventListener('click', (event) => {
        event.preventDefault();
        const listID = button.parentElement.querySelector('#listID').value;
        const prodID = button.parentElement.querySelector('#prodID').value;
        let metaTags = document.getElementsByTagName("meta");
        let token, header;

        for (let i = 0; i < metaTags.length; i++) {
            if (metaTags[i].getAttribute("name") === "_csrf") {
                token = metaTags[i].getAttribute("content");
            } else if (metaTags[i].getAttribute("name") === "_csrf_header") {
                header = metaTags[i].getAttribute("content");
            }
        }
        fetch(`/addProduct/${prodID}/${listID}`, {
            method: 'POST',
            credentials:"include",
            headers: {
                [header]: token
            }
        })
            .then(response => {
                if (!response.ok) {
                    alert('Этот товар уже есть в этом вишлисте');
            }})
            .catch(error => {
                console.error('Error:', error);
                alert('Произошла ошибка');
            });
    });
});

document.querySelectorAll('.removeProductButton').forEach(button => {
    button.addEventListener('click', (event) => {
        event.preventDefault();
        const listID = button.parentElement.querySelector('#listID').value;
        const prodID = button.parentElement.querySelector('#prodID').value;
        let metaTags = document.getElementsByTagName("meta");
        let token, header;

        for (let i = 0; i < metaTags.length; i++) {
            if (metaTags[i].getAttribute("name") === "_csrf") {
                token = metaTags[i].getAttribute("content");
            } else if (metaTags[i].getAttribute("name") === "_csrf_header") {
                header = metaTags[i].getAttribute("content");
            }
        }

        fetch(`/removeProduct/${prodID}/${listID}`, {
            method: 'DELETE',
            credentials: "include",
            headers:{
                [header]: token
            }
        })
            .then(response => {
                if (response.ok) {
                    // alert('Успешно удалено');
                    const productElement = document.getElementById(`product-${prodID}`);
                    if (productElement) {
                        productElement.remove();
                    }
                } else {
                    alert('Не удалось удалить товар');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Не удалось удалить товар');
            });
    });
});
