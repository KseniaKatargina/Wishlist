function getProducts() {
    const metaTags = document.getElementsByTagName("meta");
    let token, header;

    for (let i = 0; i < metaTags.length; i++) {
        if (metaTags[i].getAttribute("name") === "_csrf") {
            token = metaTags[i].getAttribute("content");
        } else if (metaTags[i].getAttribute("name") === "_csrf_header") {
            header = metaTags[i].getAttribute("content");
        }
    }
    fetch('/products', {
        method: "GET",
        credentials: "include",
        headers: {
            [header]: token
        }
    })
        .then(response => response.json())
        .then(products => {
            const tableBody = document.querySelector('#product-table tbody');
            tableBody.innerHTML = '';

            products.forEach(product => {
                const row = document.createElement('tr');

                const idData = document.createElement('td');
                idData.textContent = product.id;
                const textData = document.createElement('td');
                textData.textContent = product.text;
                const imgData = document.createElement('td');

                const img = document.createElement('img');
                img.src = product.img;
                img.alt = 'Изображение';
                imgData.appendChild(img);

                const editBtn = document.createElement('button');
                editBtn.textContent = 'Редактировать';
                editBtn.addEventListener('click', () => {
                    toEditProductPage(product.id);
                });

                const deleteBtn = document.createElement('button');
                deleteBtn.textContent = 'Удалить';
                deleteBtn.addEventListener('click', (event) => {
                    deleteProduct(product.id)
                });

                const btnContainer = document.createElement('td');
                const productBtns = document.createElement('div');
                productBtns.className = 'productBtns';

                productBtns.appendChild(editBtn);
                productBtns.appendChild(deleteBtn);
                btnContainer.appendChild(productBtns);

                row.appendChild(idData);
                row.appendChild(textData);
                row.appendChild(imgData);
                row.appendChild(btnContainer);

                tableBody.appendChild(row);
            });
        })
        .catch(error => console.error(error));
}


function deleteProduct(id) {
    let metaTags = document.getElementsByTagName("meta");
    let token, header;

    for (let i = 0; i < metaTags.length; i++) {
        if (metaTags[i].getAttribute("name") === "_csrf") {
            token = metaTags[i].getAttribute("content");
        } else if (metaTags[i].getAttribute("name") === "_csrf_header") {
            header = metaTags[i].getAttribute("content");
        }
    }

    fetch(`/products/${id}`, {
        method: 'DELETE',
        credentials: "include",
        headers: {
            [header]: token
        }
    })
        .then(response => {
            if (response.ok) {
                getProducts();
            } else {
                alert('Не удалось удалить продукт');
            }
        })
        .catch(error => console.error(error));
}

getProducts();

const addButton = document.querySelector("#addButton")

addButton.addEventListener("click", toAddProductPage)

function toAddProductPage(){
    window.location.href = "/adminPage/addProduct"
}

function toEditProductPage(productId){
    window.location.href = "/adminPage/editProduct/" + productId;
}